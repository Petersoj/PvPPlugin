package net.jacobpeterson.pvpplugin.gui.guis.ranked1v1;

import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.pvpplugin.gui.guis.ChooseArenaMenu;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.gui.PlayerGUIManager;
import net.jacobpeterson.pvpplugin.util.CharUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Ranked1v1Menu extends ChooseArenaMenu {

    public Ranked1v1Menu(PlayerGUIManager playerGUIManager) {
        super(playerGUIManager, ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Ranked 1v1");
    }

    @Override
    public void onInventoryClickEvent(PvPPlayer pvpPlayer, InventoryClickEvent event) {
        for (ArenaItemStack arenaItemStack : arenaItemStacks) {
//            if () {
//
//            }
        }
    }
}
