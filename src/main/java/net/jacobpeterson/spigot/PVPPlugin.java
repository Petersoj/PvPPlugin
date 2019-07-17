package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.command.CommandListener;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPPlugin extends JavaPlugin implements Initializers {

    private GroupManager groupManager;
    private PluginListeners pluginListeners;
    private CommandListener commandListener;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        this.groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
        this.pluginListeners = new PluginListeners(this);
        this.commandListener = new CommandListener(this);
        this.guiManager = new GUIManager(this);

        this.init();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(pluginListeners, this);

        guiManager.init();

        // TODO use this later
        // groupManager.getWorldsHolder().getWorldData(player).getPermissionsHandler().getUserPrefix("username");
    }

    @Override
    public void deinit() {
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
    public GUIManager getGuiManager() {
        return guiManager;
    }
}

