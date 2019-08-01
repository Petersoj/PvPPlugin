package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.logging.Logger;

public abstract class ChooseArenaMenu extends AbstractInventoryGUI {

    protected static ItemStack ANY_ITEM;
    protected static ItemStack BACK_ITEM;

    protected final Logger LOGGER;

    protected GUIManager guiManager;
    protected String title;
    protected ArrayList<Arena> arenas;

    /**
     * Instantiates a new 'Choose Arena' menu. This is meant to be extended by other GUI/menus as this is a general viewer.
     *
     * @param guiManager the gui manager
     * @param title      the title
     */
    public ChooseArenaMenu(GUIManager guiManager, String title) {
        this.LOGGER = PvPPlugin.getPluginLogger();
        this.guiManager = guiManager;
        this.title = title;
        this.arenas = guiManager.getPvPPlugin().getArenaManager().getAllArenas();
    }

    @Override
    public void init() {
        if (ANY_ITEM == null) {
            ANY_ITEM = new ItemStack(Material.EMERALD);
            ItemStackUtil.formatLore(ANY_ITEM, true,
                    CharUtil.boldColor(ChatColor.YELLOW) + "Any",
                    ChatColor.GOLD + "Play on any arena (fastest)");
        }

        if (BACK_ITEM == null) {
            BACK_ITEM = new ItemStack(Material.BOOK);
            ItemStackUtil.formatLore(BACK_ITEM, true,
                    CharUtil.boldColor(ChatColor.YELLOW) + "Back",
                    "");
        }

        this.createInventory();
    }

    @Override
    public void createInventory() {
        if (arenas == null) {
            LOGGER.warning("Null Arenas ArrayList in ChooseArenaMenu!");
            return;
        }

        int chestLines = (int) Math.ceil((arenas.size() == 0 ? 2 : arenas.size()) / 5); // 5 Arena ItemStacks per chest row
        if (chestLines > 6) {
            throw new UnsupportedOperationException("Pagination not implemented! Too many arenas created!");
        }
        inventory = Bukkit.createInventory(null, chestLines * 9, title);

        inventory.setItem(0, ANY_ITEM);
        inventory.setItem(8, BACK_ITEM);

        if (arenas != null) {
            int currentIndex = 2;
            int currentLine = 0;
            for (Arena arena : arenas) {
                if (currentIndex > 7) {
                    currentIndex = 2;
                    currentLine++;
                }

                if (arena.getArenaItemStack() == null) {
                    LOGGER.warning("No Arena ItemStack representation for arena: " + arena.getName());
                } else {
                    inventory.setItem((currentLine * 9) + currentIndex, arena.getArenaItemStack().getItemStack());
                }

                currentIndex++;
            }
        }
    }

    /**
     * Update arenas shown in the GUI.
     * Note that this simply execute {@link AbstractInventoryGUI#closeViewers()}
     * and then {@link AbstractInventoryGUI#createInventory()} respectively.
     */
    public void updateArenas() {
        this.closeViewers();
        this.createInventory();
    }

    /**
     * Gets arenas.
     *
     * @return the arenas
     */
    public ArrayList<Arena> getArenas() {
        return arenas;
    }

    /**
     * Sets arenas.
     *
     * @param arenas the arenas
     */
    public void setArenas(ArrayList<Arena> arenas) {
        this.arenas = arenas;
    }
}
