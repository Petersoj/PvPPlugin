package net.jacobpeterson.pvpplugin.arena.itemstack;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class ArenaItemStack implements Serializable, Cloneable {

    protected transient Arena arena; // Used strictly for reference
    protected transient ItemStack itemStack; // No need to serialize because methods below can create it new
    protected Material material;

    /**
     * Default constructor for Gson.
     */
    public ArenaItemStack() {
    }

    /**
     * Instantiates a new ArenaItemStack which is used as a wrapper for the {@link ItemStack} representing the Arena.
     * Note: this is meant to be instantiated on a per-player basis because of the unique, per-player lores.
     *
     * @param arena    the arena
     * @param material the material
     */
    public ArenaItemStack(Arena arena, Material material) {
        this.arena = arena;
        this.material = material;
    }

    /**
     * Updates the lore for this Arena representation ItemStack.
     *
     * @param currentGame the game instance that is currently being played on this Arena (can be null)
     * @param pvpPlayer   the pvp player who is viewing this ArenaItemStack
     */
    public void updateItemStack(Game currentGame, PvPPlayer pvpPlayer) {
        if (itemStack == null) { // Check if a new ItemStack needs to be created
            this.itemStack = new ItemStack(material, 1);
        }
    }

    /**
     * Gets the standard lore format of an ArenaItemStack.
     *
     * @param pvpPlayer           the pvp player
     * @param currentPlayingLines the current playing lines
     * @return the standard lore format
     */
    public ArrayList<String> getStandardLoreFormat(PvPPlayer pvpPlayer,
                                                   String... currentPlayingLines) {
        ArrayList<String> lore = new ArrayList<>();

        if (!pvpPlayer.isPremium()) {
            lore.add(ChatColor.GOLD + "You need premium to choose this 1v1 arena.");
            lore.add(ChatColor.GOLD + "For premium, visit " + ChatColor.AQUA + "shop.mcsiege.com");
        }

        if (currentPlayingLines != null) {
            lore.addAll(Arrays.asList(currentPlayingLines));
        }

        lore.add(ChatColor.GOLD + "Total times played" + ChatColor.GRAY + ": " +
                ChatColor.AQUA + pvpPlayer.getPlayerData().getArenaTimesPlayedMap().get(arena));

        lore.add(ChatColor.GOLD + "Built by" + ChatColor.GRAY + ": " +
                ChatColor.GREEN + arena.getBuiltByName());

        // Append arena description lines (line separated by \n)
        for (String descriptionLine : arena.getDescription().split("\n")) {
            lore.add(ChatColor.GOLD + descriptionLine);
        }

        return lore;
    }

    /**
     * Clones this ArenaItemStack, preserving instance type.
     *
     * @return the cloned ArenaItemStack
     */
    public abstract ArenaItemStack clone();

    /**
     * Gets arena.
     *
     * @return the arena
     */
    public abstract Arena getArena();

    /**
     * Sets arena.
     *
     * @param arena the arena
     */
    public abstract void setArena(Arena arena);

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

    /**
     * Gets material.
     *
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets material.
     *
     * @param material the material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
}
