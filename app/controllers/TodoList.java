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
        return ok(projectListPanel.render(Project.getProjectsByUser(MicroSession.getUser())));
    }

    public static Result addProject() {
        Project project = new Project();
        User user = MicroSession.getUser();
        project.user = user;
        project.name = Messages.get("project.defaultName");
        Project.create(project);
        return ok(projectListPanel.render(Project.getProjectsByUser(user)));
    }

    public static Result updateProject(String id, String name) {
        Project project = Project.getProjectById(Long.valueOf(id));
        if (project.user.id == MicroSession.getUser().id) {
            project.name = name;
            project.save();
            return ok(projectPanel.render(project));
        } else {
            return badRequest(); // TODO
        }
    }

    public static Result removeProject(String id) {
        Project project = Project.getProjectById(Long.valueOf(id));
        if (project.user.id == MicroSession.getUser().id) {
            Project.remove(project);
            return ok();
        } else {
            return badRequest(); // TODO
        }
    }
}
