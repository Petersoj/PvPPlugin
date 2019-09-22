package net.jacobpeterson.pvpplugin.gui;

import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

/**
 * The type Abstract Inventory GUI.
 */
public abstract class AbstractInventoryGUI implements Initializers {

    protected Inventory inventory;

    /**
     * Instantiates a new Abstract inventory gui with no initial inventory created.
     */
    public AbstractInventoryGUI() {
    }

    /**
     * {@inheritDoc}
     * Will close the inventory for all current viewers.
     */
    @Override
    public void deinit() {
        this.closeViewers();
    }

    /**
     * Method used to (re)create the inventory and populate it.
     */
    public abstract void createInventory();

    /**
     * Method triggered on player inventory click event.
     *
     * @param pvpPlayer the pvp player
     * @param event     the Inventory click event
     */
    public abstract void onInventoryClickEvent(PvPPlayer pvpPlayer, InventoryClickEvent event);

    /**
     * Open this inventory for a player.
     *
     * @param pvpPlayer the pvp player
     */
    public void open(PvPPlayer pvpPlayer) {
        pvpPlayer.getPlayer().openInventory(inventory);
    }

    /**
     * Will close the inventory for all current viewers.
     */
    public void closeViewers() {
        if (inventory != null) {
            // Must copy references to prevent CME
            ArrayList<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());
            viewers.forEach(HumanEntity::closeInventory);
        }
    }

    /**
     * Gets inventory.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}
