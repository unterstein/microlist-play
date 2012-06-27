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
 * @project     microlist-play
 */
package security;

import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import controllers.routes;

/**
 * Security token
 */
public class Secured extends Security.Authenticator {

    public static final String AUTH_SESSION = "email";

    @Override
    public String getUsername(Context ctx) {
        String username = ctx.session().get(AUTH_SESSION);
        if (username != null) {
            boolean checkIfUserExsists = User.checkIfUserExsists(username);
            if (checkIfUserExsists == false) {
                ctx.session().clear();
                return null;
            }
        }
        return username;
    }

    @Override
    public Result onUnauthorized(Context arg0) {
        return redirect(routes.Authentication.login());
    }
}
