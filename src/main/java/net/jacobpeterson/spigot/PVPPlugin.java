package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.gui.GUIManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPPlugin extends JavaPlugin {

    private GUIManager guiManager;

//        GroupManager groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
//        groupManager.getWorldsHolder().getWorldData(player).getPermissionsHandler().getUserPrefix("username");

    @Override
    public void onEnable() {
        this.guiManager = new GUIManager(this);

        guiManager.init();
    }

    @Override
    public void onDisable() {

    }
}

