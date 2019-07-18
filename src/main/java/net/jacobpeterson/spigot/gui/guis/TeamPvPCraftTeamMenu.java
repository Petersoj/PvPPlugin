package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TeamPvPCraftTeamMenu extends AbstractInventoryGUI {

    private GUIManager guiManager;
    private ItemStack backItem;

    public TeamPvPCraftTeamMenu(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public void init() {

    }

    @Override
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        // TODO TeamPvPCraftTeamMenu Inventory
    }
}
