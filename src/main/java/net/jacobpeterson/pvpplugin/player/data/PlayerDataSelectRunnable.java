package net.jacobpeterson.pvpplugin.player.data;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDataSelectRunnable extends BukkitRunnable {

    private final Logger LOGGER;

    private PvPPlayer pvpPlayer;
    private PlayerDataManager playerDataManager;

    /**
     * Instantiates a new FetchPlayerDataRunnable which is meant to be run async to fetch from the SQL Database
     * and populate {@link PlayerData} via {@link PlayerDataManager#selectPlayerDataFromDatabase(UUID)} (may call
     * {@link PlayerDataManager#insertNewPlayerDataInDatabase(PvPPlayer)} if necessary).
     *
     * @param pvpPlayer         the pvp player
     * @param playerDataManager the player data manager
     */
    public PlayerDataSelectRunnable(PvPPlayer pvpPlayer, PlayerDataManager playerDataManager) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlayer = pvpPlayer;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public void run() {
        boolean insertNewPlayer = false;

        try {
            PlayerData playerData = playerDataManager.selectPlayerDataFromDatabase(pvpPlayer.getPlayer().getUniqueId());

            if (playerData == null) { // If no exception occurs and player data is null
                insertNewPlayer = true;
                pvpPlayer.setPlayerData(new PlayerData()); // Create a fresh PlayerData for the player
            } else {
                pvpPlayer.setPlayerData(playerData);
            }
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Could not select player data for: " + pvpPlayer.getPlayer().getName(), exception);
            playerDataManager.getPlayerManager().kickPlayerSync(pvpPlayer,
                    ChatColor.RED + "Could not get your data from database!\n" +
                            ChatColor.WHITE + "Contact the server admins!");
        }

        if (insertNewPlayer) {
            try {
                playerDataManager.insertNewPlayerDataInDatabase(pvpPlayer);
            } catch (SQLException exception) {
                LOGGER.log(Level.SEVERE, "Could not insert new player data for: " +
                        pvpPlayer.getPlayer().getName(), exception);
                playerDataManager.getPlayerManager().kickPlayerSync(pvpPlayer,
                        ChatColor.RED + "Could not insert your data in database!\n" +
                                ChatColor.WHITE + "Contact the server admins!");
            }
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
}
