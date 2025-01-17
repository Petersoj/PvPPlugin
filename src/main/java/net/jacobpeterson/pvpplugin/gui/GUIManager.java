package net.jacobpeterson.pvpplugin.gui;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.gui.guis.main.MainMenu;
import net.jacobpeterson.pvpplugin.gui.event.InventoryGUIEventHandlers;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.inventory.Inventory;

public class GUIManager implements Initializers, InventoryReferenceMatcher {

    private final PvPPlugin pvpPlugin;
    private InventoryGUIEventHandlers inventoryGUIEventHandlers;
    private MainMenu mainMenu; // A global, non-player-specific GUI

    /**
     * Instantiates a new GUI manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GUIManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.inventoryGUIEventHandlers = new InventoryGUIEventHandlers(this);
        this.mainMenu = new MainMenu(this, pvpPlugin.getPlayerManager(), pvpPlugin.getGameManager());
    }

    @Override
    public void init() {
        mainMenu.init();
    }

    @Override
    public void deinit() {
        mainMenu.deinit();
    }

    @Override
    public AbstractInventoryGUI getInventoryGUI(Inventory inventory) {
        if (mainMenu.getInventory().equals(inventory)) return mainMenu;
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
