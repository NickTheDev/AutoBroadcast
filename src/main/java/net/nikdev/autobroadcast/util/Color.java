package net.nikdev.autobroadcast.util;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Color {

    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> c(List<String> s) {
        return s.stream().map(Color::c).collect(Collectors.toList());
    }

}
