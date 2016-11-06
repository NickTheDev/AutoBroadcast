package net.nikdev.autobroadcast.broadcast;

import net.nikdev.autobroadcast.AutoBroadcast;
import net.nikdev.autobroadcast.util.Color;
import net.nikdev.autobroadcast.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BroadCastManager {

    private final AutoBroadcast plugin;
    private List<BroadCast> broadcasts = new ArrayList<>();

    public BroadCastManager(AutoBroadcast plugin) {
        this.plugin = plugin;

        load();
        runTimer();

    }

    public List<BroadCast> getBroadcasts() {
        return Collections.unmodifiableList(broadcasts);
    }

    private void load() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("Broadcasts");

        if(section != null && !section.getKeys(false).isEmpty()) {
            section.getKeys(false).stream().forEach(key -> {
                ConfigurationSection broadcast = section.getConfigurationSection(key);

                List<String> messagesStr = broadcast.getStringList("Messages");
                List<String> messages = new ArrayList<>();

                if(messagesStr != null && !messagesStr.isEmpty()) {
                    messages = messagesStr;
                }

                String soundStr = broadcast.getString("Sound");
                Optional<Sound> sound;

                try {
                    sound = Optional.of(Sound.valueOf(soundStr));

                } catch(Exception e) {
                    sound = Optional.empty();
                }

                List<ItemFactory> items = new ArrayList<>();
                ConfigurationSection itemsSection = broadcast.getConfigurationSection("Items");

                if(itemsSection != null && !itemsSection.getKeys(false).isEmpty()) {
                    itemsSection.getKeys(false).stream().forEach(itemKey -> {
                        ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemKey);

                        ItemFactory item;

                        try {
                            item = new ItemFactory(Material.matchMaterial(itemSection.getString("Type")), itemSection.getInt("Amount"));

                        } catch(Exception e) {
                            item = new ItemFactory(Material.STONE);
                        }

                        List<String> lore = itemSection.getStringList("Lore");

                        if(lore != null && !lore.isEmpty()) {
                            item.withLore(lore);
                        }

                        List<String> enchants = itemSection.getStringList("Enchantments");

                        if (enchants != null && !enchants.isEmpty()) {
                            final ItemFactory enchantItem = item;

                            enchants.stream().filter(enchantKey -> Enchantment.getByName(enchantKey) != null).forEach(enchantKey -> enchantItem.withEnchantment(Enchantment.getByName(enchantKey)));
                        }

                        items.add(item);

                    });

                }

                broadcasts.add(new BroadCast(messages, items, Optional.ofNullable(broadcast.getString("permission")), sound));

            });
        }

        plugin.getLogger().info(getBroadcasts().size() + " Broadcast(s) loaded successfully!");
    }

    public void runTimer() {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (getBroadcasts().isEmpty()) {
                    this.cancel();
                }

                BroadCast broadcast;

                if (getBroadcasts().size() == 1) {
                    broadcast = getBroadcasts().get(0);

                } else {
                    broadcast = getBroadcasts().get(new Random().nextInt(getBroadcasts().size()));

                }

                broadcast(broadcast);

            }

        }.runTaskTimer(plugin, 1, plugin.getConfig().getInt("Interval"));

    }

    public void broadcast(BroadCast broadcast) {
        if(!broadcast.getPermission().isPresent()) {
            broadcast.getMessages().stream().forEach(msg -> Bukkit.broadcastMessage(Color.c(msg)));

            Bukkit.getOnlinePlayers().stream().forEach(p -> {
                if (broadcast.getSound().isPresent()) {
                    p.playSound(p.getLocation(),broadcast.getSound().get(), 1.0F, 0.0F);
                }

                if (!broadcast.getItems().isEmpty()) {
                    broadcast.getItems().stream().forEach(item -> p.getInventory().addItem(item.build()));
                }

            });

        } else {
            if(!broadcast.getMessages().isEmpty()) {
                broadcast.getMessages().stream().forEach(msg -> Bukkit.broadcast(Color.c(msg), broadcast.getPermission().get()));
            }

            Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission(broadcast.getPermission().get())).forEach(p -> {
                if (broadcast.getSound().isPresent()) {
                    p.playSound(p.getLocation(),broadcast.getSound().get(), 1.0F, 0.0F);
                }

                if (!broadcast.getItems().isEmpty()) {
                    broadcast.getItems().stream().forEach(item -> p.getInventory().addItem(item.build()));
                }

            });
        }

    }

}