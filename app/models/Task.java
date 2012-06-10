package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import controllers.MicroSession;

@Entity
public class Task extends Model {
    @Id
    public Long id;

    @Required
    @Formats.NonEmpty
    public String title;

    @OneToOne
    @Required
    public User user;

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
        task.user = MicroSession.getUser();
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
     * Deletes the task from the database
     * 
     * @param task
     */
    public static void remove(Task task) {
        task.delete();
    }
}
