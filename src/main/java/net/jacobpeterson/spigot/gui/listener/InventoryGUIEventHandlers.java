package net.jacobpeterson.spigot.gui.listener;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.gui.GUIManager;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.logging.Logger;

public class InventoryGUIEventHandlers {

    private final Logger LOGGER;
    private GUIManager guiManager;

    /**
     * Instantiates a new InventoryGUIListeners for handling Inventory-related Bukkit events.
     *
     * @param guiManager the gui manager
     */
    public InventoryGUIEventHandlers(GUIManager guiManager) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.guiManager = guiManager;
    }

    /**
     * Handle on inventory interact event.
     *
     * @param event the event
     */
    public void handleOnInventoryInteractEvent(InventoryInteractEvent event) {

    }

    /**
     * Handle on inventory open event.
     *
     * @param event the event
     */
    public void handleOnInventoryOpenEvent(InventoryOpenEvent event) {

    }

}
