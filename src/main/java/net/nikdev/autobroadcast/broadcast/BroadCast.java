package net.nikdev.autobroadcast.broadcast;

import net.nikdev.autobroadcast.util.ItemFactory;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.nikdev.autobroadcast.util.Conditions.isPresentCollection;

public class Broadcast {

    private final List<String> messages = new ArrayList<>();
    private final List<ItemFactory> items = new ArrayList<>();

    private final String permission;
    private final Sound sound;

    public Broadcast(List<String> messages, List<ItemFactory> items, String permission, Sound sound) {
        this.permission = permission;
        this.sound = sound;

        if(isPresentCollection(messages)) {
            getMessages().addAll(messages);
        }

        if(isPresentCollection(items)) {
            getItems().addAll(items);
        }
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<ItemFactory> getItems() {
        return items;
    }

    public Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    public Optional<Sound> getSound() {
        return Optional.ofNullable(sound);
    }

}
