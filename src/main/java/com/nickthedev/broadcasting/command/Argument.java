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
package com.nickthedev.broadcasting.command;

import org.bukkit.command.CommandSender;

/**
 * Argument of the {@link CommandManager}.
 *
 * @author NickTheDev
 * @since 1.0
 */
public interface Argument {

    /**
     * Executes this command argument with the specified information.
     *
     * @param sender Sender of the command.
     * @param args Arguments of the command.
     * @throws CommandException Thrown if an error occurs executing.
     */
    void execute(CommandSender sender, String[] args) throws CommandException;

}
