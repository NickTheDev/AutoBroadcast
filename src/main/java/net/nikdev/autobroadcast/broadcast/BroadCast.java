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

import net.nikdev.autobroadcast.util.ItemFactory;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.nikdev.autobroadcast.util.Conditions.isPresentCollection;

public final class BroadCast {

    private final List<String> messages = new ArrayList<>();
    private final List<ItemFactory> items = new ArrayList<>();

    private final String permission;
    private final Sound sound;

    public BroadCast(List<String> messages, List<ItemFactory> items, String permission, Sound sound) {
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
