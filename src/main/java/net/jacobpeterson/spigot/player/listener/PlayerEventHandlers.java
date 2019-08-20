package net.jacobpeterson.spigot.player.listener;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.data.PlayerBukkitDataRemoveRunnable;
import net.jacobpeterson.spigot.player.data.PlayerDataManager;
import net.jacobpeterson.spigot.player.data.PlayerDataSelectRunnable;
import net.jacobpeterson.spigot.player.data.PlayerDataUpdateRunnable;
import net.jacobpeterson.spigot.player.item.PlayerItemManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Logger;

public class PlayerEventHandlers implements Initializers {

    private final Logger LOGGER;
    private PlayerManager playerManager;

    /**
     * Instantiates a new PlayerEventHandlers for handling Bukkit events.
     *
     * @param playerManager the player manager
     */
    public PlayerEventHandlers(PlayerManager playerManager) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.playerManager = playerManager;
    }

    @Override
    public void init() {
    }

    /**
     * {@inheritDoc}
     * This will call {@link #handlePlayerQuitEvent(PlayerQuitEvent)} for all PvPPlayers.
     */
    @Override
    public void deinit() {
        for (PvPPlayer pvpPlayer : playerManager.getPvPPlayers()) {
            this.handlePlayerQuitEvent(new PlayerQuitEvent(pvpPlayer.getPlayer(), "Deinitializing"));
        }
    }

    /**
     * Handle player join event (create PvPPlayer, fetch player data, etc.).
     *
     * @param event the event
     */
    public void handlePlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerDataManager playerDataManager = playerManager.getPlayerDataManager();

        PvPPlayer pvpPlayer = playerManager.createNewPvPPlayer(player);
        pvpPlayer.getPlayerItemManager().loadSpawnInventory();

        PlayerDataSelectRunnable playerDataSelectRunnable = new PlayerDataSelectRunnable(pvpPlayer, playerDataManager);
        playerDataSelectRunnable.runTaskAsynchronously(playerManager.getPvPPlugin());
    }

    /**
     * Handle player quit event (save player data, deinit, etc.).
     *
     * @param event the event
     */
    public void handlePlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);
        PlayerDataManager playerDataManager = playerManager.getPlayerDataManager();

        if (pvpPlayer == null) {
            return;
        }

        pvpPlayer.deinit();
        playerManager.getPvPPlayers().remove(pvpPlayer);

        PlayerDataUpdateRunnable playerDataUpdateRunnable = new PlayerDataUpdateRunnable(pvpPlayer, playerDataManager);
        playerDataUpdateRunnable.runTaskAsynchronously(playerManager.getPvPPlugin());

        PlayerBukkitDataRemoveRunnable playerBukkitDataRemoveRunnable =
                new PlayerBukkitDataRemoveRunnable(pvpPlayer, playerDataManager);
        // Run 20 ticks later (1 second) to ensure deletion
        playerBukkitDataRemoveRunnable.runTaskLaterAsynchronously(playerManager.getPvPPlugin(), 20);
    }

    /**
     * Handle player interact event.
     *
     * @param event the event
     */
    public void handlePlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        // If action is not right click air or right click block
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        PlayerItemManager playerItemManager = pvpPlayer.getPlayerItemManager();

        if (playerItemManager.getPlayNowCompassItem().equals(event.getItem())) {
            pvpPlayer.getPlayer().openInventory(playerManager.getPvPPlugin().getGUIManager().getMainMenu().getInventory());
        }
    }

    /**
     * Handle player drop item event.
     *
     * @param event the event
     */
    public void handlePlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        PlayerItemManager playerItemManager = pvpPlayer.getPlayerItemManager();

        if (playerItemManager.getPlayNowCompassItem().equals(event.getItemDrop().getItemStack()) && !player.isOp()) {
            event.setCancelled(true);
        }
    }

    /**
     * Handle inventory click event.
     *
     * @param event the event
     */
    public void handleInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) { // If who clicked is not a player
            return;
        }

        Player player = (Player) event.getWhoClicked();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        PlayerItemManager playerItemManager = pvpPlayer.getPlayerItemManager();

        if (playerItemManager.getPlayNowCompassItem().equals(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }
}
