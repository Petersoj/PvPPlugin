package net.jacobpeterson.spigot.player.gui;

import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.gui.InventoryReferenceMatcher;
import net.jacobpeterson.spigot.gui.GUIManager;
import net.jacobpeterson.spigot.gui.guis.ranked1v1.Ranked1v1Menu;
import net.jacobpeterson.spigot.gui.guis.teampvp.TeamPvPArenaMenu;
import net.jacobpeterson.spigot.gui.guis.teampvp.TeamPvPCraftTeamMenu;
import net.jacobpeterson.spigot.gui.guis.teampvp.TeamPvPMenu;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.inventory.Inventory;

public class PlayerGUIManager implements Initializers, InventoryReferenceMatcher {

    private final PvPPlayer pvpPlayer;
    private GUIManager guiManager;
    private Ranked1v1Menu ranked1v1Menu;
    private TeamPvPMenu teamPvPMenu;
    private TeamPvPArenaMenu teamPvPArenaMenu;
    private TeamPvPCraftTeamMenu teamPvPCraftTeamMenu;

    /**
     * Instantiates a new Player GUI Manager which is used to handle player-specific Inventory GUIs.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerGUIManager(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
        this.ranked1v1Menu = new Ranked1v1Menu(this);
        this.teamPvPMenu = new TeamPvPMenu(this);
        this.teamPvPArenaMenu = new TeamPvPArenaMenu(this);
        this.teamPvPCraftTeamMenu = new TeamPvPCraftTeamMenu(this);
    }

    @Override
    public void init() {
        this.guiManager = pvpPlayer.getPlayerManager().getPvPPlugin().getGUIManager();

        ranked1v1Menu.init();
        teamPvPMenu.init();
        teamPvPArenaMenu.init();
        teamPvPCraftTeamMenu.init();
    }

    @Override
    public void deinit() {
    }

    @Override
    public AbstractInventoryGUI getInventoryGUI(Inventory inventory) {
        if (ranked1v1Menu.getInventory().equals(inventory)) return ranked1v1Menu;
        if (teamPvPMenu.getInventory().equals(inventory)) return ranked1v1Menu;
        if (teamPvPArenaMenu.getInventory().equals(inventory)) return teamPvPArenaMenu;
        if (teamPvPCraftTeamMenu.getInventory().equals(inventory)) return teamPvPArenaMenu;
        return null;
    }

    /**
     * Gets GUI manager.
     *
     * @return the GUI manager
     */
    public GUIManager getGUIManager() {
        return guiManager;
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
     * Sets ranked 1v1 menu.
     *
     * @param ranked1v1Menu the ranked 1v1 menu
     */
    public void setRanked1v1Menu(Ranked1v1Menu ranked1v1Menu) {
        this.ranked1v1Menu = ranked1v1Menu;
    }

    /**
     * Gets team pvp menu.
     *
     * @return the team pvp menu
     */
    public TeamPvPMenu getTeamPvPMenu() {
        return teamPvPMenu;
    }

    /**
     * Sets team pvp menu.
     *
     * @param teamPvPMenu the team pvp menu
     */
    public void setTeamPvPMenu(TeamPvPMenu teamPvPMenu) {
        this.teamPvPMenu = teamPvPMenu;
    }

    /**
     * Gets team pvp arena menu.
     *
     * @return the team pvp arena menu
     */
    public TeamPvPArenaMenu getTeamPvPArenaMenu() {
        return teamPvPArenaMenu;
    }

    /**
     * Sets team pvp arena menu.
     *
     * @param teamPvPArenaMenu the team pvp arena menu
     */
    public void setTeamPvPArenaMenu(TeamPvPArenaMenu teamPvPArenaMenu) {
        this.teamPvPArenaMenu = teamPvPArenaMenu;
    }

    /**
     * Gets team pvp craft team menu.
     *
     * @return the team pvp craft team menu
     */
    public TeamPvPCraftTeamMenu getTeamPvPCraftTeamMenu() {
        return teamPvPCraftTeamMenu;
    }

    /**
     * Sets team pvp craft team menu.
     *
     * @param teamPvPCraftTeamMenu the team pvp craft team menu
     */
    public void setTeamPvPCraftTeamMenu(TeamPvPCraftTeamMenu teamPvPCraftTeamMenu) {
        this.teamPvPCraftTeamMenu = teamPvPCraftTeamMenu;
    }
}
