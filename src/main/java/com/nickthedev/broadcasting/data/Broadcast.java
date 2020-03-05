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
package com.nickthedev.broadcasting.data;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Data object to represent a plugin broadcast that has been loaded from the config.
 *
 * @author NickTheDev
 * @since 3.0
 */
public class Broadcast {

    private final List<String> messages;
    private final List<String> worlds;
    private final List<ItemStack> items;
    private final String permission, title, subtitle;
    private final Sound sound;

    /**
     * Creates a new broadcast object with the specified info.
     *
     * @param messages Messages sent out by this broadcast.
     * @param worlds Worlds that the broadcast can be viewed in.
     * @param items Items given to all players by this broadcast.
     * @param permission Permission needed ot experience this broadcast.
     * @param sound Sound played by the broadcast.
     */
    public Broadcast(List<String> messages, List<String> worlds, List<ItemStack> items, String permission, String title, String subtitle, Sound sound) {
        this.messages = messages;
        this.worlds = worlds;
        this.items = items;
        this.permission = permission;
        this.title = title;
        this.subtitle = subtitle;
        this.sound = sound;
    }

    /**
     * Gets the messages to broadcast in chat.
     *
     * @return Messages to chat.
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Gets the worlds that this broadcast can be seen in.
     *
     * @return Worlds this broadcast supports.
     */
    public List<String> getWorlds() {
        return worlds;
    }

    /**
     * Gets the items to give certain players.
     *
     * @return Items to give.
     */
    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * Gets the sound played by this broadcast which may be null.
     *
     * @return Sound played.
     */
    public Optional<Sound> getSound() {
        return Optional.ofNullable(sound);
    }

    /**
     * Gets the title played by this broadcast if any.
     *
     * @return Optional title.
     */
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    /**
     * Gets the subtitle played by this broadcast if any.
     *
     * @return Optional subtitle.
     */
    public Optional<String> getSubtitle() {
        return Optional.ofNullable(subtitle);
    }

    /**
     * Checks if a player has permission to see this.
     *
     * @param player Player to check.
     * @return If the player passes the check.
     */
    public boolean checkPermission(Player player) {
        return permission == null || player.hasPermission(permission);
    }

}
