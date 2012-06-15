package controllers;

import models.Project;
import models.Task;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import views.html.todo.taskListPanel;

@Security.Authenticated(Secured.class)
public class Tasks extends Controller {

    public static Result removeTask(Long id) {
        Task task = Task.getTasksById(id);
        Project project = Project.getProjectById(task.project.id);
        if (project.user.id == MicroSession.getUser().id) {
            Task.remove(task);
            return ok();
        } else {
            return badRequest(); // TODO
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

}
