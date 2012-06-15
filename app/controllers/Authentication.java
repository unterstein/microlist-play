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
            return Results.redirect(routes.TodoList.todoDefault());
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
        setUserToSession(email);
        return Results.redirect(routes.TodoList.todoDefault());
    }

    private static void setUserToSession(String email) {
        Controller.flash("success", Messages.get("welcome") + " " + email);
        Controller.session(Secured.AUTH_SESSION, "" + email);
    }

    public static Result registerUser() {
        final Form<Register> registerForm = Controller.form(Register.class).bindFromRequest();
        if (registerForm.hasErrors()) {
            return ok(registerPanel.render(registerForm));
        } else {
            setUserToSession(registerForm.get().email);
            return ok("" + routes.TodoList.todoDefault()); // just return route
        }
    }

    public static Result registerPanel() {
        return ok(registerPanel.render(new Form<Authentication.Register>(Authentication.Register.class)));
    }

    public static Result logout() {
        Controller.session().clear();
        return Results.redirect(routes.Authentication.login());
    }

    public static class Login {
        @Formats.NonEmpty
        @Required(message = "error.mailRequired")
        @Email(message = "error.noMail")
        public String email;
        @Required(message = "password.required")
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
        @Required(message = "password.required")
        @MaxLength(value = 10)
        @MinLength(value = 5)
        public String password;

        public String repassword;

        @Formats.NonEmpty
        @Required(message = "error.mailRequired")
        @Email(message = "error.noMail")
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

            User.create(this.email, this.password);

            return null;
        }
    }
}
