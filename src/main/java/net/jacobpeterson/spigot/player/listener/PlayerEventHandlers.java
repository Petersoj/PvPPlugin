package net.jacobpeterson.spigot.player.listener;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.data.PlayerData;
import net.jacobpeterson.spigot.player.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.logging.Level;
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

        // TODO Async all this
        PlayerData playerData = null;
        try {
            playerData = playerDataManager.fetchPlayerDataDatabase(pvpPlayer);
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error fetching Player Data for: " + player.getName(), exception);
        }
        if (playerData == null) { // This means the player is new and has no entry in the database
            playerData = new PlayerData(); // Create a fresh PlayerData for the player
        }
        pvpPlayer.setPlayerData(playerData);
    }

    /**
     * Handle on player quit event (save player data, clean up, etc.).
     *
     * @param event the event
     */
    public void handleOnPlayerQuitEvent(PlayerQuitEvent event) {

    }
}
