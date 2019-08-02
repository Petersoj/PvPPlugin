package net.jacobpeterson.spigot.gui.guis.teampvp;

import net.jacobpeterson.spigot.gui.guis.ChooseArenaMenu;
import net.jacobpeterson.spigot.player.gui.PlayerGUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class TeamPvPArenaMenu extends ChooseArenaMenu {

    public TeamPvPArenaMenu(PlayerGUIManager playerGUIManager) {
        super(playerGUIManager, ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Team PvP " +
                CharUtil.DOUBLE_RIGHT_ARROW + " Arena");
    }

    @Override
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        // TODO Ranked1v1 Inventory Interact
    }
}
