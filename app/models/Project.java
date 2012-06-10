package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import controllers.MicroSession;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.i18n.Messages;

@Entity
public class Project extends Model {

    @Id
    public Long id;

    @Required
    @Formats.NonEmpty
    public String name;

    @OneToOne
    @Required
    public User user;

    private static Model.Finder<Long, Project> find = new Model.Finder<Long, Project>(Long.class, Project.class);

    /**
     * Saves the project to the database
     * 
     * @param name
     * @param user
     * @return
     */
    public static Project create(final String name, final User user) {
        Project project = new Project();
        project.name = name;
        project.user = user;
        project.save();
        return project;
    }

    /**
     * Saves the project to the database
     * 
     * @param name
     * @return
     */
    public static Project create(final String name) {
        return create(name, MicroSession.getUser());
    }

    /**
     * Deletes the project from the database
     * 
     * @param project
     */
    public static void remove(final Project project) {
        List<Task> tasks = Task.getTasksByProject(project);
        for (Task task : tasks) {
            Task.remove(task);
        }
        project.delete();
    }

    /**
     * Gets a {@link Project} by the given id
     * 
     * @param id
     * @return
     */
    public static Project getProjectById(final Long id) {
        return find.where().eq("id", id).findUnique();
    }

    /**
     * Gets a {@link List} of {@link Project}s for the given {@link User}
     * 
     * @param user
     * @return
     */
    public static List<Project> getProjectsByUser(final User user) {
        return find.where().eq("user", user).findList();
    }

    public static Project getDefaultProject(final User user) {
        List<Project> list = find.where().eq("user", user).findList();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            // at least the default project should exist, lets create!
            return Project.create(Messages.get("project.inboxName"), user);
        }
    }
}
