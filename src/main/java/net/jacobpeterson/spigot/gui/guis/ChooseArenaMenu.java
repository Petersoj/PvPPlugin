package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public abstract class ChooseArenaMenu extends AbstractInventoryGUI {

    public static final Logger LOGGER = LogManager.getLogger();

    protected GUIManager guiManager;
    protected String title;
    protected ItemStack anyItem;
    protected ItemStack backItem;
    protected ArrayList<Arena> arenas;

    /**
     * Instantiates a new 'Choose Arena' menu. This is meant to be extended by other GUI/menus as this is a general viewer.
     *
     * @param guiManager the gui manager
     * @param title      the title
     */
    public ChooseArenaMenu(GUIManager guiManager, String title) {
        this.guiManager = guiManager;
        this.title = title;
    }

    @Override
    public void init() {
        int chestLines = (int) Math.ceil((arenas.size() == 0 ? 5 : arenas.size()) / 5); // 5 Arena ItemStacks per chest row
        if (chestLines > 6) {
            throw new UnsupportedOperationException("Pagination not implemented! Too many arenas created!");
        }
        inventory = Bukkit.createInventory(null, chestLines * 9, title);

        anyItem = new ItemStack(Material.EMERALD);
        ItemStackUtil.formatLore(anyItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Any",
                ChatColor.GOLD + "Play on any arena (fastest)");
        inventory.setItem(0, anyItem);

        backItem = new ItemStack(Material.BOOK);
        ItemStackUtil.formatLore(backItem, true,
                CharUtil.boldColor(ChatColor.YELLOW) + "Back",
                "");
        inventory.setItem(8, backItem);

        if (arenas != null) {
            int currentIndex = 1;
            int currentLine = 0;
            for (Arena arena : arenas) {
                if (currentIndex > 5) {
                    currentIndex = 0;
                    currentLine++;
                }

                if (arena.getItemStack() == null) {
                    LOGGER.warn("No Arena ItemStack representation for arena: " + arena.getName());
                } else {
                    inventory.setItem((currentLine * 9) + currentIndex, arena.getItemStack());
                }

                currentIndex++;
            }
        }
    }

    /**
     * Update arenas shown in the GUI.
     * Note that this simply execute {@link AbstractInventoryGUI#deinit()} and then {@link AbstractInventoryGUI#init()}.
     */
    public void updateArenas() {
        this.deinit();
        this.init();
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
