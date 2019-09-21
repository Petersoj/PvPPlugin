package net.jacobpeterson.pvpplugin.gui.guis.team2v2;

import net.jacobpeterson.pvpplugin.gui.AbstractInventoryGUI;
import net.jacobpeterson.pvpplugin.itemstack.ItemStackUtil;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.gui.PlayerGUIManager;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class Team2v2CraftTeamMenu extends AbstractInventoryGUI {

    private final PlayerGUIManager playerGUIManager;
    private ItemStack backItem;

    public Team2v2CraftTeamMenu(PlayerGUIManager playerGUIManager) {
        this.playerGUIManager = playerGUIManager;
    }

    @Override
    public void init() {
        backItem = new ItemStack(Material.BOOK);
        ItemStackUtil.formatLore(backItem, true,
                ChatUtil.boldColor(ChatColor.YELLOW) + "Back", (String[]) null);
    }

    @Override
    public void createInventory() {
        // TODO create Inventory lines based on number of prospective players who want to join
//        inventory.setItem(8, backItem);
    }

    @Override
    public void onInventoryClickEvent(PvPPlayer pvpPlayer, InventoryClickEvent event) {
        // TODO TeamPvPCraftTeamMenu Inventory
    }
}
