package net.jacobpeterson.spigot.itemstack;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemStackUtil {

    /**
     * Set ItemStack lore.
     *
     * @param itemStack the item stack
     * @param showItem  is a show item (apply durability)
     * @param name      the name
     * @param lore      the lore string array
     */
    public static void formatLore(ItemStack itemStack, boolean showItem, String name, String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (showItem) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.spigot().setUnbreakable(true);
        }
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Sets a lore line.
     *
     * @param itemStack the item stack
     * @param lineIndex the line index
     * @param loreLine  the lore line
     */
    public static void setLoreLine(ItemStack itemStack, int lineIndex, String loreLine) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.set(lineIndex, loreLine);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}
