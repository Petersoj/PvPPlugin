package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends AbstractInventoryGUI {

    private GUIManager guiManager;
    private ItemStack ranked1v1Item;
    private ItemStack unrankedFFAItem;
    private ItemStack teamPvPItem;
    private String currentlyPlayingLine;

    /**
     * Instantiates a new Main menu.
     *
     * @param guiManager the gui manager
     */
    public MainMenu(GUIManager guiManager) {
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

        unrankedFFAItem = new ItemStack(Material.BOW);
        ItemStackUtil.formatLore(unrankedFFAItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Play Unranked FFA",
                ChatColor.GOLD + "Play FFA with or against your friends for fun.",
                ChatColor.GOLD + "Perfect activity during queues.",
                ChatColor.RESET + "",
                currentlyPlayingLine);

        teamPvPItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3); // damage of 3 = player skull
        ItemStackUtil.formatLore(teamPvPItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Play Team PvP",
                ChatColor.GOLD + "Here you can play 2v2s and soon 3v3s!",
                ChatColor.GOLD + "Join a team or craft your own!",
                ChatColor.GOLD + "For more information, please visit",
                ChatColor.AQUA + "mcsiege.namelesshosting.com/teampvp");

        this.createInventory();
    }

    @Override
    public void createInventory() {
        String title = ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Main Menu";
        this.inventory = Bukkit.createInventory(null, 9, title);

        inventory.setItem(0, ranked1v1Item);
        this.updateFFACurrentlyPlaying(0); // Also set ItemStack in Inventory at index 1
        inventory.setItem(2, teamPvPItem);
    }

    @Override
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        event.setCancelled(true);
        // TODO MainMenu Inventory Interact
    }

    /**
     * Update FFA currently playing on Itemstack.
     *
     * @param ffaCurrentlyPlaying the number of FFA players currently playing
     */
    public void updateFFACurrentlyPlaying(int ffaCurrentlyPlaying) {
        ItemStackUtil.setLoreLine(unrankedFFAItem, 3, currentlyPlayingLine + ChatColor.AQUA + ffaCurrentlyPlaying);
        inventory.setItem(1, unrankedFFAItem); // Will update any clients' open GUI
    }
}
