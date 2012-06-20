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
package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.i18n.Messages;

@Entity
public class Task extends Model {
    @Id
    public Long id;

    @Required
    @Formats.NonEmpty
    public String title;

    public String description;
    public boolean finished;
    public DateTime dueDate;

    @OneToOne
    @Required
    public Project project;

    public String niceDueDate() {
        return DateTimeFormat.forPattern(Messages.get("time.format")).print(this.dueDate);
    }

    private static Model.Finder<Long, Task> find = new Model.Finder<Long, Task>(Long.class, Task.class);

    /**
     * Saves the task to the database
     * 
     * @param title
     * @param project
     */
    public static void create(final String title, final Project project) {
        Task task = new Task();
        task.title = title;
        task.project = Project.getProjectById(project.id);
        task.save();
    }

    /**
     * Gets a {@link List} of {@link Task}s for the given {@link Project}
     * 
     * @param project
     * @return
     */
    public static List<Task> getTasksByProject(Project project) {
        return find.where().eq("project", project).findList();
    }

    /**
     * Gets a {@link Task} for the given id
     * 
     * @param id
     * @return
     */
    public static Task getTasksById(Long id) {
        return find.where().eq("id", id).findUnique();
    }

    /**
     * Deletes the task from the database
     * 
     * @param task
     */
    public static void remove(Task task) {
        task.delete();
    }

    /**
     * Saves or updates the {@link Task} to the database
     * 
     * @param task
     */
    public static void save(Task task) {
        task.save();
    }

    /**
     * Updates the {@link Task} from database
     * 
     * @param taskId
     * @param task
     */
    public static void updateUser(Long taskId, Task task) {
        Task taskFromDb = getTasksById(taskId);

        taskFromDb.description = task.description;
        taskFromDb.dueDate = task.dueDate;
        taskFromDb.finished = task.finished;
        taskFromDb.project = task.project;
        taskFromDb.title = task.title;

        taskFromDb.save();
    }
}
