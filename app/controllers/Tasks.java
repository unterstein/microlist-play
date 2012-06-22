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
 * @package     microlist-play
 */
package controllers;

import models.Project;
import models.Task;
import models.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

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
            return ok(taskPanel.render(Task.getTasksById(task.id)));
        } else {
            return badRequest();
        }
    }

    public static Result updateTaskDueDate(Long taskId, String dueDate) {
        Task task = checkAndGetTask(taskId);
        if (task != null) {
            task.dueDate = DateTime.parse(dueDate, DateTimeFormat.forPattern(Messages.get("time.format")));
            Task.save(task);
            return ok(taskPanel.render(Task.getTasksById(task.id)));
        } else {
            return badRequest();
        }
    }

    public static Result updateTaskDescription(Long taskId, String description) {
        Task task = checkAndGetTask(taskId);
        if (task != null) {
            task.description = description;
            Task.save(task);
            return ok(taskPanel.render(Task.getTasksById(task.id)));
        } else {
            return badRequest();
        }
    }

    public static Result updateTaskDescriptionNull(Long taskId) {
        return updateTaskDescription(taskId, null);
    }

    public static class Description {
        public String description;
    }

}
