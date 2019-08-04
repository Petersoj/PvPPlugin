package net.jacobpeterson.spigot.gui;

import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

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
     * Method triggered on player inventory interact event.
     *
     * @param event the Inventory interact event
     */
    public abstract void onInventoryInteractEvent(InventoryInteractEvent event);

    /**
     * Will close the inventory for all current viewers.
     */
    public void closeViewers() {
        if (inventory != null) {
            inventory.getViewers().iterator().forEachRemaining(HumanEntity::closeInventory);
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
