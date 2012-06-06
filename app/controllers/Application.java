package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(login.render());
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
        return ok();
    }

    // -- Javascript routing

    public static Result jsRoutes() {
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("jsRoutes",
                controllers.routes.javascript.Application.loginUser(),
                controllers.routes.javascript.Application.registerUser(),
                controllers.routes.javascript.Application.registerPanel()
                ));
    }
}