package controllers;

import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import security.Secured;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        return Results.redirect(routes.TodoList.todo());
    }


    // -- Javascript routing

    public static Result jsRoutes() {
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("jsRoutes",
                controllers.routes.javascript.Authentication.registerUser(),
                controllers.routes.javascript.Authentication.registerPanel(),
                controllers.routes.javascript.TodoList.projects(),
                controllers.routes.javascript.TodoList.addProject(),
                controllers.routes.javascript.TodoList.updateProject(),
                controllers.routes.javascript.TodoList.removeProject()
                ));
    }
}