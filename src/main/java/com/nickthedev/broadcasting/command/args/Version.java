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
 * Argument that allows admins to check the plugin version.
 *
 * @author NickTheDev
 * @since 3.0
 */
@ArgInfo(name = "version", help = "Checks the plugin version.", permission = "autobroadcast.verion")
public class Version implements Argument {

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        Chat.to(sender, Chat.INFO, "You are currently running version &a&l" + Broadcasting.get().getDescription().getVersion() + "&f&l.");
    }

}
