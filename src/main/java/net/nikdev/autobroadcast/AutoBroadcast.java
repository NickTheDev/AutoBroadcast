/*
Copyright 2017 NickTheDev

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.nikdev.autobroadcast;

import net.nikdev.autobroadcast.broadcast.BroadCastManager;
import net.nikdev.autobroadcast.command.BroadCastCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoBroadcast extends JavaPlugin {

    private BroadCastManager broadCastManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Created by NickTheDev, " + getDescription().getWebsite());

        broadCastManager = new BroadCastManager(this);

        getCommand("broadcast").setExecutor(new BroadCastCommand(this));
    }

    public BroadCastManager getBroadCastManager() {
        return broadCastManager;
    }

}
