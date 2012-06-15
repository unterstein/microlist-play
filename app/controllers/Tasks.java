package controllers;

import models.Project;
import models.Task;
import play.mvc.Controller;
import play.mvc.Result;

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
}
