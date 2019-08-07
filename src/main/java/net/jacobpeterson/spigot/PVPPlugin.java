package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.arena.ArenaManager;
import net.jacobpeterson.spigot.command.CommandListener;
import net.jacobpeterson.spigot.data.DatabaseConfig;
import net.jacobpeterson.spigot.data.DatabaseManager;
import net.jacobpeterson.spigot.data.GsonManager;
import net.jacobpeterson.spigot.game.GameManager;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.player.PlayerManager;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PvPPlugin extends JavaPlugin {

    private static Logger PLUGIN_LOGGER_INSTANCE = null; // This only for the getPluginLogger() static method

    private final Logger LOGGER;
    private GroupManager groupManager;
    private GsonManager gsonManager;
    private DatabaseConfig databaseConfig;
    private PluginListeners pluginListeners;
    private CommandListener commandListener;
    private DatabaseManager databaseManager;
    private ArenaManager arenaManager;
    private PlayerManager playerManager;
    private GameManager gameManager;
    private GUIManager guiManager;

    public PvPPlugin() {
        PLUGIN_LOGGER_INSTANCE = super.getLogger(); // The Bukkit logger is nicely formatted so use it globally
        LOGGER = PvPPlugin.getPluginLogger();

        LOGGER.info("Building " + this.getName());

        this.groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
        this.gsonManager = new GsonManager(this);
        this.databaseConfig = new DatabaseConfig(this);
        this.pluginListeners = new PluginListeners(this);
        this.commandListener = new CommandListener(this);
        this.databaseManager = new DatabaseManager(this);
        this.arenaManager = new ArenaManager(this);
        this.playerManager = new PlayerManager(this);
        this.gameManager = new GameManager(this);
        this.guiManager = new GUIManager(this);
    }

    /**
     * Gets plugin logger for this Plugin.
     * NOTE: This WILL be null if used in a static context in another class! The plugin class needs to be instantiated
     * by Spigot first before using this method (so you should only set logger instances in an init() or constructor).
     *
     * @return the plugin logger
     */
    public static Logger getPluginLogger() {
        return PLUGIN_LOGGER_INSTANCE;
    }

    @Override
    public void onEnable() {
        LOGGER.info("Initializing " + this.getName());

        try {
            gsonManager.init();
            databaseConfig.init();
            pluginListeners.init();
            databaseManager.init();
            arenaManager.init();
            playerManager.init();
            gameManager.init();
            guiManager.init();
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Cannot enable " + this.getName(), exception);

            Bukkit.getPluginManager().disablePlugin(this); // Disable plugin because of error in initializing plugin
            return;
        }

        LOGGER.info("Finished Initializing " + this.getName());
    }

    @Override
    public void onDisable() {
        LOGGER.info("Deinitializing " + this.getName());

        // Call all .deinit() methods in reverse order as some deinit() might use previous managers
        try {
            guiManager.deinit();
            gameManager.deinit();
            playerManager.deinit();
            arenaManager.deinit();
            databaseManager.deinit();
            pluginListeners.deinit();
            databaseConfig.deinit();
            gsonManager.deinit();
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Cannot disable " + this.getName(), exception);
        }

        LOGGER.info("Finished Deinitializing " + this.getName());
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
     * Gets gson manager.
     *
     * @return the gson manager
     */
    public GsonManager getGsonManager() {
        return gsonManager;
    }

    /**
     * Gets database config.
     *
     * @return the database config
     */
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    /**
     * Gets plugin listeners.
     *
     * @return the plugin listeners
     */
    public PluginListeners getPluginListeners() {
        return pluginListeners;
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
     * Gets database manager.
     *
     * @return the database manager
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Gets arena manager.
     *
     * @return the arena manager
     */
    public ArenaManager getArenaManager() {
        return arenaManager;
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

