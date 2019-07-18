package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class TeamPvPArena extends ChooseArenaMenu {

    public TeamPvPArena(GUIManager guiManager) {
        super(guiManager, ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Team PvP " +
                CharUtil.DOUBLE_RIGHT_ARROW + " Arena");
    }

    @Override
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        // TODO Ranked1v1 Inventory Interact
    }
}
