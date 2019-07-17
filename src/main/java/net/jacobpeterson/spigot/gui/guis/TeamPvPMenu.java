package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class TeamPvPMenu extends AbstractInventoryGUI implements Initializers {

    private GUIManager guiManager;

    public TeamPvPMenu(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init() {

    }

    @Override
    public void deinit() {

    }

    @Override
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        event.setCancelled(true);
        // TODO TeamPvP inventory
    }
}
