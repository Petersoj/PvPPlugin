package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.command.CommandListener;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPPlugin extends JavaPlugin implements Initializers {

    private PluginListeners pluginListeners;
    private CommandListener commandListener;
    private GUIManager guiManager;

//        GroupManager groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
//        groupManager.getWorldsHolder().getWorldData(player).getPermissionsHandler().getUserPrefix("username");

    @Override
    public void onEnable() {
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
    }

    @Override
    public void deinit() {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        return commandListener.onCommand(commandSender, command, alias, args);
    }
}

