package controllers;

import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        return TodoController.todo();
    }


    // -- Javascript routing

    public static Result jsRoutes() {
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("jsRoutes",
                controllers.routes.javascript.Authentication.loginUser(),
                controllers.routes.javascript.Authentication.registerUser(),
                controllers.routes.javascript.Authentication.registerPanel()
                ));
    }
}