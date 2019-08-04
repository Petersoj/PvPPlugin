package net.jacobpeterson.spigot.player.data;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.PvPPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDataUpdateRunnable extends BukkitRunnable {

    private final Logger LOGGER;

    private PvPPlayer pvpPlayer;
    private PlayerDataManager playerDataManager;
    private Runnable completionRunnable;

    /**
     * Instantiates a new PushPlayerDataRunnable which is meant to be run async to push {@link PlayerData} to the
     * SQL Database via {@link PlayerDataManager#updatePlayerDataInDatabase(PvPPlayer)}.
     *
     * @param pvpPlayer          the pvp player
     * @param playerDataManager  the player data manager
     * @param completionRunnable the completion runnable (will get called after all data has been fetched) (can be null)
     */
    public PlayerDataUpdateRunnable(PvPPlayer pvpPlayer, PlayerDataManager playerDataManager, Runnable completionRunnable) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlayer = pvpPlayer;
        this.playerDataManager = playerDataManager;
        this.completionRunnable = completionRunnable;
    }

    @Override
    public void run() {
        try {
            playerDataManager.updatePlayerDataInDatabase(pvpPlayer);
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error pushing player data for: " + pvpPlayer.getPlayer().getName(), exception);
        }

        if (completionRunnable != null) {
            completionRunnable.run();
        }
    }
}
