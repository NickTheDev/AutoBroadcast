package net.nikdev.autobroadcast.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class ItemFactory {

    private final ItemStack wrappedItemStack;

    public ItemFactory(Material material) {
        wrappedItemStack = new ItemStack(material);
    }

    public ItemFactory(Material material, int amount) {
        wrappedItemStack = new ItemStack(material, amount);
    }

    public ItemFactory withName(String name) {
        ItemMeta meta = wrappedItemStack.getItemMeta();
        meta.setDisplayName(Color.c(name));
        wrappedItemStack.setItemMeta(meta);

        return this;
    }

    public ItemFactory withAmount(int amount) {
        wrappedItemStack.setAmount(amount);

        return this;
    }

    public ItemFactory withEnchantment(Enchantment enchant) {
        wrappedItemStack.addUnsafeEnchantment(enchant, 1);

        return this;
    }

    public ItemFactory withLore(List<String> lore) {
        ItemMeta meta = wrappedItemStack.getItemMeta();
        meta.setLore(Color.c(lore));
        wrappedItemStack.setItemMeta(meta);

        return this;
    }

    public ItemStack build() {
        return wrappedItemStack;
    }

}
