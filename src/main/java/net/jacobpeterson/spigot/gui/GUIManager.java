package net.jacobpeterson.spigot.gui;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.gui.guis.MainMenu;
import net.jacobpeterson.spigot.gui.listener.InventoryGUIEventHandlers;
import net.jacobpeterson.spigot.player.gui.PlayerGUIManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.inventory.Inventory;

public class GUIManager implements Initializers {

    private final PvPPlugin pvpPlugin;
    private InventoryGUIEventHandlers inventoryGUIEventHandlers;
    private MainMenu mainMenu;

    /**
     * Instantiates a new GUI manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GUIManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.inventoryGUIEventHandlers = new InventoryGUIEventHandlers(this);
        this.mainMenu = new MainMenu(this);
    }

    @Override
    public void init() {
        mainMenu.init();
    }

    @Override
    public void deinit() {
        mainMenu.deinit();
    }

    /**
     * Gets the {@link AbstractInventoryGUI} associated with the passed in Bukkit Inventory (if exists).
     *
     * @param inventory        the inventory
     * @param playerGUIManager the player GUI manager
     * @return the {@link AbstractInventoryGUI} (null if nothing was matched)
     */
    public AbstractInventoryGUI getInventoryGUI(Inventory inventory, PlayerGUIManager playerGUIManager) {
        // TODO search instantiated inventories
        return null;
    }

    /**
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PvPPlugin getPvPPlugin() {
        return pvpPlugin;
    }

    /**
     * Gets inventory gui event handlers.
     *
     * @return the inventory gui event handlers
     */
    public InventoryGUIEventHandlers getInventoryGUIEventHandlers() {
        return inventoryGUIEventHandlers;
    }

    /**
     * Gets main menu.
     *
     * @return the main menu
     */
    public MainMenu getMainMenu() {
        return mainMenu;
    }
}
