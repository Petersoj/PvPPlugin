package net.jacobpeterson.spigot.player.item;

import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.CharUtil;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerItemManager implements Initializers {

    private static ItemStack PLAY_NOW_COMPASS_ITEM;

    private PvPPlayer pvpPlayer;

    /**
     * Instantiates a new PlayerItemStackManager which is used to manage itemstacks for the player on a per-player basis.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerItemManager(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
    }

    @Override
    public void init() {
        if (PLAY_NOW_COMPASS_ITEM == null) {
            PLAY_NOW_COMPASS_ITEM = new ItemStack(Material.COMPASS);
            ItemStackUtil.formatLore(PLAY_NOW_COMPASS_ITEM, true,
                    CharUtil.boldColor(ChatColor.YELLOW) + "Play Now!", (String[]) null);
        }
    }

    @Override
    public void deinit() {
    }

    /**
     * Loads the spawn inventory into the current player's inventory.
     */
    public void loadSpawnInventory() {
        PlayerInventory playerInventory = pvpPlayer.getPlayer().getInventory();
        playerInventory.clear();

        playerInventory.setItem(0, PLAY_NOW_COMPASS_ITEM);
    }

    /**
     * Gets "play now" compass item.
     *
     * @return the "play now" compass item
     */
    public ItemStack getPlayNowCompassItem() {
        return PLAY_NOW_COMPASS_ITEM;
    }

    /**
     * Gets pvp player.
     *
     * @return the pvp player
     */
    public PvPPlayer getPvpPlayer() {
        return pvpPlayer;
    }
}
