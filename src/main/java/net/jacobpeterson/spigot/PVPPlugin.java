package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.command.CommandListener;
import net.jacobpeterson.spigot.data.DatabaseManager;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.anjocaido.groupmanager.GroupManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class PvPPlugin extends JavaPlugin implements Initializers {

    private static final Logger LOGGER = LogManager.getLogger();

    private GroupManager groupManager;
    private PvPConfig pvpConfig;
    private PvPListeners pvpListeners;
    private CommandListener commandListener;
    private DatabaseManager databaseManager;
    private PlayerManager playerManager;
    private GUIManager guiManager;

    public PvPPlugin() {
        LOGGER.info("Building PvPPlugin");

        this.groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
        this.pvpConfig = new PvPConfig(this);
        this.pvpListeners = new PvPListeners(this);
        this.commandListener = new CommandListener(this);
        this.databaseManager = new DatabaseManager(this);
        this.playerManager = new PlayerManager(this);
        this.guiManager = new GUIManager(this);
    }

    @Override
    public void onEnable() {
        LOGGER.info("Initializing PvPPlugin");

        try {
            this.init();
        } catch (SQLException exception) {
            LOGGER.fatal(exception);

            Bukkit.getPluginManager().disablePlugin(this); // Disable plugin
        }
    }

    @Override
    public void onDisable() {
        LOGGER.info("Deinitializing PvPPlugin");

        try {
            this.deinit();
        } catch (SQLException sqlException) {
            LOGGER.fatal(sqlException);
        }
    }

    @Override
    public void init() throws SQLException {
        pvpConfig.init();
        pvpListeners.init();
        databaseManager.init();
        playerManager.init();
//        guiManager.init();
    }

    @Override
    public void deinit() throws SQLException {
        pvpConfig.deinit();
        pvpListeners.deinit();
        databaseManager.deinit();
        playerManager.deinit();
//        guiManager.deinit();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        return commandListener.onCommand(commandSender, command, alias, args);
    }

    /**
     * Gets group manager.
     *
     * @return the group manager
     */
    public GroupManager getGroupManager() {
        return groupManager;
    }

    /**
     * Gets pvp config.
     *
     * @return the pvp config
     */
    public PvPConfig getPvPConfig() {
        return pvpConfig;
    }

    /**
     * Gets pvp listeners.
     *
     * @return the pvp listeners
     */
    public PvPListeners getPvPListeners() {
        return pvpListeners;
    }

    /**
     * Gets command listener.
     *
     * @return the command listener
     */
    public CommandListener getCommandListener() {
        return commandListener;
    }

    /**
     * Gets gui manager.
     *
     * @return the gui manager
     */
    public GUIManager getGUIManager() {
        return guiManager;
    }

    /**
     * Gets player manager.
     *
     * @return the player manager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}

