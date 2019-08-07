package net.jacobpeterson.spigot.gui.guis.team2v2;

import net.jacobpeterson.spigot.gui.guis.ChooseArenaMenu;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.gui.PlayerGUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Team2v2ArenaMenu extends ChooseArenaMenu {

    public Team2v2ArenaMenu(PlayerGUIManager playerGUIManager) {
        super(playerGUIManager, ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Team PvP " +
                CharUtil.DOUBLE_RIGHT_ARROW + " Arena");
    }

    @Override
    public void onInventoryClickEvent(PvPPlayer pvpPlayer, InventoryClickEvent event) {
        // TODO Ranked1v1 Inventory Interact
    }
}
