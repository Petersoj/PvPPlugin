package net.jacobpeterson.pvpplugin.player.inventory;

import net.jacobpeterson.pvpplugin.itemstack.ItemStackUtil;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryManager implements Initializers {

    private static ItemStack PLAY_NOW_COMPASS_ITEM;

    private PvPPlayer pvpPlayer;

    /**
     * Instantiates a new PlayerItemStackManager which is used to manage itemstacks for the player on a per-player basis.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerInventoryManager(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
    }

    @Override
    public void init() {
        if (PLAY_NOW_COMPASS_ITEM == null) {
            PLAY_NOW_COMPASS_ITEM = new ItemStack(Material.COMPASS);
            ItemStackUtil.formatLore(PLAY_NOW_COMPASS_ITEM, true,
                    ChatUtil.boldColor(ChatColor.YELLOW) + "Play Now!", (String[]) null);
        }
    }

    @Override
    public void deinit() {
    }

    /**
     * Whether or not the player can manipulate their inventory in lobbies and such (aka if the player has the
     * 'game.inventory' permission which can be granted via PermissionsEx in a given group).
     *
     * @return whether or not the player can manipulate their inventory
     */
    public boolean canManipulateInventory() {
        return pvpPlayer.getPlayer().hasPermission("game.inventory");
    }

    /**
     * Loads the spawn inventory into the current player's inventory.
     */
    public void loadSpawnInventory() {
        PlayerInventory playerInventory = pvpPlayer.getPlayer().getInventory();
        playerInventory.clear();
        playerInventory.setArmorContents(null);

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
    public PvPPlayer getPvPPlayer() {
        return pvpPlayer;
    }
}
