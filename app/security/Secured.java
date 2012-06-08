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
