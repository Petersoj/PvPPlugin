package net.jacobpeterson.spigot.gui;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.gui.guis.MainMenu;
import net.jacobpeterson.spigot.gui.guis.ranked1v1.Ranked1v1Menu;
import net.jacobpeterson.spigot.gui.guis.teampvp.TeamPvPMenu;
import net.jacobpeterson.spigot.gui.listener.InventoryGUIEventHandlers;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.inventory.Inventory;

public class GUIManager implements Initializers {

    private final PvPPlugin pvpPlugin;
    private InventoryGUIEventHandlers inventoryGUIEventHandlers;
    private MainMenu mainMenu;
    private Ranked1v1Menu ranked1v1Menu;
    private TeamPvPMenu teamPvPMenu;

    /**
     * Instantiates a new GUI manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GUIManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.inventoryGUIEventHandlers = new InventoryGUIEventHandlers(this);
        this.mainMenu = new MainMenu(this);
        this.ranked1v1Menu = new Ranked1v1Menu(this);
        this.teamPvPMenu = new TeamPvPMenu(this);
    }

    @Override
    public void init() {
        mainMenu.init();
        ranked1v1Menu.init();
        teamPvPMenu.init();
    }

    @Override
    public void deinit() {
        mainMenu.deinit();
        ranked1v1Menu.deinit();
        teamPvPMenu.deinit();
    }

    /**
     * Gets the {@link AbstractInventoryGUI} associated with the passed in Bukkit Inventory (if exists).
     *
     * @param inventory the inventory
     * @return the {@link AbstractInventoryGUI} (null if nothing was matched)
     */
    public AbstractInventoryGUI getInventoryByReference(Inventory inventory) {
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

    /**
     * Gets ranked 1v1 menu.
     *
     * @return the ranked 1v1 menu
     */
    public Ranked1v1Menu getRanked1v1Menu() {
        return ranked1v1Menu;
    }

    /**
     * Gets team pvp menu.
     *
     * @return the team pvp menu
     */
    public TeamPvPMenu getTeamPvPMenu() {
        return teamPvPMenu;
    }
}
