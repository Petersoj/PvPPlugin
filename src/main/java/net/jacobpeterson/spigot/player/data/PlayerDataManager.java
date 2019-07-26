package net.jacobpeterson.spigot.player.data;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.data.DatabaseManager;
import net.jacobpeterson.spigot.data.GsonManager;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class PlayerDataManager implements Initializers {

    private static final Logger LOGGER = PvPPlugin.LOGGER;

    private final PlayerManager playerManager;
    private final GsonManager gsonManager;
    private final DatabaseManager databaseManager;
    private String databaseTableName;
    private String[] databaseTablecolumns;

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

    @Override
    public void init() throws SQLException {
        this.databaseTableName = "player_data";

        if (!this.doesPlayerDataTableExists()) { // Check if PlayerData table exists
            this.createPlayerDataTable(); // Create the table if it doesn't exist
            if (!this.doesPlayerDataTableExists()) { // Check again after creating table
                throw new SQLException("Cannot create " + databaseTableName + " table!");
            }
        }
    }

    @Override
    public void deinit() {

    }

    public synchronized PlayerData fetchDatabasePlayerData(PvPPlayer pvpPlayer) {

        // Use Gson for PlayerData#arenaTimesPlayed()
        return null;
    }

    public synchronized void pushDatabasePlayerData(PvPPlayer pvpPlayer) {
        // Use Gson for PlayerData#arenaTimesPlayed()
    }

    /**
     * Create player data table.
     *
     * @throws SQLException the sql exception
     */
    public synchronized void createPlayerDataTable() throws SQLException {
        // Table modeled after PlayerData object
        String createTableSQL = "CREATE TABLE " + databaseTableName + " (\n" + // create table where ? = databaseTableName
                "    uuid CHAR(36) NOT NULL,\n" +
                "    elo INT NOT NULL,\n" +
                "    arena_times_played TEXT NOT NULL,\n" +
                "    unranked_ffa_kills INT NOT NULL,\n" +
                "    unranked_ffa_deaths INT NOT NULL,\n" +
                "    ranked_1v1_kills INT NOT NULL,\n" +
                "    ranked_1v1_deaths INT NOT NULL,\n" +
                "    team_pvp_wins INT NOT NULL,\n" +
                "    team_pvp_losses INT NOT NULL,\n" +
                "    PRIMARY KEY (UUID)\n" +
                ")";

        Statement createTableStatement = databaseManager.getMysqlConnection().createStatement();
        createTableStatement.execute(createTableSQL);

        LOGGER.info("Created " + databaseTableName + " with the following SQL: \n" + createTableSQL);
    }

    /**
     * Checks if the {@link PlayerDataManager#getDatabaseTableName()} table exists in the database.
     *
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public synchronized boolean doesPlayerDataTableExists() throws SQLException {
        String tableExistsSQL = "SHOW TABLES LIKE '" + databaseTableName + "'";

        Statement tableExistsStatement = databaseManager.getMysqlConnection().createStatement();

        ResultSet resultSet = tableExistsStatement.executeQuery(tableExistsSQL);
        while (resultSet.next()) {
            if (resultSet.getString(1).equals(databaseTableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets database table name.
     *
     * @return the database table name
     */
    public String getDatabaseTableName() {
        return databaseTableName;
    }
}
