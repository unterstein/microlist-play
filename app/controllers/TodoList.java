package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class TodoList extends Controller {

    public static Result todo() {
        return TODO;
    }
}