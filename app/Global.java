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
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

public class Global extends GlobalSettings {

    private OwnEbeanPlugin ebeanPlugin;

    @Override
    public void onStart(Application app) {
        Logger.info("Application has started");
        ebeanPlugin = new OwnEbeanPlugin(app);
        ebeanPlugin.onStart();
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
        ebeanPlugin.onStop();
    }

    @Override
    public Result onError(RequestHeader arg0, Throwable arg1) {
        return super.onError(arg0, arg1);
    }

    @Override
    public Result onBadRequest(RequestHeader arg0, String arg1) {
        return super.onBadRequest(arg0, arg1);
    }
}