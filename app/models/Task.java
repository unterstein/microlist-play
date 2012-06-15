package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

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
}
