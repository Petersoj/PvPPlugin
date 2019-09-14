package net.jacobpeterson.spigot.player.data;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.data.DatabaseManager;
import net.jacobpeterson.spigot.data.GsonManager;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDataManager implements Initializers {

    private final Logger LOGGER;

    private final PlayerManager playerManager;
    private final GsonManager gsonManager;
    private final DatabaseManager databaseManager;
    private String databaseTableName;
    private LinkedHashMap<String, String> databaseColumnsMap;

    /**
     * Instantiates a new Player Data Manager which is used to read/write {@link PlayerData} via
     * {@link DatabaseManager}.
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

        // Create map of table columns and their associated SQL characteristics
        databaseColumnsMap = new LinkedHashMap<>();
        databaseColumnsMap.put("uuid", "CHAR(36) NOT NULL");
        databaseColumnsMap.put("name", "VARCHAR(100) NOT NULL");

        databaseColumnsMap.put("elo", "INT NOT NULL");

        databaseColumnsMap.put("arena_times_played", "TEXT NOT NULL");
        databaseColumnsMap.put("arena_inventory", "TEXT NOT NULL");

        databaseColumnsMap.put("unranked_ffa_kills", "INT NOT NULL");
        databaseColumnsMap.put("unranked_ffa_deaths", "INT NOT NULL");

        databaseColumnsMap.put("ranked_1v1_kills", "INT NOT NULL");
        databaseColumnsMap.put("ranked_1v1_deaths", "INT NOT NULL");

        databaseColumnsMap.put("ranked_1v1_wins", "INT NOT NULL");
        databaseColumnsMap.put("ranked_1v1_losses", "INT NOT NULL");

        databaseColumnsMap.put("team_pvp_wins", "INT NOT NULL");
        databaseColumnsMap.put("team_pvp_losses", "INT NOT NULL");

        if (!this.doesPlayerDataTableExist()) { // If PlayerData table does NOT exists

            this.createPlayerDataTable(); // Create the table
            LOGGER.info("Created " + databaseTableName + " database table");

            if (!this.doesPlayerDataTableExist()) { // Check again after creating table
                throw new SQLException("Cannot create " + databaseTableName + " table!");
            }
        }
        if (!this.isPlayerDataTableValid()) { // If PlayerData table is NOT valid
            throw new SQLException("Table " + databaseTableName + " is NOT valid!");
        }
        LOGGER.info("Valid " + databaseTableName + " table is in database");

        try {
            this.deleteBukkitPlayerDataFile(null); // Null will delete all bukkit player data files
            LOGGER.info("Successfully deleted Bukkit player data files");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Could not delete Bukkit player data files!", exception);
        }
    }

    /**
     * {@inheritDoc}
     * Will execute {@link PlayerDataUpdateRunnable} for all {@link PvPPlayer}s and deinitialize them.
     */
    @Override
    public void deinit() {
        LOGGER.info("Pushing all online player's data into database");
        for (PvPPlayer pvpPlayer : playerManager.getPvPPlayers()) {
            pvpPlayer.deinit();

            // Run the tasks synchronously without registering as the server is shutting down and
            // cannot queue Bukkit tasks

            PlayerDataUpdateRunnable playerDataUpdateRunnable = new PlayerDataUpdateRunnable(pvpPlayer, this);
            playerDataUpdateRunnable.run();
        }
    }

    /**
     * Selects PlayerData from SQL database.
     *
     * @param playerUUID the player uuid
     * @return the player data (null if no data exists)
     * @throws SQLException the sql exception
     */
    public synchronized PlayerData selectPlayerDataFromDatabase(UUID playerUUID) throws SQLException {
        return this.selectPlayerDataFromDatabase(playerUUID, null);
    }

    /**
     * Selects PlayerData from SQL database.
     *
     * @param playerName the player name
     * @return the player data (null if no data exists)
     * @throws SQLException the sql exception
     */
    public synchronized PlayerData selectPlayerDataFromDatabase(String playerName) throws SQLException {
        return this.selectPlayerDataFromDatabase(null, playerName);
    }

    /**
     * Selects PlayerData from SQL database.
     *
     * @param playerUUID the player uuid (can be null)
     * @param playerName the player name (can be null)
     * @return the player data (null if no data exists)
     * @throws SQLException the sql exception
     */
    @SuppressWarnings("unchecked") // Easier to ignore cast checking and let the runtime throw the exception if so
    private synchronized PlayerData selectPlayerDataFromDatabase(UUID playerUUID, String playerName)
            throws SQLException {
        if (playerUUID == null && playerName == null) {
            throw new IllegalArgumentException("UUID and Name cannot both be null!");
        }

        PlayerData playerData = new PlayerData();

        StringBuilder selectPlayerDataPreparedSQL = new StringBuilder("SELECT ");
        // Add columns in order according to databaseColumnsMap so that code below doesn't get column index wrong
        ArrayList<String> databaseColumnNames = new ArrayList<>(databaseColumnsMap.keySet());
        for (int i = 2; i < databaseColumnsMap.size(); i++) { // Start at 2 b/c no need to get 'uuid' or 'name'
            selectPlayerDataPreparedSQL.append(databaseColumnNames.get(i));
            if (i != databaseColumnsMap.size() - 1) {
                selectPlayerDataPreparedSQL.append(",");
            }
        }
        selectPlayerDataPreparedSQL.append(" FROM ");
        selectPlayerDataPreparedSQL.append(databaseTableName);
        selectPlayerDataPreparedSQL.append(" WHERE ");
        selectPlayerDataPreparedSQL.append(playerUUID != null ? "uuid=?" : "name=?");

        databaseManager.validateMySQLConnection();

        PreparedStatement selectPreparedStatement = databaseManager.getMySQLConnection().
                prepareStatement(selectPlayerDataPreparedSQL.toString());

        // Set UUID or name
        selectPreparedStatement.setString(1, playerUUID != null ? playerUUID.toString() : playerName);

        ResultSet resultSet = selectPreparedStatement.executeQuery();

        if (!resultSet.next()) { // no row present if .next() is false
            return null;
        }

        // Arenas times played parsing from DB logic
        gsonManager.getArenaSerializer().setReferenceDeserialization(true); // Turn on referencing to already-created Arenas
        HashMap arenaTimesPlayedMap = gsonManager.getGson().fromJson(resultSet.getString(2),
                playerData.getArenaTimesPlayedMap().getClass());
        playerData.setArenaTimesPlayedMap(arenaTimesPlayedMap);

        // Arena Inventory parsing from DB logic
        HashMap arenaInventory = gsonManager.getGson().fromJson(resultSet.getString(3),
                playerData.getArenaInventory().getClass());
        playerData.setArenaInventory(arenaInventory);

        // Set other primitives
        playerData.setELO(resultSet.getInt(1));
        playerData.setUnrankedFFAKills(resultSet.getInt(4));
        playerData.setUnrankedFFADeaths(resultSet.getInt(5));
        playerData.setRanked1v1Kills(resultSet.getInt(6));
        playerData.setRanked1v1Deaths(resultSet.getInt(7));
        playerData.setRanked1v1Wins(resultSet.getInt(8));
        playerData.setRanked1v1Losses(resultSet.getInt(9));
        playerData.setTeamPvPWins(resultSet.getInt(10));
        playerData.setTeamPvPLosses(resultSet.getInt(11));

        return playerData;
    }

    /**
     * Pushes PlayerData to SQL database.
     *
     * @param pvpPlayer the pvp player
     * @throws SQLException the sql exception
     */
    public synchronized void updatePlayerDataInDatabase(PvPPlayer pvpPlayer) throws SQLException {
        PlayerData playerData = pvpPlayer.getPlayerData();

        StringBuilder updatePlayerDataPreparedSQL = new StringBuilder("UPDATE " + databaseTableName + " SET ");
        ArrayList<String> databaseColumnNames = new ArrayList<>(databaseColumnsMap.keySet());
        for (int i = 1; i < databaseColumnsMap.size(); i++) { // Start at 1 b/c no need to update 'uuid'
            updatePlayerDataPreparedSQL.append(databaseColumnNames.get(i)); // key is column name
            updatePlayerDataPreparedSQL.append("=? ");
            if (i != databaseColumnsMap.size() - 1) {
                updatePlayerDataPreparedSQL.append(",");
            }
        }
        updatePlayerDataPreparedSQL.append("WHERE uuid=?");

        databaseManager.validateMySQLConnection();

        PreparedStatement updatePreparedStatement = databaseManager.getMySQLConnection()
                .prepareStatement(updatePlayerDataPreparedSQL.toString());

        // Set UUID
        updatePreparedStatement.setString(databaseColumnsMap.size(),
                pvpPlayer.getPlayer().getUniqueId().toString());

        this.setPreparedStatementWithPlayerData(updatePreparedStatement, pvpPlayer.getPlayer().getName(),
                playerData, 1); // start at index 1

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

        // e.g. INSERT INTO player_data VALUES("<UUID>", 1000, "{}", 0, 0, 0, 0, 0, 0);

        StringBuilder insertPlayerDataPreparedSQL = new StringBuilder("INSERT INTO " + databaseTableName + " VALUES(");
        for (int i = 0; i < databaseColumnsMap.size(); i++) {
            insertPlayerDataPreparedSQL.append("?");
            if (i != databaseColumnsMap.size() - 1) {
                insertPlayerDataPreparedSQL.append(",");
            }
        }
        insertPlayerDataPreparedSQL.append(")");

        databaseManager.validateMySQLConnection();

        PreparedStatement insertPreparedStatement = databaseManager.getMySQLConnection()
                .prepareStatement(insertPlayerDataPreparedSQL.toString());

        insertPreparedStatement.setString(1, pvpPlayer.getPlayer().getUniqueId().toString());

        this.setPreparedStatementWithPlayerData(insertPreparedStatement, pvpPlayer.getPlayer().getName(),
                playerData, 2); // start at index 2

        insertPreparedStatement.executeUpdate();
    }

    /**
     * Sets player data values within prepared statement according to {@link #getDatabaseColumnsMap()}
     * (excludes Primary Key aka UUID).
     *
     * @param preparedStatement      the prepared statement
     * @param playerName             the player name
     * @param playerData             the player data
     * @param questionMarkStartIndex the question mark start index (inclusive)
     * @throws SQLException the sql exception
     */
    private void setPreparedStatementWithPlayerData(PreparedStatement preparedStatement, String playerName,
                                                    PlayerData playerData, int questionMarkStartIndex) throws SQLException {
        gsonManager.getArenaSerializer().setReferenceSerialization(true); // Turn on referencing to already-created Arenas

        // Arena times played parsing logic
        String arenaTimesPlayedMapJson = gsonManager.getGson().toJson(playerData.getArenaTimesPlayedMap(),
                playerData.getArenaTimesPlayedMap().getClass());
        preparedStatement.setString(questionMarkStartIndex + 2, arenaTimesPlayedMapJson);

        // Arena Inventory parsing logic
        String arenaInventoryMapJson = gsonManager.getGson().toJson(playerData.getArenaInventory(),
                playerData.getArenaInventory().getClass());
        preparedStatement.setString(questionMarkStartIndex + 3, arenaInventoryMapJson);

        // Set other primitives
        preparedStatement.setString(questionMarkStartIndex, playerName);
        preparedStatement.setInt(questionMarkStartIndex + 1, playerData.getELO());
        preparedStatement.setInt(questionMarkStartIndex + 4, playerData.getUnrankedFFAKills());
        preparedStatement.setInt(questionMarkStartIndex + 5, playerData.getUnrankedFFADeaths());
        preparedStatement.setInt(questionMarkStartIndex + 6, playerData.getRanked1v1Kills());
        preparedStatement.setInt(questionMarkStartIndex + 7, playerData.getRanked1v1Deaths());
        preparedStatement.setInt(questionMarkStartIndex + 8, playerData.getRanked1v1Wins());
        preparedStatement.setInt(questionMarkStartIndex + 9, playerData.getRanked1v1Losses());
        preparedStatement.setInt(questionMarkStartIndex + 10, playerData.getTeamPvPWins());
        preparedStatement.setInt(questionMarkStartIndex + 11, playerData.getTeamPvPLosses());
    }

    /**
     * Create player data table.
     *
     * @throws SQLException the sql exception
     */
    @SuppressWarnings("concat")
    public synchronized void createPlayerDataTable() throws SQLException {
        // Table modeled after PlayerData object
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE " + databaseTableName + " (\n");

        ArrayList<String> databaseColumnNames = new ArrayList<>(databaseColumnsMap.keySet());
        for (String columnName : databaseColumnNames) {
            createTableSQL.append(columnName)
                    .append(" ")
                    .append(databaseColumnsMap.get(columnName))
                    .append(",");
        }
        createTableSQL.append("PRIMARY KEY (UUID)");
        createTableSQL.append(");");

        databaseManager.validateMySQLConnection();

        Statement createTableStatement = databaseManager.getMySQLConnection().createStatement();
        createTableStatement.execute(createTableSQL.toString());
    }

    /**
     * Checks if the {@link PlayerDataManager#getDatabaseTableName()} table exists in the database.
     *
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public synchronized boolean doesPlayerDataTableExist() throws SQLException {
        String tableExistsSQL = "SHOW TABLES LIKE '" + databaseTableName + "';";

        databaseManager.validateMySQLConnection();

        Statement tableExistsStatement = databaseManager.getMySQLConnection().createStatement();

        ResultSet resultSet = tableExistsStatement.executeQuery(tableExistsSQL);
        while (resultSet.next()) {
            if (resultSet.getString(1).equals(databaseTableName)) { // Only 1 column and that is the table names
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if player data table is valid (comparing columns in DB to {@link #getDatabaseColumnsMap()}).
     *
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public synchronized boolean isPlayerDataTableValid() throws SQLException {
        String describeTableSQL = "DESCRIBE " + databaseTableName + ";";

        databaseManager.validateMySQLConnection();

        Statement tableExistsStatement = databaseManager.getMySQLConnection().createStatement();

        ResultSet resultSet = tableExistsStatement.executeQuery(describeTableSQL);

        // Add table columns to temp list in order to compare properly to required table columns
        HashMap<String, String> realTableColumns = new HashMap<>();
        while (resultSet.next()) {
            String databaseColumnName = resultSet.getString(1); // 1st column is 'Field' which is column name
            String databaseColumnType = resultSet.getString(2); // 2nd column is 'type' which is column type
            realTableColumns.put(databaseColumnName, databaseColumnType);
        }

        // Search required column list because description table from SQL might in different order than
        // databaseColumnsList
        databaseColumnsListSearch:
        for (String requiredColumnName : databaseColumnsMap.keySet()) {
            String requiredColumnType = databaseColumnsMap.get(requiredColumnName).split(" ")[0];

            // Search realTableColumns
            for (String realColumnName : realTableColumns.keySet()) {
                String realColumnType = realTableColumns.get(realColumnName);

                // If found exact column name that is required and
                // if the columns types (e.g. INT, TEXT) are somewhat contained in the description table
                if (requiredColumnName.equals(realColumnName) &&
                        realColumnType.toLowerCase().contains(requiredColumnType.toLowerCase())) {
                    // Break out of database column list search and go on to next column from description table
                    continue databaseColumnsListSearch;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Gets top elo rankings.
     * NOTE: *MUST BE CALLED ASYNC* because this method queries DB and Mojang API
     *
     * @param numberOfRecords the number of records
     * @return the top elo rankings where the position of each entry is the rank (0 is first/highest), the key is
     * the player name, and the value is the elo value
     */
    public synchronized LinkedHashMap<String, Integer> getTopELORankings(int numberOfRecords)
            throws SQLException {
        LinkedHashMap<String, Integer> topELORankingsMap = new LinkedHashMap<>();

        String getELORankingsSQL = "SELECT name, elo FROM " + databaseTableName +
                " ORDER BY elo desc LIMIT " + numberOfRecords;

        databaseManager.validateMySQLConnection();

        Statement createTableStatement = databaseManager.getMySQLConnection().createStatement();
        ResultSet eloRankingResults = createTableStatement.executeQuery(getELORankingsSQL);

        while (eloRankingResults.next()) {
            String name = eloRankingResults.getString(1);
            Integer elo = eloRankingResults.getInt(2);
            topELORankingsMap.put(name, elo);
        }

        return topELORankingsMap;
    }

    public synchronized int getPlayerELORank(String playerName) throws SQLException {
        String selectPlayerELORankingSQL = "SELECT `rank` FROM (SELECT `name`, @rank := @rank + 1 AS `rank` FROM `" +
                databaseTableName + "`, (SELECT @rank := 0) `rankAlias` ORDER BY `elo` DESC) `descendingAlias` " +
                "WHERE name = ?;";

        databaseManager.validateMySQLConnection();

        PreparedStatement selectPlayerELORankedStatement = databaseManager.getMySQLConnection()
                .prepareStatement(selectPlayerELORankingSQL);

        selectPlayerELORankedStatement.setString(1, playerName);

        ResultSet selectPlayerELORankResult = selectPlayerELORankedStatement.executeQuery();

        if (selectPlayerELORankResult.next()) {
            return selectPlayerELORankResult.getInt(1);
        } else {
            return -1;
        }
    }

    /**
     * Deletes all bukkit player data files created by bukkit e.g. delete world/playerdata/UUID.dat for
     * every world.
     *
     * @param pvpPlayer the pvp player (null to delete all bukkit player data in all worlds)
     * @throws IOException the io exception
     */
    public synchronized void deleteBukkitPlayerDataFile(PvPPlayer pvpPlayer) throws IOException {
        for (World world : Bukkit.getWorlds()) {
            if (pvpPlayer == null) {
                File playerDataFolder = new File(world.getWorldFolder(), "playerdata");
                File[] playerDataFiles = playerDataFolder.listFiles();
                if (playerDataFiles != null) {
                    for (File playerDataFile : playerDataFiles) {
                        boolean deleteSuccess = playerDataFile.delete();
                        if (!deleteSuccess) {
                            throw new IOException("Could not delete " + playerDataFile.getName() + " data file!");
                        }
                    }
                }
            } else {
                File playerDataFile = new File(world.getWorldFolder(),
                        "playerdata/" + pvpPlayer.getPlayer().getUniqueId().toString() + ".dat");
                if (playerDataFile.exists()) {
                    boolean deleteSuccess = playerDataFile.delete();
                    if (!deleteSuccess) {
                        throw new IOException("Could not delete " + playerDataFile.getName() + " data file!");
                    }
                }
            }
        }
    }

    /**
     * Gets player manager.
     *
     * @return the player manager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * Gets database table name.
     *
     * @return the database table name
     */
    public String getDatabaseTableName() {
        return databaseTableName;
    }

    /**
     * Gets database columns map.
     * The key is the column name and the value is the column type (e.g. INT NOT NULL)
     *
     * @return the database columns map
     */
    public HashMap<String, String> getDatabaseColumnsMap() {
        return databaseColumnsMap;
    }
}
