package net.nikdev.autobroadcast.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.nikdev.autobroadcast.util.Conditions.orElse;

public final class Chat {

    private Chat() {}

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', orElse(message, ""));
    }

    public static List<String> color(List<String> messages) {
        return orElse(messages, new ArrayList<String>()).stream().map(Chat::color).collect(Collectors.toList());
    }

}
