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

public class PlayerDataUpdateRunnable extends BukkitRunnable implements ExceptionTracker {

    private final Logger LOGGER;

    private PvPPlayer pvpPlayer;
    private PlayerDataManager playerDataManager;
    private InstanceRunnable<PlayerDataUpdateRunnable> instanceRunnable;
    private ArrayList<AbstractMap.SimpleEntry<String, Exception>> exceptions;

    /**
     * Instantiates a new PushPlayerDataRunnable which is meant to be run async to push {@link PlayerData} to the
     * SQL Database via {@link PlayerDataManager#updatePlayerDataInDatabase(PvPPlayer)}.
     *
     * @param pvpPlayer         the pvp player
     * @param playerDataManager the player data manager
     * @param instanceRunnable  the instance runnable (will get called after all data has been fetched) (can be null)
     */
    public PlayerDataUpdateRunnable(PvPPlayer pvpPlayer, PlayerDataManager playerDataManager,
                                    InstanceRunnable<PlayerDataUpdateRunnable> instanceRunnable) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlayer = pvpPlayer;
        this.playerDataManager = playerDataManager;
        this.instanceRunnable = instanceRunnable;
        this.exceptions = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            playerDataManager.updatePlayerDataInDatabase(pvpPlayer);
        } catch (SQLException exception) {
            exceptions.add(new AbstractMap.SimpleEntry<>("Error pushing player data for: "
                    + pvpPlayer.getPlayer().getName(), exception));
        }

        if (instanceRunnable != null) {
            instanceRunnable.run(this);
        }
    }

    @Override
    public boolean didExceptionOccur() {
        return exceptions.size() > 0;
    }

    @Override
    public ArrayList<AbstractMap.SimpleEntry<String, Exception>> getExceptions() {
        return exceptions;
    }
}
