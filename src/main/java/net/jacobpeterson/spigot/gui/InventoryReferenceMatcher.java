package net.jacobpeterson.spigot.gui;

import org.bukkit.inventory.Inventory;

public interface InventoryReferenceMatcher {

    /**
     * This method is used by InventoryGUI managers as a way to get an {@link AbstractInventoryGUI}
     * that is wrapping the passed in inventory. (Mostly used to get the wrapper for
     * {@link org.bukkit.event.inventory.InventoryClickEvent}).
     *
     * @param inventory the inventory
     * @return the {@link AbstractInventoryGUI} wrapping the passed in inventory
     */
    AbstractInventoryGUI getInventoryGUI(Inventory inventory);

}
