package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import net.jacobpeterson.spigot.util.ItemStackUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        // TODO create Inventory lines based on number of prospective players who want to join

        backItem = new ItemStack(Material.BOOK);
        ItemStackUtil.formatLore(backItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Back",
                "");
        inventory.setItem(8, backItem);
    }

    @Override
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        // TODO TeamPvPCraftTeamMenu Inventory
    }
}
