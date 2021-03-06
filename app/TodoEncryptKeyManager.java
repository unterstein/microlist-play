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
import play.Configuration;
import play.Logger;

import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.config.EncryptKeyManager;

public class TodoEncryptKeyManager implements EncryptKeyManager {
    private EncryptKey key;
    private String keyAsString;

    @Override
    public void initialise() {
        try {
            keyAsString = Configuration.root().getString("aes.key");
        } catch (Exception e) {
            Logger.error("Unable to find configuration property aes.key", e);
        }
        key = new EncryptKey() {

            @Override
            public String getStringValue() {
                return keyAsString;
            }
        };
    }

    @Override
    public EncryptKey getEncryptKey(String tableName, String columnName) {
        return key;
    }
}
