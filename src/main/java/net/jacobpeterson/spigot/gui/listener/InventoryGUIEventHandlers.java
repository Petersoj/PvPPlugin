package net.jacobpeterson.spigot.gui.listener;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

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
     * Handle on inventory click event.
     *
     * @param event the event
     */
    public void handleOnInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        PvPPlayer pvpPlayer = guiManager.getPvPPlugin().getPlayerManager().getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        // Two different types of Inventory Managers to search and match a reference to:
        // GUIManager
        // PlayerGUIManager

        AbstractInventoryGUI guiManagerInventory = guiManager.getInventoryGUI(inventory);
        if (guiManagerInventory != null) {
            guiManagerInventory.onInventoryClickEvent(pvpPlayer, event);
            return;
        }

        AbstractInventoryGUI playerGUIManagerInventory = pvpPlayer.getPlayerGUIManager().getInventoryGUI(inventory);
        if (playerGUIManagerInventory != null) {
            playerGUIManagerInventory.onInventoryClickEvent(pvpPlayer, event);
            return;
        }
    }
}
