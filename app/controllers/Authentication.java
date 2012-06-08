package controllers;

import models.User;
import play.data.Form;
import play.data.format.Formats;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.Secured;
import views.html.login;
import views.html.signin.registerPanel;

public class Authentication extends Controller {

    public static Result login() {
        if (User.checkIfUserExsists(Controller.session(Secured.AUTH_SESSION))) {
            return Results.redirect(routes.TodoList.todo());
        } else {
            return ok(login.render(new Form<Authentication.Login>(Authentication.Login.class)));
        }
    }

    public static Result loginUser() {
        final Form<Login> loginForm = Controller.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return Results.badRequest(login.render(loginForm));
        } else {
            return sucessfullyLoggedIn(loginForm.get().email);
        }
    }

    private static Result sucessfullyLoggedIn(String email) {
        Controller.flash("success", Messages.get("welcome") + email);
        Controller.session(Secured.AUTH_SESSION, "" + email);
        return Results.redirect(routes.TodoList.todo());
    }

    public static Result registerUserAjax() {
        final Form<Register> registerForm = Controller.form(Register.class).bindFromRequest();
        if (registerForm.hasErrors()) {
            return ok(registerPanel.render(registerForm));
        } else {
            return sucessfullyLoggedIn(registerForm.get().email);
        }
    }

    public static Result registerUser() {
        final Form<Register> registerForm = Controller.form(Register.class).bindFromRequest();
        if (registerForm.hasErrors()) {
            Controller.flash("error", registerForm.errorsAsJson().toString()); // TODO ajax error handling
            return Results.redirect(routes.Authentication.login());
        } else {
            return sucessfullyLoggedIn(registerForm.get().email);
        }
    }

    public static Result registerPanel() {
        return ok(registerPanel.render(new Form<Authentication.Register>(Authentication.Register.class)));
    }

    public static class Login {
        @Formats.NonEmpty
        // TODO i18n
        @Required(message = "Email is required")
        public String email;
        // TODO i18n
        @Required(message = "Password is needed")
        public String password;
        public boolean remember;

        public String validate() {
            final User user = User.authenticate(this.email, this.password);
            if (user == null) {
                return Messages.get("error.signInFailed");
            }
            return null;
        }
    }

    public static class Register {

        @Formats.NonEmpty
        // TODO i18n
        @Required(message = "Password is needed")
        @MaxLength(value = 10)
        @MinLength(value = 5)
        public String password;

        public String repassword;

        @Formats.NonEmpty
        // TODO i18n
        @Required(message = "Email is required")
        // TODO i18n
        @Email(message = "The entered Email is not an email")
        public String email;

        public String validate() {
            if (this.password.equals(this.repassword) == false) {
                return Messages.get("error.passwordNotMatch");
            }

            // check if the email is unique
            final boolean checkIfUserExsists = User.checkIfUserExsists(this.email);
            if (checkIfUserExsists == true) {
                return Messages.get("error.duplicateEmail");
            }

            final User user = new User();
            user.email = this.email;
            user.password = this.password;
            User.create(user);

            return null;
        }
    }
}
