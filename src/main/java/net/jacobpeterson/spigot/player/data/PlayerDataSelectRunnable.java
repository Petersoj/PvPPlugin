package net.jacobpeterson.spigot.player.data;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.ExceptionTracker;
import net.jacobpeterson.spigot.util.InstanceRunnable;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.logging.Logger;

public class PlayerDataSelectRunnable extends BukkitRunnable implements ExceptionTracker {

    private final Logger LOGGER;

    private PvPPlayer pvpPlayer;
    private PlayerDataManager playerDataManager;
    private InstanceRunnable<PlayerDataSelectRunnable> instanceRunnable;
    private ArrayList<AbstractMap.SimpleEntry<String, Exception>> exceptions;

    /**
     * Instantiates a new FetchPlayerDataRunnable which is meant to be run async to fetch from the SQL Database
     * and populate {@link PlayerData} via {@link PlayerDataManager#selectPlayerDataFromDatabase(PvPPlayer)} (may call
     * {@link PlayerDataManager#insertNewPlayerDataInDatabase(PvPPlayer)} if necessary).
     *
     * @param pvpPlayer         the pvp player
     * @param playerDataManager the player data manager
     * @param instanceRunnable  the instance runnable (will get called after all data has been fetched) (can be null)
     */
    public PlayerDataSelectRunnable(PvPPlayer pvpPlayer, PlayerDataManager playerDataManager,
                                    InstanceRunnable<PlayerDataSelectRunnable> instanceRunnable) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlayer = pvpPlayer;
        this.playerDataManager = playerDataManager;
        this.instanceRunnable = instanceRunnable;
        this.exceptions = new ArrayList<>();
    }

    @Override
    public void run() {
        boolean insertNewPlayer = false;

        try {
            PlayerData playerData = playerDataManager.selectPlayerDataFromDatabase(pvpPlayer);

            if (playerData == null) { // If no exception occurs and player data is null
                insertNewPlayer = true;
                pvpPlayer.setPlayerData(new PlayerData()); // Create a fresh PlayerData for the player
            }
        } catch (SQLException exception) {
            exceptions.add(new AbstractMap.SimpleEntry<>("Could not select player data for: " +
                    pvpPlayer.getPlayer().getName(), exception));
        }

        if (insertNewPlayer && !didExceptionOccur()) {
            try {
                playerDataManager.insertNewPlayerDataInDatabase(pvpPlayer);
            } catch (SQLException exception) {
                exceptions.add(new AbstractMap.SimpleEntry<>("Could not insert new player data for: " +
                        pvpPlayer.getPlayer().getName(), exception));
            }
        }

        if (instanceRunnable != null) {
            instanceRunnable.run(this);
        }
    }

    /**
     * Gets pvp player.
     *
     * @return the pvp player
     */
    public PvPPlayer getPvPPlayer() {
        return pvpPlayer;
    }

    /**
     * Gets player data manager.
     *
     * @return the player data manager
     */
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    @Override
    public boolean didExceptionOccur() {
        return exceptions.size() > 0;
    }

    @Override
    public ArrayList<AbstractMap.SimpleEntry<String, Exception>> getExceptions() {
        return exceptions;
    }

    /**
     * Used for the instance runnable for {@link PlayerDataSelectRunnable} which will check if an exception occured
     * and kick the player if needs be.
     */
    public static class PlayerDataSelectInstanceRunnable implements InstanceRunnable<PlayerDataSelectRunnable> {
        @Override
        public void run(PlayerDataSelectRunnable playerDataSelectRunnable) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (playerDataSelectRunnable.didExceptionOccur()) {
                        // TODO sync kick player
                    }
                }
            }.runTask(playerDataSelectRunnable.getPlayerDataManager().getPlayerManager().getPvPPlugin());
        }
    }
}
