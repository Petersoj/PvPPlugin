package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.gamemode.Arena;
import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import net.jacobpeterson.spigot.util.Initializers;
import net.jacobpeterson.spigot.util.ItemStackUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public abstract class ChooseArenaMenu extends AbstractInventoryGUI implements Initializers {

    public static final Logger LOGGER = LogManager.getLogger();

    protected GUIManager guiManager;
    protected ItemStack anyItem;
    protected ItemStack backItem;
    protected ArrayList<Arena> arenas;

    /**
     * Instantiates a new 'Choose Arena' menu. This is meant to be extended by other GUI/menus as this is a general viewer.
     *
     * @param guiManager the gui manager
     */
    public ChooseArenaMenu(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.arenas = new ArrayList<>();
    }

    @Override
    public void init() {
        int chestLines = (int) Math.ceil((arenas.size() == 0 ? 5 : arenas.size()) / 5); // 5 Arena ItemStacks per chest row
        if (chestLines > 6) {
            throw new UnsupportedOperationException("Pagination not implemented! Too many arenas created!");
        }
        inventory = Bukkit.createInventory(null, chestLines * 9,
                ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Ranked 1v1");

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
    }

    @Override
    public void deinit() {

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
