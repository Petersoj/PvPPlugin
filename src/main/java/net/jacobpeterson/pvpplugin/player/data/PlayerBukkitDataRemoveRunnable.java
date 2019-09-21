package net.jacobpeterson.pvpplugin.player.data;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerBukkitDataRemoveRunnable extends BukkitRunnable {

    private final Logger LOGGER;

    private PvPPlayer pvpPlayer;
    private PlayerDataManager playerDataManager;

    /**
     * Instantiates a new PlayerBukkitDataRemoveRunnable which will remove the data file generated by bukkit:
     * <world>/playerdata/<UUID>.dat for every world.
     *
     * @param pvpPlayer         the pvp player
     * @param playerDataManager the player data manager
     */
    public PlayerBukkitDataRemoveRunnable(PvPPlayer pvpPlayer, PlayerDataManager playerDataManager) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlayer = pvpPlayer;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public void run() {
        try {
            playerDataManager.deleteBukkitPlayerDataFile(pvpPlayer);
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error delete bukkit player data for: " + pvpPlayer.getPlayer().getName(), exception);
        }
    }
}