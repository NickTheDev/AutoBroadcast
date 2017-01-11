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
package net.nikdev.autobroadcast.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

import static net.nikdev.autobroadcast.util.Conditions.orElse;

public final class ItemFactory {

    private final ItemStack item;

    public ItemFactory(Material material) {
        this(material, 1);
    }

    public ItemFactory(Material material, int amount) {
        item = new ItemStack(orElse(material, Material.AIR), amount);
    }

    public ItemFactory name(String name) {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Chat.color(name));
        item.setItemMeta(meta);

        return this;
    }

    public ItemFactory amount(int amount) {
        item.setAmount(amount);

        return this;
    }

    public ItemFactory enchant(Enchantment enchant) {
        item.addUnsafeEnchantment(orElse(enchant, Enchantment.DURABILITY), 1);

        return this;
    }

    public ItemFactory lore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();

        meta.setLore(Chat.color(lore));
        item.setItemMeta(meta);

        return this;
    }

    public ItemStack create() {
        return item.clone();
    }

}
