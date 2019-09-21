package net.jacobpeterson.pvpplugin.data;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.util.Initializers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseManager implements Initializers {

    private final Logger LOGGER;

    private PvPPlugin pvpPlugin;
    private String jdbcURL;
    private Connection mysqlConnection;

    /**
     * Instantiates a new Database manager to execute updates and query a MySQL database.
     *
     * @param pvpPlugin the pvp plugin
     */
    public DatabaseManager(PvPPlugin pvpPlugin) {
        this.LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() throws SQLException {
        DatabaseConfig databaseConfig = pvpPlugin.getDatabaseConfig();

        this.jdbcURL = "jdbc:mysql://" + databaseConfig.getMysqlHost() +
                ":" +
                databaseConfig.getMysqlPort() +
                "/" +
                databaseConfig.getMysqlDatabase() +
                "?useSSL=true";

        LOGGER.info("Attempting to connect to MySQL Database with URL: " + jdbcURL);
        this.createMySQLConnection();
        LOGGER.info("Database connection successful");
    }

    @Override
    public void deinit() throws SQLException {
        if (mysqlConnection != null && !mysqlConnection.isClosed()) {
            mysqlConnection.close();
        }
    }

    /**
     * Creates the MySQL connection.
     *
     * @throws SQLException the sql exception
     */
    private void createMySQLConnection() throws SQLException {
        DatabaseConfig databaseConfig = pvpPlugin.getDatabaseConfig();

        mysqlConnection = DriverManager.getConnection(jdbcURL, databaseConfig.getMysqlUsername(),
                databaseConfig.getMysqlPassword());
    }

    /**
     * Validate my sql connection.
     *
     * @throws SQLException the sql exception
     */
    public void validateMySQLConnection() throws SQLException {
        if (!mysqlConnection.isValid(5)) { // Allow for 5 seconds to validate connection
            // Try to reconnect as the connection may have timed out
            this.createMySQLConnection();
        }
    }

    /**
     * Gets mysql connection.
     *
     * @return the mysql connection
     */
    public Connection getMySQLConnection() {
        return mysqlConnection;
    }
}
