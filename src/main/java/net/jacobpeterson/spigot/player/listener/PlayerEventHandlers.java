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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

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
     * Handle player spawn location event.
     *
     * @param event the event
     */
    public void handlePlayerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
        // Ensure that the player spawns in the default world at the exact spawn location
        event.setSpawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation().add(0.5, 0, 0.5));
    }

    /**
     * Handle player death event.
     *
     * @param event the event
     */
    public void handlePlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        player.spigot().respawn();
        // TODO should ArenaEventHandlers do this?
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
     * Handle async player chat event.
     *
     * @param event the event
     */
    public void handleAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        // Format for normal Bukkit Minecraft message: <%1$s> %2$s
        // Format for PvPPlugin: &8:&3<elo>&8:<displayname>&8: %2$s

        event.setFormat(ChatColor.DARK_GRAY + ":" + ChatColor.DARK_AQUA + pvpPlayer.getPlayerData().getELO() +
                ChatColor.DARK_GRAY + ":" + pvpPlayer.getPrefixedName() + ChatColor.DARK_GRAY + ":" +
                ChatColor.WHITE + " %2$s");
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

        if (playerItemManager.getPlayNowCompassItem().equals(event.getCurrentItem()) && !player.isOp()) {
            event.setCancelled(true);
        }
    }
}
