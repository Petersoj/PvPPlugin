package net.jacobpeterson.pvpplugin.player.inventory;

import net.jacobpeterson.pvpplugin.arena.Arena;
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
        if (PLAY_NOW_COMPASS_ITEM == null) {
            PLAY_NOW_COMPASS_ITEM = new ItemStack(Material.COMPASS);
            ItemStackUtil.formatLore(PLAY_NOW_COMPASS_ITEM, true,
                    ChatUtil.boldColor(ChatColor.YELLOW) + "Play Now!", (String[]) null);
        }

        PlayerInventory playerInventory = pvpPlayer.getPlayer().getInventory();
        playerInventory.clear();
        playerInventory.setArmorContents(null);

        playerInventory.setItem(0, PLAY_NOW_COMPASS_ITEM);
    }

    /**
     * Load arena persisted inventory.
     *
     * @param arena the arena
     * @return the if the inventory was loaded
     */
    public boolean loadArenaPersistedInventory(Arena arena) {
        PlayerInventory playerInventory = pvpPlayer.getPlayer().getInventory();
        ItemStack[][] persistedInventory = pvpPlayer.getPlayerData().getArenaInventoryMap().get(arena);
        if (persistedInventory == null) {
            pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Could not load " +
                    "your saved inventory!");
            return false;
        } else if (persistedInventory.length != 2) {
            new ArrayIndexOutOfBoundsException("Saved inventory is in wrong format!").printStackTrace();
            return false;
        }
        playerInventory.setContents(persistedInventory[0]);
        playerInventory.setArmorContents(persistedInventory[1]);
        return true;
    }

    /**
     * Save arena persisted inventory.
     *
     * @param arena the arena
     */
    public void saveArenaPersistedInventory(Arena arena) {
        PlayerInventory playerInventory = pvpPlayer.getPlayer().getInventory();
        ItemStack[] inventoryContents = playerInventory.getContents();
        ItemStack[] inventoryArmorContents = playerInventory.getArmorContents();
        pvpPlayer.getPlayerData().getArenaInventoryMap().put(arena,
                new ItemStack[][]{inventoryContents, inventoryArmorContents});
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
