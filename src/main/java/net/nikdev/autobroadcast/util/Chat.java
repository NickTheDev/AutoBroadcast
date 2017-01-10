/*
Copyright 2017 NickTheDev

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
