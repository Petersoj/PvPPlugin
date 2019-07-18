package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.command.CommandListener;
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

public class PvPPlugin extends JavaPlugin implements Initializers {

    public static final Logger LOGGER = LogManager.getLogger();

    private GroupManager groupManager;
    private PluginListeners pluginListeners;
    private CommandListener commandListener;
    private GUIManager guiManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        LOGGER.info("Building PvPPlugin");

        this.groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
        this.pluginListeners = new PluginListeners(this);
        this.commandListener = new CommandListener(this);
        this.guiManager = new GUIManager(this);
        this.playerManager = new PlayerManager(this);

        LOGGER.info("Initializing PvPPlugin");
        this.init(); // this onEnable() is kinda acting as a constructor when using Spigot
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void init() {
        pluginListeners.init();
        guiManager.init();
    }

    @Override
    public void deinit() {
        pluginListeners.deinit();
        guiManager.deinit();
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

