package net.jacobpeterson.pvpplugin.data;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class DatabaseConfig implements Initializers {

    private static final String MYSQL_HOST_KEY = "mysql_host";
    private static final String MYSQL_PORT_KEY = "mysql_port";
    private static final String MYSQL_DATABASE_KEY = "mysql_database";
    private static final String MYSQL_USERNAME_KEY = "mysql_username";
    private static final String MYSQL_PASSWORD_KEY = "mysql_password";

    private final Logger LOGGER;
    private PvPPlugin pvpPlugin;
    private String mysqlHost;
    private int mysqlPort;
    private String mysqlDatabase;
    private String mysqlUsername;
    private String mysqlPassword;

    /**
     * Instantiates a new Database config. Used to get Java objects from config.yml.
     *
     * @param pvpPlugin the pvp plugin
     */
    public DatabaseConfig(PvPPlugin pvpPlugin) {
        this.LOGGER = PvPPlugin.getPluginLogger();
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() {
        FileConfiguration fileConfiguration = pvpPlugin.getConfig();

        fileConfiguration.options().copyDefaults(true); // Copy Defaults if config doesn't exist
        pvpPlugin.saveConfig(); // Write config to plugin folder

        mysqlHost = fileConfiguration.getString(MYSQL_HOST_KEY);
        mysqlPort = fileConfiguration.getInt(MYSQL_PORT_KEY);
        mysqlDatabase = fileConfiguration.getString(MYSQL_DATABASE_KEY);
        mysqlUsername = fileConfiguration.getString(MYSQL_USERNAME_KEY);
        mysqlPassword = fileConfiguration.getString(MYSQL_PASSWORD_KEY);

        LOGGER.info("Loaded Configuration");
    }

    @Override
    public void deinit() {
    }

    /**
     * Gets mysql host.
     *
     * @return the mysql host
     */
    public String getMysqlHost() {
        return mysqlHost;
    }

    /**
     * Gets mysql port.
     *
     * @return the mysql port
     */
    public int getMysqlPort() {
        return mysqlPort;
    }

    /**
     * Gets mysql database.
     *
     * @return the mysql database
     */
    public String getMysqlDatabase() {
        return mysqlDatabase;
    }

    /**
     * Gets mysql username.
     *
     * @return the mysql username
     */
    public String getMysqlUsername() {
        return mysqlUsername;
    }

    /**
     * Gets mysql password.
     *
     * @return the mysql password
     */
    public String getMysqlPassword() {
        return mysqlPassword;
    }
}
