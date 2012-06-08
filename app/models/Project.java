package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Project extends Model {

    @Id
    public Long id;

    @Required
    @Formats.NonEmpty
    public String name;

    @OneToOne
    public User user;

    private static Model.Finder<Long, Project> find = new Model.Finder<Long, Project>(Long.class, Project.class);

    /**
     * Saves the project to the database
     * 
     * @param project
     */
    public static Project create(final String name) {
        Project project = new Project();
        project.name = name;
        project.save();
        return project;
    }

    /**
     * Saves the project to the database
     * 
     * @param project
     */
    public static void create(final Project project) {
        project.save();
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
     * Gets a {@link List} of {@link Project}s for the given user
     * 
     * @param user
     * @return
     */
    public static List<Project> getProjectsByUser(final User user) {
        return find.where().eq("user", user).findList();
    }
}
