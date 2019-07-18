package net.jacobpeterson.spigot.gui;

import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
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
     * Instantiates a new Abstract inventory GUI.
     *
     * @param size  the size
     * @param title the title
     */
    public AbstractInventoryGUI(int size, String title) {
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    /**
     * {@inheritDoc}
     * Will close the inventory for all current viewers.
     */
    @Override
    public void deinit() {
        if (inventory != null) {
            inventory.getViewers().stream()
                    .filter(humanEntity -> humanEntity instanceof Player)
                    .forEach(HumanEntity::closeInventory);
        }
    }

    /**
     * Method triggered on player inventory interact event.
     *
     * @param event the Inventory interact event
     */
    public abstract void onInventoryInteractEvent(InventoryInteractEvent event);

    /**
     * Gets inventory.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}
