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
import views.html.login;
import views.html.signin.registerPanel;

public class Authentication extends Controller {

    public static Result login() {
        return ok(login.render("login", new Form<Authentication.Login>(Authentication.Login.class)));
    }

    public static Result loginUser() {
        // final Form<> registerForm = Controller.form(Register.class).bindFromRequest();
        return ok();
    }

    public static Result registerUser() {
        // final Form<> registerForm = Controller.form(Register.class).bindFromRequest();
        return ok();
    }

    public static Result registerPanel() {
        return ok(registerPanel.render(new Form<Authentication.Register>(Authentication.Register.class)));
    }

    public static class Login {
        public String username;
        public String password;

        public String validate() {

            final User user = User.authenticate(this.username, this.password);
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
