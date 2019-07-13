package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import net.jacobpeterson.spigot.util.Initializers;
import net.jacobpeterson.spigot.util.ItemStackUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends AbstractInventoryGUI implements Initializers {

    private GUIManager guiManager;
    private ItemStack ranked1v1Item;
    private ItemStack unrankedFFAItem;
    private ItemStack teamPvPItem;
    private String currentlyPlayingLine;

    public MainMenu(GUIManager guiManager) {
        super(9, ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Main Menu");
        this.guiManager = guiManager;
        this.currentlyPlayingLine = ChatColor.GOLD + "Currently Playing" + ChatColor.GRAY + ": ";
    }

    @Override
    public void init() {
        ranked1v1Item = new ItemStack(Material.IRON_SWORD);
        ItemStackUtil.formatLore(ranked1v1Item, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Play Ranked 1v1",
                ChatColor.GOLD + "We will match up you up against the",
                ChatColor.GOLD + "most suitable opponent possible!");
        this.getInventory().setItem(0, ranked1v1Item);

        unrankedFFAItem = new ItemStack(Material.BOW);
        ItemStackUtil.formatLore(unrankedFFAItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Play Unranked FFA",
                ChatColor.GOLD + "Play FFA with or against your friends for fun.",
                ChatColor.GOLD + "Perfect activity during queues.",
                ChatColor.RESET + "",
                currentlyPlayingLine);
        this.updateFFACurrentlyPlaying(0); // Also adds ItemStack to Inventory at index 1

        teamPvPItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3); // damage of 3 = player skull
        ItemStackUtil.formatLore(teamPvPItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Play Team PvP",
                ChatColor.GOLD + "Here you can play 2v2s and soon 3v3s!",
                ChatColor.GOLD + "Join a team or craft your own!",
                ChatColor.GOLD + "For more information, please visit",
                ChatColor.AQUA + "mcsiege.namelesshosting.com/teampvp");
        this.getInventory().setItem(2, teamPvPItem);
    }

    @Override
    public void deinit() {
    }

    @Override
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        event.setCancelled(true);
    }

    public void updateFFACurrentlyPlaying(int ffaCurrentlyPlaying) {
        ItemStackUtil.setLoreLine(unrankedFFAItem, 3, currentlyPlayingLine + ChatColor.AQUA + ffaCurrentlyPlaying);
        inventory.setItem(1, unrankedFFAItem); // Updates any client's open GUI
    }
}
