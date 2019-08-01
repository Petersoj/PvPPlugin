package net.jacobpeterson.spigot.arena.itemstack;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ArenaItemStack {

    public transient static final String TIMES_PLAYED_PLACEHOLDER = "<times_played>";
    private ItemStack itemStack;
    private int timesPlayedLineIndex;
    private int timesPlayedFromCharIndex;
    private int timesPlayedToCharIndex;

    /**
     * Instantiates a new ArenaItemStack which is used as a wrapper for the {@link ItemStack} representing the Arena.
     * Note: this is meant to be instantiated on a per-player basis because of the unique, per-player lores.
     *
     * @param itemStack the item stack
     */
    public ArenaItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Update times played in itemstack lore.
     *
     * @param timesPlayed the times played
     */
    public void updateTimesPlayed(int timesPlayed) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();

        String loreLine = lore.get(timesPlayedLineIndex);
        String newLine = loreLine.substring(0, timesPlayedFromCharIndex) + timesPlayed
                + loreLine.substring(timesPlayedToCharIndex);

        this.timesPlayedToCharIndex = String.valueOf(timesPlayed).length(); // Update the 'to' replace position

        lore.set(timesPlayedLineIndex, newLine);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Gets position of {@link ArenaItemStack#TIMES_PLAYED_PLACEHOLDER} in the lore (line index and char index).
     *
     * @return whether or not the ItemStack lore lines contained {@link ArenaItemStack#TIMES_PLAYED_PLACEHOLDER}.
     */
    public boolean setIndexOfTimesPlayedPlaceholder() {
        List<String> lore = itemStack.getItemMeta().getLore();
        for (int lineIndex = 0; lineIndex < lore.size(); lineIndex++) {
            int indexOfPlaceholder = lore.get(lineIndex).indexOf(TIMES_PLAYED_PLACEHOLDER);
            if (indexOfPlaceholder == -1) {
                this.timesPlayedLineIndex = lineIndex;
                this.timesPlayedFromCharIndex = indexOfPlaceholder;
                this.timesPlayedToCharIndex = timesPlayedFromCharIndex + TIMES_PLAYED_PLACEHOLDER.length();
                return true;
            }
        }
        return false;
    }

    /**
     * Gets item stack.
     *
     * @return the item stack
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Sets item stack.
     *
     * @param itemStack the item stack
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
