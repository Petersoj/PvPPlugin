package net.jacobpeterson.pvpplugin.player.data;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDataUpdateRunnable extends BukkitRunnable {

    private final Logger LOGGER;

    private PvPPlayer pvpPlayer;
    private PlayerDataManager playerDataManager;

    /**
     * Instantiates a new PushPlayerDataRunnable which is meant to be run async to push {@link PlayerData} to the
     * SQL Database via {@link PlayerDataManager#updatePlayerDataInDatabase(PvPPlayer)}.
     *
     * @param pvpPlayer         the pvp player
     * @param playerDataManager the player data manager
     */
    public PlayerDataUpdateRunnable(PvPPlayer pvpPlayer, PlayerDataManager playerDataManager) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlayer = pvpPlayer;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public void run() {
        try {
            // Update the Player Data in accordance with current Arenas
            playerDataManager.updatePlayerDataArenas(pvpPlayer.getPlayerData());

            // Update the PlayerData in the database
            playerDataManager.updatePlayerDataInDatabase(pvpPlayer);
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error pushing player data for: " + pvpPlayer.getPlayer().getName(), exception);
        }
    }
}
