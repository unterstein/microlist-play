package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class User extends Model {

    @Id
    public Long id;

    @Required
    @Formats.NonEmpty
    public String password;

    @Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String email;

    private static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);

    /**
     * Saves the user to the database
     * 
     * @param user
     */
    public static void create(final User user) {
        user.save();
    }

    public static User authenticate(final String email, final String password) {
        return find.where().eq("email", email).eq("password", password).findUnique();
    }

    /**
     * Checks if a {@link User} wit the given email exists
     * 
     * @param username
     * @return
     */
    public static boolean checkIfUserExsists(final String email) {
        return getUserByEMail(email) != null;
    }

    /**
     * Gets a {@link User} by the given email
     * 
     * @param username
     * @return
     */
    public static User getUserByEMail(final String email) {
        return find.where().eq("email", email).findUnique();
    }

}