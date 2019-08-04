package net.jacobpeterson.spigot.player.listener;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.data.PlayerDataSelectRunnable;
import net.jacobpeterson.spigot.player.data.PlayerDataManager;
import net.jacobpeterson.spigot.player.data.PlayerDataUpdateRunnable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Logger;

public class PlayerEventHandlers {

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

    /**
     * Handle on player join event (create PvPPlayer, fetch player data, etc.).
     *
     * @param event the event
     */
    public void handleOnPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerDataManager playerDataManager = playerManager.getPlayerDataManager();

        PvPPlayer pvpPlayer = playerManager.createNewPvPPlayer(player);

        PlayerDataSelectRunnable playerDataSelectRunnable = new PlayerDataSelectRunnable(pvpPlayer, playerDataManager, null);
        playerDataSelectRunnable.runTaskAsynchronously(playerManager.getPvPPlugin());
    }

    /**
     * Handle on player quit event (save player data, deinit, etc.).
     *
     * @param event the event
     */
    public void handleOnPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);
        PlayerDataManager playerDataManager = playerManager.getPlayerDataManager();

        if (pvpPlayer == null) {
            return;
        }

        pvpPlayer.deinit();

        PlayerDataUpdateRunnable playerDataUpdateRunnable = new PlayerDataUpdateRunnable(pvpPlayer, playerDataManager, null);
        playerDataUpdateRunnable.runTaskAsynchronously(playerManager.getPvPPlugin());
    }
}
