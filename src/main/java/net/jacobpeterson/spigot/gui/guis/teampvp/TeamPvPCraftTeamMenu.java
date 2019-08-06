package net.jacobpeterson.spigot.gui.guis.teampvp;

import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import net.jacobpeterson.spigot.player.gui.PlayerGUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TeamPvPCraftTeamMenu extends AbstractInventoryGUI {

    private final PlayerGUIManager playerGUIManager;
    private ItemStack backItem;

    public TeamPvPCraftTeamMenu(PlayerGUIManager playerGUIManager) {
        this.playerGUIManager = playerGUIManager;
    }

    @Override
    public void init() {
        backItem = new ItemStack(Material.BOOK);
        ItemStackUtil.formatLore(backItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Back",
                "");
    }

    @Override
    public void createInventory() {
        // TODO create Inventory lines based on number of prospective players who want to join
//        inventory.setItem(8, backItem);
    }

    @Override
    public void onInventoryClickEvent(InventoryClickEvent event) {
        // TODO TeamPvPCraftTeamMenu Inventory
    }
}
