package net.jacobpeterson.spigot.data;

import net.jacobpeterson.spigot.PvPConfig;
import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.util.Initializers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager implements Initializers {

    private static final Logger LOGGER = LogManager.getLogger();

    private PvPPlugin pvpPlugin;
    private Connection mysqlConnection;

    /**
     * Instantiates a new Database manager to execute updates and query a MySQL database.
     *
     * @param pvpPlugin the pvp plugin
     */
    public DatabaseManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() throws SQLException {
        PvPConfig pvpConfig = pvpPlugin.getPvPConfig();

        StringBuilder urlBuilder = new StringBuilder("jdbc:mysql://");
        urlBuilder.append(pvpConfig.getMysqlHost());
        urlBuilder.append(":");
        urlBuilder.append(pvpConfig.getMysqlPort());
        urlBuilder.append("/");
        urlBuilder.append(pvpConfig.getMysqlDatabase());
        urlBuilder.append("?useSSL=true");

        String mysqlURL = urlBuilder.toString();

        LOGGER.info("Attempting to connect to MySQL Database with URL {}", mysqlURL);
        mysqlConnection = DriverManager.getConnection(mysqlURL, pvpConfig.getMysqlUsername(),
                pvpConfig.getMysqlPassword());
        LOGGER.info("Database connection successful!");
    }

    @Override
    public void deinit() throws SQLException {
        if (mysqlConnection != null && !mysqlConnection.isClosed()) {
            mysqlConnection.close();
        }
    }
}
