/*
Copyright 2020 NickTheDev

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
package com.nickthedev.broadcasting;

import com.nickthedev.broadcasting.command.CommandManager;
import com.nickthedev.broadcasting.data.Scheduler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Driver class responsible for initializing the plugin.
 *
 * @author NickTheDeb
 * @since 3.0
 */
public class Broadcasting extends JavaPlugin {

    private static Broadcasting instance;
    private Scheduler scheduler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Created by NickTheDev, with much <3");

        instance = this;

        CommandManager command = new CommandManager();

        getCommand("autobroadcast").setExecutor(command);
        getCommand("autobroadcast").setTabCompleter(command);

        scheduler = new Scheduler();
        scheduler.load(true);
    }

    /**
     * Gets the broadcast scheduler.
     *
     * @return Scheduler instance.
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Gets the global instance of this plugin.
     *
     * @return Plugin instance.
     */
    public static Broadcasting get() {
        return instance;
    }

}
