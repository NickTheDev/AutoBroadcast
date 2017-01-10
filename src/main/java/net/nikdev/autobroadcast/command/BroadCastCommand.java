package net.nikdev.autobroadcast.command;

import net.nikdev.autobroadcast.AutoBroadcast;
import net.nikdev.autobroadcast.broadcast.Broadcast;
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

        plugin.getBroadCastManager().broadcast(new Broadcast(Collections.singletonList(Chat.color(format.replaceAll("%sender%", sender instanceof ConsoleCommandSender ? "Console" : sender.getName()).replaceAll("%message%", message.toString()))), null, null, null));

        return true;
    }

}
