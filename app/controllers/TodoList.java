package controllers;

import models.Project;
import models.Task;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import security.Secured;
import views.html.todo.projectListPanel;
import views.html.todo.projectPanel;
import views.html.todo.taskListPanel;

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

    public static Result changeTaskState(Long taskId, String state) {
        Task task = Task.getTasksById(taskId);
        User user = MicroSession.getUser();
        if (task != null && task.project.user.id == user.id) {
            task.finished = "true".equals(state) ? true : false;
            task.save();
            return ok();
        } else {
            return badRequest();
        }
    }

    public static Result addTask(Long projectId, String taskName) {
        Project project = Project.getProjectById(projectId);
        User userFromSession = MicroSession.getUser();
        if (project != null && project.user.id == userFromSession.id) {
            if (taskName != null && !taskName.equals("")) {
                Task.create(taskName, project);
            }
            return ok(taskListPanel.render(project));
        } else {
            return badRequest(); // TODO
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

    public static Result removeTask(Long id) {
        Task task = Task.getTasksById(id);
        if (task.project.user.id == MicroSession.getUser().id) {
            Task.remove(task);
            return ok();
        } else {
            return badRequest(); // TODO
        }
    }
}
