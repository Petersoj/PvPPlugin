package net.jacobpeterson.pvpplugin.itemstack;

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
     * @param lore      the lore string array (can be null)
     */
    public static void formatLore(ItemStack itemStack, boolean showItem, String name, String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (showItem) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.spigot().setUnbreakable(true);
        }
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore == null ? null : Arrays.asList(lore));
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

    /**
     * Insert a string at a specific line and character index.
     *
     * @param itemStack   the item stack
     * @param lineIndex   the line index
     * @param atCharIndex the at char index
     * @param substring   the substring
     */
    public static void insertLoreLineSubstring(ItemStack itemStack, int lineIndex, int atCharIndex, String substring) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();

        String loreLine = lore.get(lineIndex);
        String newLine = loreLine.substring(0, atCharIndex) + substring;
        if (newLine.length() < loreLine.length()) {
            newLine += loreLine.substring(substring.length());
        }

        lore.set(lineIndex, newLine);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}
