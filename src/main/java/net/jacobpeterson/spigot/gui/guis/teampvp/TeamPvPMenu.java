package net.jacobpeterson.spigot.gui.guis.teampvp;

import net.jacobpeterson.spigot.gamemode.team2v2pvp.Team2v2;
import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamPvPMenu extends AbstractInventoryGUI {

    private GUIManager guiManager;
    private ItemStack craftTeamItem;
    private ItemStack backItem;
    private HashMap<ItemStack, Team2v2> teamItems;

    public TeamPvPMenu(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.teamItems = new HashMap<>();
    }

    @Override
    public void init() {
        craftTeamItem = new ItemStack(Material.WORKBENCH, 2);
        ItemStackUtil.formatLore(craftTeamItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Craft Your Own 1v1 Team",
                ChatColor.GOLD + "Be the leader of a team and invite others.");

        backItem = new ItemStack(Material.BOOK);
        ItemStackUtil.formatLore(backItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Back",
                "");
    }

    @Override
    public void createInventory() {
        // TODO create Inventory based on # of teams
//        inventory.setItem(0, craftTeamItem);
//        inventory.setItem(8, backItem);
    }

    @Override
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        event.setCancelled(true);
        // TODO TeamPvP inventory
    }

    public void updateTeams(ArrayList<Team2v2> teams) {
        for (Team2v2 team2v2 : teams) {
            // TODO
        }
    }
}
