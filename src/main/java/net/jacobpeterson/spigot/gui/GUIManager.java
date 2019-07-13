package net.jacobpeterson.spigot.gui;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.gui.guis.MainMenu;
import net.jacobpeterson.spigot.util.Initializers;

public class GUIManager implements Initializers {

    private final PvPPlugin pvpPlugin;
    private MainMenu mainMenu;

    /**
     * Instantiates a new Gui manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GUIManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
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
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PvPPlugin getPvPPlugin() {
        return pvpPlugin;
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
