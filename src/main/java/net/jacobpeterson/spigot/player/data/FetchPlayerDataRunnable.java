package net.jacobpeterson.spigot.player.data;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.PvPPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FetchPlayerDataRunnable extends BukkitRunnable {

    private final Logger LOGGER;

    private PvPPlayer pvpPlayer;
    private PlayerDataManager playerDataManager;
    private Runnable completionRunnable;

    /**
     * Instantiates a new FetchPlayerDataRunnable which is meant to be run async to fetch from the SQL Database
     * and populate {@link PlayerData} via {@link PlayerDataManager#fetchPlayerDataDatabase(PvPPlayer)} (may call
     * {@link PlayerDataManager#insertNewPlayerDataInDatabase(PvPPlayer)} if necessary).
     *
     * @param pvpPlayer          the pvp player
     * @param playerDataManager  the player data manager
     * @param completionRunnable the completion runnable (will get called after all data has been fetched) (can be null)
     */
    public FetchPlayerDataRunnable(PvPPlayer pvpPlayer, PlayerDataManager playerDataManager, Runnable completionRunnable) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlayer = pvpPlayer;
        this.playerDataManager = playerDataManager;
        this.completionRunnable = completionRunnable;
    }

    @Override
    public void run() {
        PlayerData playerData = null;

        try {
            playerData = playerDataManager.fetchPlayerDataDatabase(pvpPlayer);
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error fetching Player Data for: " + pvpPlayer.getPlayer().getName(), exception);
        }

        boolean insertNewPlayer = false;

        if (playerData == null) { // This means the player is new and has no entry in the database
            insertNewPlayer = true; // We now need to insert a new entry into database later
            playerData = new PlayerData(); // Create a fresh PlayerData for the player
        }

        pvpPlayer.setPlayerData(playerData);

        if (insertNewPlayer) {
            try {
                playerDataManager.insertNewPlayerDataInDatabase(pvpPlayer);
            } catch (SQLException exception) {
                LOGGER.log(Level.SEVERE, "Error inserting Player Data for: " + pvpPlayer.getPlayer().getName(), exception);
            }
        }

        if (completionRunnable != null) {
            completionRunnable.run();
        }
    }
}
