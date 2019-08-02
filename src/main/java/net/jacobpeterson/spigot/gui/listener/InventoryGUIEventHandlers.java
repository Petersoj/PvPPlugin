package net.jacobpeterson.spigot.gui.listener;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;

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
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        PvPPlayer pvpPlayer = guiManager.getPvPPlugin().getPlayerManager().getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        // Call onInventoryClick in AbstractInventoryGUI for the proper inventory
    }
}
