package net.jacobpeterson.spigot.gui.guis.team2v2;

import net.jacobpeterson.spigot.game.games.team2v2.Team2v2;
import net.jacobpeterson.spigot.game.games.team2v2.itemstack.Team2v2ItemStack;
import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.gui.PlayerGUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Team2v2Menu extends AbstractInventoryGUI {

    private static ItemStack CRAFT_TEAM_ITEM;
    private static ItemStack BACK_ITEM;

    private final PlayerGUIManager playerGUIManager;
    private ArrayList<Team2v2ItemStack> team2v2ItemStacks;

    public Team2v2Menu(PlayerGUIManager playerGUIManager) {
        this.playerGUIManager = playerGUIManager;
        this.team2v2ItemStacks = new ArrayList<>();
    }

    @Override
    public void init() {
        if (CRAFT_TEAM_ITEM == null) {
            CRAFT_TEAM_ITEM = new ItemStack(Material.WORKBENCH, 2);
            ItemStackUtil.formatLore(CRAFT_TEAM_ITEM, true,
                    CharUtil.boldColor(ChatColor.YELLOW) + "Craft Your Own 1v1 Team",
                    ChatColor.GOLD + "Be the leader of a team and invite others.");
        }

        if (BACK_ITEM == null) {
            BACK_ITEM = new ItemStack(Material.BOOK);
            ItemStackUtil.formatLore(BACK_ITEM, true,
                    CharUtil.boldColor(ChatColor.YELLOW) + "Back", (String[]) null);
        }

        this.createInventory();
    }

    @Override
    public void createInventory() {
        // TODO create Inventory based on # of teams
//        inventory.setItem(0, craftTeamItem);
//        inventory.setItem(8, backItem);
    }

    @Override
    public void onInventoryClickEvent(PvPPlayer pvpPlayer, InventoryClickEvent event) {
        event.setCancelled(true);
        // TODO TeamPvP inventory
    }

    public void updateTeams(ArrayList<Team2v2> team2v2s) {
        for (Team2v2 team2v2 : team2v2s) {
            // TODO
        }
    }
}
