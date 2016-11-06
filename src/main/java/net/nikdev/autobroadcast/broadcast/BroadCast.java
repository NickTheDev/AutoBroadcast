package net.nikdev.autobroadcast.broadcast;

import net.nikdev.autobroadcast.util.ItemFactory;
import org.bukkit.Sound;

import java.util.List;
import java.util.Optional;

public class BroadCast {

    private List<String> messages;
    private List<ItemFactory> items;

    private Optional<String> permission;
    private Optional<Sound> sound;

    public BroadCast(List<String> messages, List<ItemFactory> items, Optional<String> permission, Optional<Sound> sound) {
        this.messages = messages;
        this.items = items;
        this.permission = permission;
        this.sound = sound;

    }

    public List<String> getMessages() {
        return messages;
    }

    public Optional<String> getPermission() {
        return permission;
    }

    public Optional<Sound> getSound() {
        return sound;
    }

    public List<ItemFactory> getItems() {
        return items;
    }

}
