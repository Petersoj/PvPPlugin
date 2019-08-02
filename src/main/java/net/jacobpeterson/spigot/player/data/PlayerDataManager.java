package net.jacobpeterson.spigot.player.data;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.data.DatabaseManager;
import net.jacobpeterson.spigot.data.GsonManager;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Logger;

public class PlayerDataManager implements Initializers {

    private final Logger LOGGER;

    private final PlayerManager playerManager;
    private final GsonManager gsonManager;
    private final DatabaseManager databaseManager;
    private String databaseTableName;
    private String[] databaseColumns; // For reference

    /**
     * Instantiates a new Player Data Manager which is used to read/write {@link PlayerData} via {@link DatabaseManager}.
     *
     * @param playerManager   the player manager
     * @param gsonManager     the gson manager
     * @param databaseManager the database manager
     */
    public PlayerDataManager(PlayerManager playerManager, GsonManager gsonManager, DatabaseManager databaseManager) {
        this.LOGGER = PvPPlugin.getPluginLogger();
        this.playerManager = playerManager;
        this.gsonManager = gsonManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public void init() throws SQLException {
        this.databaseTableName = "player_data";

        this.databaseColumns = new String[]{
                "uuid",
                "elo",
                "arena_times_played",
                "unranked_ffa_kills",
                "unranked_ffa_deaths",
                "ranked_1v1_kills",
                "ranked_1v1_deaths",
                "team_pvp_wins",
                "team_pvp_losses"
        };

        if (!this.doesPlayerDataTableExists()) { // Check if PlayerData table exists
            this.createPlayerDataTable(); // Create the table if it doesn't exist
            if (!this.doesPlayerDataTableExists()) { // Check again after creating table
                throw new SQLException("Cannot create " + databaseTableName + " table!");
            }
        }
        LOGGER.info("Found " + databaseTableName + " table in database");
    }

    @Override
    public void deinit() {

    }

    /**
     * Fetch PlayerData from SQL database.
     *
     * @param pvpPlayer the pvp player
     * @return the player data (null if no data exists)
     * @throws SQLException the sql exception
     */
    @SuppressWarnings("unchecked") // Easier to ignore cast checking and let the runtime throw the exception if so
    public synchronized PlayerData fetchPlayerDataDatabase(PvPPlayer pvpPlayer) throws SQLException {
        PlayerData playerData = new PlayerData();

        String selectPlayerDataSQL = "SELECT * from '" + databaseTableName + "' WHERE uuid='"
                + pvpPlayer.getPlayer().getUniqueId().toString() + "';";

        Statement selectPlayerDataStatement = databaseManager.getMysqlConnection().createStatement();
        ResultSet resultSet = selectPlayerDataStatement.executeQuery(selectPlayerDataSQL);

        if (resultSet.getRow() == 0) { // 0th row means no data returned
            return null;
        }

        // Arenas times played parsing from DB logic
        gsonManager.getArenaSerializer().setReferenceDeserialization(true); // Turn on referencing to already-created Arenas
        HashMap arenaTimesPlayedMap = gsonManager.getGson().fromJson(resultSet.getString(3),
                playerData.getArenaTimesPlayedMap().getClass());
        playerData.setArenaTimesPlayedMap(arenaTimesPlayedMap);

        // Set other primitives
        playerData.setELO(resultSet.getInt(2));
        playerData.setUnrankedFFAKills(resultSet.getInt(4));
        playerData.setUnrankedFFADeaths(resultSet.getInt(5));
        playerData.setRanked1v1Kills(resultSet.getInt(6));
        playerData.setRanked1v1Deaths(resultSet.getInt(7));
        playerData.setTeamPvPWins(resultSet.getInt(8));
        playerData.setTeamPvPLosses(resultSet.getInt(9));

        return playerData;
    }

    /**
     * Pushes PlayerData to SQL database.
     *
     * @param pvpPlayer the pvp player
     * @throws SQLException the sql exception
     */
    public synchronized void updatePlayerDataDatabase(PvPPlayer pvpPlayer) throws SQLException {
        PlayerData playerData = pvpPlayer.getPlayerData();

        String updatePlayerDataPreparedSQL = "UPDATE ? SET " +
                "elo=? " +
                "arena_times_played=? " +
                "unranked_ffa_kills=? " +
                "unranked_ffa_deaths=? " +
                "ranked_1v1_kills=? " +
                "ranked_1v1_deaths=? " +
                "team_pvp_wins=? " +
                "team_pvp_losses=? " +
                "WHERE uuid=?";

        PreparedStatement updatePreparedStatement = databaseManager.getMysqlConnection()
                .prepareStatement(updatePlayerDataPreparedSQL);

        // Table name
        updatePreparedStatement.setString(1, databaseTableName);

        // UUID
        updatePreparedStatement.setString(10, pvpPlayer.getPlayer().getUniqueId().toString());

        // Arena times played parsing logic
        gsonManager.getArenaSerializer().setReferenceSerialization(true); // Turn on referencing to already-created Arenas
        String arenaTimesPlayedMapJson = gsonManager.getGson().toJson(playerData.getArenaTimesPlayedMap(),
                playerData.getArenaTimesPlayedMap().getClass());
        updatePreparedStatement.setString(3, arenaTimesPlayedMapJson);

        // Other primitives
        updatePreparedStatement.setInt(2, playerData.getELO());
        updatePreparedStatement.setInt(4, playerData.getUnrankedFFAKills());
        updatePreparedStatement.setInt(5, playerData.getUnrankedFFADeaths());
        updatePreparedStatement.setInt(6, playerData.getRanked1v1Kills());
        updatePreparedStatement.setInt(7, playerData.getRanked1v1Deaths());
        updatePreparedStatement.setInt(8, playerData.getTeamPvPWins());
        updatePreparedStatement.setInt(9, playerData.getTeamPvPLosses());

        updatePreparedStatement.executeUpdate();
    }

    /**
     * Inserts a new PlayerData in the SQL database.
     *
     * @param pvpPlayer the pvp player
     * @throws SQLException the sql exception
     */
    public synchronized void insertNewPlayerDataInDatabase(PvPPlayer pvpPlayer) throws SQLException {
        PlayerData playerData = pvpPlayer.getPlayerData();

        String insertPlayerDataPreparedSQL = "INSERT INTO ? VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertPreparedStatement = databaseManager.getMysqlConnection()
                .prepareStatement(insertPlayerDataPreparedSQL);

        // Table name
        insertPreparedStatement.setString(1, databaseTableName);

        // Arena times played parsing logic
        gsonManager.getArenaSerializer().setReferenceSerialization(true); // Turn on referencing to already-created Arenas
        String arenaTimesPlayedMapJson = gsonManager.getGson().toJson(playerData.getArenaTimesPlayedMap(),
                playerData.getArenaTimesPlayedMap().getClass());
        insertPreparedStatement.setString(4, arenaTimesPlayedMapJson);

        // Other primitives
        insertPreparedStatement.setString(2, pvpPlayer.getPlayer().getUniqueId().toString());
        insertPreparedStatement.setInt(3, playerData.getELO());
        insertPreparedStatement.setInt(5, playerData.getUnrankedFFAKills());
        insertPreparedStatement.setInt(6, playerData.getUnrankedFFADeaths());
        insertPreparedStatement.setInt(7, playerData.getRanked1v1Kills());
        insertPreparedStatement.setInt(8, playerData.getRanked1v1Deaths());
        insertPreparedStatement.setInt(9, playerData.getTeamPvPWins());
        insertPreparedStatement.setInt(10, playerData.getTeamPvPLosses());

        // e.g. INSERT INTO player_data VALUES("<UUID>", 1000, "{}", 0, 0, 0, 0, 0, 0);
    }

    /**
     * Create player data table.
     *
     * @throws SQLException the sql exception
     */
    public synchronized void createPlayerDataTable() throws SQLException {
        // Table modeled after PlayerData object
        String createTableSQL = "CREATE TABLE " + databaseTableName + " (\n" +
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
                ");";

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
        String tableExistsSQL = "SHOW TABLES LIKE '" + databaseTableName + "';";

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
