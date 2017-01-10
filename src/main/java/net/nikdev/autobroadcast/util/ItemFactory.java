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
