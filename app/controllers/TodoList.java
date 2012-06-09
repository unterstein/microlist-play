package controllers;

import models.Project;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import views.html.todo.projectListPanel;

@Security.Authenticated(Secured.class)
public class TodoList extends Controller {

    public static Result todo() {
        return ok(views.html.todoList.render(Project.getProjectsByUser(User.getUserByEMail(Controller.session(Secured.AUTH_SESSION)))));
    }

    public static Result update(String id, String name) {
        Project project = Project.getProjectById(Long.valueOf(id));
        project.name = name;
        project.save();
        return ok(projectListPanel.render(project));
    }
}
