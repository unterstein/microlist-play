package controllers;

import models.Project;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import views.html.todo.projectListPanel;
import views.html.todo.projectPanel;

@Security.Authenticated(Secured.class)
public class TodoList extends Controller {

    public static Result todo() {
        return ok(views.html.todoList.render(Project.getProjectsByUser(User.getUserByEMail(Controller.session(Secured.AUTH_SESSION)))));
    }

    public static Result projects() {
        String email = Controller.session(Secured.AUTH_SESSION);
        return ok(projectListPanel.render(Project.getProjectsByUser(User.getUserByEMail(email))));
    }

    public static Result addProject() {
        String email = Controller.session(Secured.AUTH_SESSION);
        Project project = new Project();
        User user = User.getUserByEMail(email);
        project.user = user;
        project.name = Messages.get("project.defaultName");
        Project.create(project);
        return ok(projectListPanel.render(Project.getProjectsByUser(user)));
    }

    public static Result update(String id, String name) {
        Project project = Project.getProjectById(Long.valueOf(id));
        project.name = name;
        project.save();
        return ok(projectPanel.render(project));
    }
}
