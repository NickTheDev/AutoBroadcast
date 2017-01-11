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
package net.nikdev.autobroadcast.broadcast;

import net.nikdev.autobroadcast.AutoBroadcast;
import net.nikdev.autobroadcast.event.BroadCastEvent;
import net.nikdev.autobroadcast.util.Chat;
import net.nikdev.autobroadcast.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static net.nikdev.autobroadcast.util.Conditions.notNull;

public class BroadCastManager {

    private static final Random RANDOM = new Random();

    private final AutoBroadcast plugin;
    private final List<BroadCast> broadcasts = new ArrayList<>();

    private boolean enabled = true;

    public BroadCastManager(AutoBroadcast plugin) {
        this.plugin = plugin;

        load();
        createTimer();
    }

    public List<BroadCast> getTimedBroadcasts() {
        return Collections.unmodifiableList(broadcasts);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        this.enabled = !enabled;
    }

    private void load() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("Broadcasts");

        if(section != null && !section.getKeys(false).isEmpty()) {
            section.getKeys(false).forEach(key -> {
                ConfigurationSection broadcast = section.getConfigurationSection(key);

                Sound sound = null;

                if(notNull(broadcast.getString("Sound"))) {
                    try {
                        sound = Sound.valueOf(broadcast.getString("Sound"));

                    } catch(IllegalArgumentException ignored) {}

                }

                List<ItemFactory> items = new ArrayList<>();
                ConfigurationSection itemsSection = broadcast.getConfigurationSection("Items");

                if(itemsSection != null && !itemsSection.getKeys(false).isEmpty()) {
                    itemsSection.getKeys(false).forEach(itemKey -> {
                        ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemKey);

                        ItemFactory item = new ItemFactory(Material.matchMaterial(itemSection.getString("Type")), itemSection.getInt("Amount"));

                        item.lore(itemSection.getStringList("Lore"));
                        List<String> enchants = itemSection.getStringList("Enchantments");

                        if (enchants != null && !enchants.isEmpty()) {
                            enchants.stream().filter(enchantKey -> Enchantment.getByName(enchantKey) != null).forEach(enchantKey -> item.enchant(Enchantment.getByName(enchantKey)));
                        }

                        items.add(item);
                    });

                }

                broadcasts.add(new BroadCast(broadcast.getStringList("Messages"), items, broadcast.getString("permission"), sound));
            });
        }

        plugin.getLogger().info(getTimedBroadcasts().size() + " Broadcast(s) loaded successfully!");
    }

    private void createTimer() {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (getTimedBroadcasts().isEmpty()) {
                    this.cancel();
                }

                broadcast(getTimedBroadcasts().get(RANDOM.nextInt(getTimedBroadcasts().size())));
            }

        }.runTaskTimer(plugin, 1, plugin.getConfig().getInt("Interval"));

    }

    public void broadcast(BroadCast broadcast) {
        if(notNull(broadcast) && isEnabled()) {
            BroadCastEvent event = new BroadCastEvent(broadcast);

            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) {
                return;
            }

            if(broadcast.getPermission().isPresent()) {
                if(!broadcast.getMessages().isEmpty()) {
                    broadcast.getMessages().forEach(message -> Bukkit.broadcast(Chat.color(message), broadcast.getPermission().get()));
                }

                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission(broadcast.getPermission().get())).forEach(p -> {
                    if (broadcast.getSound().isPresent()) {
                        p.playSound(p.getLocation(),broadcast.getSound().get(), 1.0F, 0.0F);
                    }

                    if (!broadcast.getItems().isEmpty()) {
                        broadcast.getItems().forEach(item -> p.getInventory().addItem(item.create()));
                    }

                });

                return;
            }

            broadcast.getMessages().forEach(msg -> Bukkit.broadcastMessage(Chat.color(msg)));

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (broadcast.getSound().isPresent()) {
                    p.playSound(p.getLocation(),broadcast.getSound().get(), 1.0F, 0.0F);
                }

                if (!broadcast.getItems().isEmpty()) {
                    broadcast.getItems().forEach(item -> p.getInventory().addItem(item.create()));
                }

            });

        }

    }



}