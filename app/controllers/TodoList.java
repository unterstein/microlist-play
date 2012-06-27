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

import models.Project;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import security.Secured;
import views.html.todo.projectListPanel;
import views.html.todo.projectPanel;

@Security.Authenticated(Secured.class)
public class TodoList extends Controller {

    public static Result todoDefault() {
        User user = MicroSession.getUser();
        return ok(views.html.todoList.render(Project.getProjectsByUser(user), Project.getDefaultProject(user)));
    }

    public static Result todo(Long projectId) {
        User user = MicroSession.getUser();
        Project project = Project.getProjectById(projectId);
        if (project != null && project.user.id == user.id) {
            return ok(views.html.todoList.render(Project.getProjectsByUser(user), project));
        } else {
            return Results.redirect(routes.TodoList.todoDefault());
        }
    }

    public static Result addProject(Long lastActiveProject) {
        Project.create(Messages.get("project.defaultName"));
        return ok(projectListPanel.render(Project.getProjectsByUser(MicroSession.getUser()), Project.getProjectById(lastActiveProject)));
    }

    public static Result updateProject(Long id, String name, Long selectedProject) {
        Project project = Project.getProjectById(id);
        if (project.user.id == MicroSession.getUser().id && project.id != Project.getDefaultProject(project.user).id) {
            project.name = name;
            project.save();
            // default project could not be updated, so we can deliver false here
            return ok(projectPanel.render(project, false, Project.getProjectById(selectedProject)));
        } else {
            return badRequest(); // TODO
        }
    }

    public static Result removeProject(Long id) {
        Project project = Project.getProjectById(id);
        if (project.user.id == MicroSession.getUser().id) {
            Project.remove(project);
            return ok();
        } else {
            return badRequest(); // TODO
        }
    }

}
