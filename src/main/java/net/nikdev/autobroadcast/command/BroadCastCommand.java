/*
Copyright 2017 NickTheDev <http://nikdev.net/>

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
package net.nikdev.autobroadcast.command;

import net.nikdev.autobroadcast.AutoBroadcast;
import net.nikdev.autobroadcast.broadcast.BroadCast;
import net.nikdev.autobroadcast.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Collections;
import java.util.stream.Stream;

public class BroadCastCommand implements CommandExecutor {

    private final AutoBroadcast plugin;

    public BroadCastCommand(AutoBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if(!sender.hasPermission("autobroadcast.broadcast") || !sender.isOp()) {
            sender.sendMessage(Chat.color(plugin.getConfig().getString("No-Permission")));

            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(Chat.color("&cNo messages specified, please use /broadcast <messages>."));

            return false;
        }

        String format = Chat.color(plugin.getConfig().getString("Broadcast-Command-Format"));
        StringBuilder message = new StringBuilder();

        Stream.of(args).forEach(arg -> message.append(arg).append(" "));

        plugin.getBroadCastManager().broadcast(new BroadCast(Collections.singletonList(Chat.color(format.replaceAll("%sender%", sender instanceof ConsoleCommandSender ? "Console" : sender.getName()).replaceAll("%message%", message.toString()))), null, null, null));

        return true;
    }

}
