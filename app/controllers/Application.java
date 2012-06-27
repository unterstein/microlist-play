/**
 *  Copyright (C) 2012 Johannes Unterstein
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author      Johannes Unterstein <unterstein@me.com>
 * @copyright   Copyright 2012, Johannes Unterstein
 * @license     http://www.gnu.org/licenses/gpl.html GPLv3
 * @project     microlist-play
 */
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
        return Results.redirect(routes.TodoList.todoDefault());
    }


    // -- Javascript routing

    public static Result jsRoutes() {
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("jsRoutes",
                controllers.routes.javascript.Authentication.registerUser(),
                controllers.routes.javascript.Authentication.registerPanel(),
                controllers.routes.javascript.TodoList.addProject(),
                controllers.routes.javascript.TodoList.updateProject(),
                controllers.routes.javascript.TodoList.removeProject(),
                controllers.routes.javascript.Tasks.addTask(),
                controllers.routes.javascript.Tasks.removeTask(),
                controllers.routes.javascript.Tasks.updateTaskName(),
                controllers.routes.javascript.Tasks.updateTaskState(),
                controllers.routes.javascript.Tasks.updateTaskDueDate(),
                controllers.routes.javascript.Tasks.updateTaskDescription()
                ));
    }
}