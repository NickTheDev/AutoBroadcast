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

import com.nickthedev.broadcasting.util.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Wrapper for a Bukkit item that provides for easy modification.
 *
 * @author NicktheDev
 * @since 1.0
 */
public final class ItemBuilder {

    private final ItemStack item;

    /**
     * Creates an item wrapper with the material.
     *
     * @param material Material to set.
     */
    ItemBuilder(Material material) {
        item = new ItemStack(material, 1);
    }

    /**
     * Sets the name of this item.
     *
     * @param name Name to set.
     * @return This instance.
     */
    public ItemBuilder name(String name) {
        if(name == null) {
            return this;
        }

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Chat.color(name));
        item.setItemMeta(meta);

        return this;
    }

    /**
     * Sets the amount of this item.
     *
     * @param amount Amount to set.
     * @return This instance.
     */
    public ItemBuilder amount(int amount) {
        if(amount < 1) {
            return this;
        }

        item.setAmount(amount);

        return this;
    }

    /**
     * Adds lore to the item.
     *
     * @param lore Lore to add.
     * @return This instance.
     */
    public ItemBuilder lore(List<String> lore) {
        if(lore == null || lore.isEmpty()) {
            return this;
        }

        ItemMeta meta = item.getItemMeta();

        meta.setLore(Chat.color(lore));
        item.setItemMeta(meta);

        return this;
    }

    /**
     * Gets the Bukkit version of this item.
     *
     * @return Copy of the item stack.
     */
    public ItemStack build() {
        return item.clone();
    }

}
