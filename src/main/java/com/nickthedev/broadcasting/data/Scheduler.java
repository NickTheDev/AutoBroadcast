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

import com.nickthedev.broadcasting.Broadcasting;
import com.nickthedev.broadcasting.compatability.MaterialMatcher;
import com.nickthedev.broadcasting.compatability.SoundMatcher;
import com.nickthedev.broadcasting.compatability.TitleMatcher;
import com.nickthedev.broadcasting.util.Chat;
import com.nickthedev.broadcasting.util.NumberSelector;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main scheduler of the plugin that creates a continuous task for broadcasting messages from the config.
 *
 * @author NickTheDev
 * @since 3.0
 */
public class Scheduler {

    /**
     * Default broadcast interval in seconds.
     */
    private static final int DEFAULT_INTERVAL = 60;

    /**
     * Default repeat rule which decides if broadcasts can be repeated.
     */
    private static final boolean DEFAULT_REPEATS = true;

    /**
     * Minimum config schema version that marks a break in back compatibility.
     */
    private static final double MIN_VERSION = 3;

    private final List<Broadcast> broadcasts = new ArrayList<>();
    private boolean muted, reset;
    private boolean repeat = DEFAULT_REPEATS;
    private int interval = DEFAULT_INTERVAL;

    /**
     * Gets if the broadcast scheduler is muted and therefor won't broadcast.
     *
     * @return If the scheduler is muted and will stay silent.
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Gets the scheduler interval between broadcasts.
     *
     * @return Schedule interval.
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Gets if the scheduler allows repeats.
     *
     * @return Repeat rule.
     */
    public boolean allowsRepeat() {
        return repeat;
    }

    /**
     * Unmodifiable getter for all loaded broadcasts, for possible future API purposes.
     *
     * @return Loaded broadcasts.
     */
    public List<Broadcast> getBroadcasts() {
        return Collections.unmodifiableList(broadcasts);
    }

    /**
     * Toggles whether or not the scheduler is muted.
     */
    public void toggleMute() {
        muted = !muted;
    }

    /**
     * Loads the plugin data from the config.
     *
     * Vastly comprehensive deserialize procedure that inspects a broadcast object in the config
     * and try to load it into usable data. Any typos or invalid data pieces will be acknowledged
     * in the log and a default data value will be used instead.
     */
    public void load(boolean first) {
        FileConfiguration config = Broadcasting.get().getConfig();
        ConfigurationSection section = config.getConfigurationSection("broadcasts");

        reset = !first;
        muted = false;
        broadcasts.clear();

        if(config.getDouble("config-version", 0) < MIN_VERSION) {
            Broadcasting.get().getLogger().warning("Unsupported config version for this plugin version (Config schema is outdated)." +
                    "Please delete your config and allow the plugin to generate a fresh one, disabling.");
            Bukkit.getPluginManager().disablePlugin(Broadcasting.get());
            return;
        }

        if(config.contains("interval")) {
            interval = config.getInt("interval", DEFAULT_INTERVAL);
        } else {
            Broadcasting.get().getLogger().warning("Invalid interval specified in the config, defaulting to 60 seconds, please check your config.");
        }

        if(config.contains("allow-repeats")) {
            repeat = config.getBoolean("allow-repeats", DEFAULT_REPEATS);
        } else {
            Broadcasting.get().getLogger().warning("Invalid allow-repeats rule specified in the config, defaulting to true, please check your config.");
        }

        if(section == null) {
            Broadcasting.get().getLogger().warning("No broadcasts have been found in the plugin, so nothing will be scheduled or shown in chat.");

        } else {
            section.getKeys(false).forEach(key -> {
                ConfigurationSection data = section.getConfigurationSection(key);
                List<String> messages = Chat.color(data.getStringList("messages"));
                List<ItemStack> items = new ArrayList<>();
                List<String> worlds = new ArrayList<>();
                Sound sound = null;
                String permission = data.getString("permission");
                String title = data.getString("title");
                String subtitle = data.getString("subtitle");

                data.getStringList("worlds").stream().filter(world -> Bukkit.getWorld(world) != null).forEach(worlds::add);

                if(data.contains("items")) {
                    try {
                        ConfigurationSection itemsSection = data.getConfigurationSection("items");

                        if(itemsSection != null && !itemsSection.getKeys(false).isEmpty()) {
                            itemsSection.getKeys(false).forEach(itemKey -> {
                                ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemKey);

                                items.add(new ItemBuilder(MaterialMatcher.fromString(itemSection.getString("type")).parseMaterial())
                                        .amount(itemSection.getInt("amount")).name(itemSection.getString("name")).lore(itemSection.getStringList("lore")).build());
                            });

                        }
                    } catch(Exception e) {
                        Broadcasting.get().getLogger().warning("Invalid items found in config" + key + ", defaulting to no items for the broadcast.");
                    }
                }

                if(data.contains("sound")) {
                    try {
                        sound = SoundMatcher.valueOf(data.getString("sound")).toNative();

                    } catch(Exception e) {
                        Broadcasting.get().getLogger().warning("Invalid sound found in config" + key + ", defaulting to no sound for the broadcast.");
                    }
                }

                broadcasts.add(new Broadcast(messages, worlds, items, permission, title, subtitle, sound));
            });

            Broadcasting.get().getLogger().info(broadcasts.size() + " broadcasts have been found in the plugin, creating the scheduler now.");

            if(first) {
                schedule();
            }
        }

    }

    private void schedule() {
        NumberSelector selector = new NumberSelector(repeat, broadcasts.size());

        new BukkitRunnable() {

            @Override
            public void run() {
                if(reset) {
                    reset = false;
                    schedule();
                    cancel();

                    return;
                }

                broadcast(broadcasts.get(selector.next()));
            }

        }.runTaskTimer(Broadcasting.get(), 2, interval * 20);
    }

    /**
     * Tries to broadcast a broadcast object as efficiently as possible in the following steps.
     *
     * 1. Check if plugin is muted, return if so.
     * 2. Iterate through all online players or filtered players if there is a permission.
     * 3. Send all colored messages to players.
     * 4. Give all items to players.
     * 5. Play the sound.
     *
     * @param cast Broadcast to play.
     */
    public void broadcast(Broadcast cast) {
        if(isMuted()) {
            return;
        }

        if(cast.getWorlds().isEmpty()) {
            Bukkit.getOnlinePlayers().stream().filter(cast::checkPermission).forEach(player -> apply(player, cast));
        } else {
            cast.getWorlds().stream().map(Bukkit::getWorld).forEach(world -> world.getPlayers().stream()
                    .filter(cast::checkPermission).forEach(player -> apply(player, cast)));
        }

    }

    /**
     * Applies the effects of a broadcast to a player.
     *
     * @param player Player to apply to.
     * @param cast Broadcast to apply.
     */
    private void apply(Player player, Broadcast cast) {
        if(cast.getTitle().isPresent() || cast.getSubtitle().isPresent()) {
            TitleMatcher.send(player, cast.getTitle().orElse(""), cast.getSubtitle().orElse(""), 1, 3, 1);
        }

        for(String message : cast.getMessages()) {
            player.sendMessage(message);
        }

        for(ItemStack item : cast.getItems()) {
            player.getInventory().addItem(item);
        }

        cast.getSound().ifPresent(sound -> player.playSound(player.getLocation(), sound, 1, 1));
    }

}