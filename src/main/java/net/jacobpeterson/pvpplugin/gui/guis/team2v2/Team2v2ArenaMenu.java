package net.jacobpeterson.pvpplugin.gui.guis.team2v2;

import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.gui.guis.ChooseArenaMenu;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.gui.PlayerGUIManager;
import net.jacobpeterson.pvpplugin.util.CharUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Team2v2ArenaMenu extends ChooseArenaMenu {

    public Team2v2ArenaMenu(ArenaManager arenaManager, PlayerGUIManager playerGUIManager) {
        super(arenaManager, playerGUIManager,
                ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Team PvP " +
                        CharUtil.DOUBLE_RIGHT_ARROW + " Arena");
    }

    @Override
    public void onInventoryClickEvent(PvPPlayer pvpPlayer, InventoryClickEvent event) {
        // TODO Ranked1v1 Inventory Interact
    }
}
