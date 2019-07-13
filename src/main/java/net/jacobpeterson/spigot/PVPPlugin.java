package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPPlugin extends JavaPlugin implements Initializers {

    private GUIManager guiManager;

//        GroupManager groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
//        groupManager.getWorldsHolder().getWorldData(player).getPermissionsHandler().getUserPrefix("username");

    @Override
    public void onEnable() {
        this.guiManager = new GUIManager(this);

        this.init();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void init() {
        guiManager.init();
    }

    @Override
    public void deinit() {

    }
}

