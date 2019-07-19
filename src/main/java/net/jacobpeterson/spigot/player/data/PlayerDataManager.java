package net.jacobpeterson.spigot.player.data;

import net.jacobpeterson.spigot.data.DatabaseManager;
import net.jacobpeterson.spigot.data.GsonManager;
import net.jacobpeterson.spigot.player.PlayerManager;

public class PlayerDataManager {

    private PlayerManager playerManager;
    private GsonManager gsonManager;
    private DatabaseManager databaseManager;

    /**
     * Instantiates a new Player Data Manager which is used to read/write {@link PlayerData} via {@link DatabaseManager}.
     *
     * @param playerManager   the player manager
     * @param gsonManager     the gson manager
     * @param databaseManager the database manager
     */
    public PlayerDataManager(PlayerManager playerManager, GsonManager gsonManager, DatabaseManager databaseManager) {
        this.playerManager = playerManager;
        this.gsonManager = gsonManager;
        this.databaseManager = databaseManager;
    }
}
