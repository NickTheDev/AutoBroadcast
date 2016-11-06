package net.nikdev.autobroadcast.command;

import net.nikdev.autobroadcast.AutoBroadcast;
import net.nikdev.autobroadcast.broadcast.BroadCast;
import net.nikdev.autobroadcast.util.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class BroadCastCommand implements CommandExecutor {

    private final AutoBroadcast plugin;

    public BroadCastCommand(AutoBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(!sender.hasPermission("autobroadcast.broadcast") || !sender.isOp()) {
            sender.sendMessage(Color.c(plugin.getConfig().getString("No-Permission")));

            return false;

        }

        if (args.length == 0) {
            sender.sendMessage(Color.c("&cNo arguments specified, do /broadcast <message>."));

            return false;

        }

        String format = plugin.getConfig().getString("Broadcast-Command-Format");
        StringBuilder message = new StringBuilder();

        Arrays.stream(args).forEach(arg -> message.append(arg).append(" "));

        plugin.getBroadCastManager().broadcast(new BroadCast(Collections.singletonList(Color.c(format.replaceAll("%sender%", sender.getName()).replaceAll("%message%", message.toString()))), new ArrayList<>(), Optional.empty(), Optional.empty()));

        return false;
    }

}
