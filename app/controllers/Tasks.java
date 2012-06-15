package controllers;

import models.Project;
import models.Task;
import models.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import views.html.todo.taskListPanel;
import views.html.todo.taskPanel;

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

    private static Task checkAndGetTask(Long taskId) {
        Task task = Task.getTasksById(taskId);
        User user = MicroSession.getUser();
        Project project = Project.getProjectById(task.project.id);
        if (task != null && project.user.id == user.id) {
            return task;
        }
        return null;
    }

    public static Result updateTaskState(Long taskId, String state) {
        Task task = checkAndGetTask(taskId);
        if (task != null) {
            task.finished = "true".equals(state) ? true : false;
            Task.save(task);
            return ok();
        } else {
            return badRequest();
        }
    }

    public static Result updateTaskName(Long taskId, String title) {
        Task task = checkAndGetTask(taskId);
        if (task != null) {
            task.title = title;
            Task.save(task);
            return ok(taskPanel.render(task));
        } else {
            return badRequest();
        }
    }

    public static Result updateTaskDueDate(Long taskId, String dueDate) {
        Task task = checkAndGetTask(taskId);
        if (task != null) {
            task.dueDate = DateTime.parse(dueDate, DateTimeFormat.forPattern(Messages.get("time.format")));
            Task.save(task);
            return ok(taskPanel.render(task));
        } else {
            return badRequest();
        }
    }

    public static Result updateTask(Long taskId) {
        final Form<Task> taskForm = Controller.form(Task.class).bindFromRequest();
        User user = MicroSession.getUser();
        Task task = Task.getTasksById(taskId);
        Project project = Project.getProjectById(task.project.id);
        if (task != null && project.user.id == user.id) {
            Task.updateUser(task.id, taskForm.get());
            return ok(taskPanel.render(task));
        } else {
            return badRequest();
        }
    }

}
