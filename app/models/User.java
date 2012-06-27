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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.i18n.Messages;

import com.avaje.ebean.annotation.Encrypted;

@Entity
public class User extends Model {

    @Id
    public Long id;

    @Required
    @Formats.NonEmpty
    @Encrypted
    public String password;

    @Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String email;

    private static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);

    /**
     * Saves the user to the database
     * 
     * @param email
     * @param password
     */
    public static User create(final String email, final String password) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.save();
        Project.create(Messages.get("project.inboxName"), user);
        return user;
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
