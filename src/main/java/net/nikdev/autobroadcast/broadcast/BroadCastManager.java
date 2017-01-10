package net.nikdev.autobroadcast.broadcast;

import net.nikdev.autobroadcast.AutoBroadcast;
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
    private final List<Broadcast> broadcasts = new ArrayList<>();

    public BroadCastManager(AutoBroadcast plugin) {
        this.plugin = plugin;

        load();
        createTimer();
    }

    public List<Broadcast> getTimedBroadcasts() {
        return Collections.unmodifiableList(broadcasts);
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

                broadcasts.add(new Broadcast(broadcast.getStringList("Messages"), items, broadcast.getString("permission"), sound));
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

    public void broadcast(Broadcast broadcast) {
        if(notNull(broadcast)) {
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