package net.jacobpeterson.pvpplugin.arena.itemstack;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.itemstack.ItemStackUtil;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ArenaItemStack {

    private transient Arena arena; // Used strictly for reference
    private transient ItemStack itemStack; // No need to serialize because methods below can create it new
    private Material material;

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
     * Updates the ItemStack lore.
     *
     * @param game      the game instance associated with this
     * @param pvpPlayer the pvp player who is viewing this ArenaItemStack
     */
    public void updateItemStack(Game game, PvPPlayer pvpPlayer) {
        if (itemStack == null) {
            this.itemStack = new ItemStack(material, 1);
        }

        ArrayList<String> lore = new ArrayList<>();
        if (!pvpPlayer.isPremium()) {
            lore.add(ChatColor.GOLD + "You need premium to choose this 1v1 arena.");
            lore.add(ChatColor.GOLD + "For premium, visit " + ChatColor.AQUA + "shop.mcsiege.com");
        }
        lore.add(ChatColor.GOLD + "Currently Playing" + ChatColor.GRAY + ": " +
                ChatColor.AQUA + game.getPvPPlayers().size());
        lore.add(ChatColor.GOLD + "Total times played" + ChatColor.GRAY + ": " +
                ChatColor.AQUA + pvpPlayer.getPlayerData().getArenaTimesPlayedMap().get(arena));
        lore.add(ChatColor.GOLD + "Built by" + ChatColor.GRAY + ": " +
                ChatColor.GREEN + arena.getBuiltByName());
        for (String descriptionLine : arena.getDescription().split("\n")) {
            lore.add(ChatColor.GOLD + descriptionLine);
        }

        ItemStackUtil.formatLore(itemStack, true, ChatUtil.boldColor(ChatColor.YELLOW) + arena.getName(),
                (String[]) lore.toArray());
    }

    /**
     * Gets arena.
     *
     * @return the arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Sets arena.
     *
     * @param arena the arena
     */
    public void setArena(Arena arena) {
        this.arena = arena;
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
