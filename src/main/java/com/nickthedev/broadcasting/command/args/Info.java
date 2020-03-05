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
package com.nickthedev.broadcasting.command.args;

import com.nickthedev.broadcasting.Broadcasting;
import com.nickthedev.broadcasting.command.ArgInfo;
import com.nickthedev.broadcasting.command.Argument;
import com.nickthedev.broadcasting.command.CommandException;
import com.nickthedev.broadcasting.util.Chat;
import org.bukkit.command.CommandSender;

/**
 * Argument that allows admins to check info about the broadcast schedule.
 *
 * @author NickTheDev
 * @since 3.0
 */
@ArgInfo(name = "info", help = "Displays info about the broadcast schedule.", permission = "autobroadcast.info")
public class Info implements Argument {

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        Chat.to(sender, Chat.INFO, "Here's the schedule info:");
        Chat.to(sender, "   &e&lBroadcast count: &a" + Broadcasting.get().getScheduler().getBroadcasts().size() + " &fhave been loaded.");
        Chat.to(sender, "   &e&lInterval: &a" + Broadcasting.get().getScheduler().getInterval() + " &fseconds between each broadcast.");
        Chat.to(sender, "   &e&lAllows repeats: &a" + Broadcasting.get().getScheduler().allowsRepeat() + "&f; whether or not repeats are allowed.");
    }

}
