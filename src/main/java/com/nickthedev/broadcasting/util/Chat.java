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
package com.nickthedev.broadcasting.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for dealing with Bukkit chat colors.
 *
 * @author NickTheDev
 * @since 1.0
 */
public enum Chat {

    INFO("&a&lAUTO&d&lBROADCAST &b&l\u25C6 &f&l"),
    ERROR("&a&lAUTO&d&lBROADCAST &b&l\u25C6 &c&l");

    private final String prefix;

    /**
     * Declares the chat type with the prefix.
     *
     * @param prefix Prefix of the chat.
     */
    Chat(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Sends a colored and formatted message to a command sender.
     *
     * @param sender Sender of the command.
     * @param type Type of prefix.
     * @param text Text of the message.
     */
    public static void to(CommandSender sender, Chat type, String text) {
        sender.sendMessage(color(type.prefix + text));
    }

    /**
     * Sends a colored and formatted message to a command sender.
     *
     * @param sender Sender of the command.
     * @param text Text of the message.
     */
    public static void to(CommandSender sender, String text) {
        sender.sendMessage(color(text));
    }

    /**
     * Utility for translating messages to native minecraft colors.
     *
     * @param text Text to color.
     * @return Colored text.
     */
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Uses the color method over a list of text.
     *
     * @param texts Text list to color.
     * @return Colored texts.
     */
    public static List<String> color(List<String> texts) {
        return texts.stream().map(Chat::color).collect(Collectors.toList());
    }

}
