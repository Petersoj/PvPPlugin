package net.jacobpeterson.pvpplugin.player.gui;

import net.jacobpeterson.pvpplugin.game.GameManager;
import net.jacobpeterson.pvpplugin.gui.AbstractInventoryGUI;
import net.jacobpeterson.pvpplugin.gui.InventoryReferenceMatcher;
import net.jacobpeterson.pvpplugin.gui.guis.ranked1v1.Ranked1v1Menu;
import net.jacobpeterson.pvpplugin.gui.guis.team2v2.Team2v2ArenaMenu;
import net.jacobpeterson.pvpplugin.gui.guis.team2v2.Team2v2CraftTeamMenu;
import net.jacobpeterson.pvpplugin.gui.guis.team2v2.Team2v2Menu;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.inventory.Inventory;

public class PlayerGUIManager implements Initializers, InventoryReferenceMatcher {

    private final PvPPlayer pvpPlayer;
    private Ranked1v1Menu ranked1v1Menu;
    private Team2v2Menu team2v2Menu;
    private Team2v2ArenaMenu team2v2ArenaMenu;
    private Team2v2CraftTeamMenu team2v2CraftTeamMenu;

    /**
     * Instantiates a new Player GUI Manager which is used to handle player-specific Inventory GUIs.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerGUIManager(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
        this.ranked1v1Menu = new Ranked1v1Menu(this);
        this.team2v2Menu = new Team2v2Menu(this);
        this.team2v2ArenaMenu = new Team2v2ArenaMenu(this);
        this.team2v2CraftTeamMenu = new Team2v2CraftTeamMenu(this);
    }

    @Override
    public void init() {
        ranked1v1Menu.init();
        team2v2Menu.init();
        team2v2ArenaMenu.init();
        team2v2CraftTeamMenu.init();

        this.updateArenaItemStackInventories(pvpPlayer.getPlayerManager().getPvPPlugin().getGameManager());
    }

    @Override
    public void deinit() {
        ranked1v1Menu.deinit();
        team2v2Menu.deinit();
        team2v2ArenaMenu.deinit();
        team2v2CraftTeamMenu.deinit();
    }

    /**
     * Updates inventories containing {@link net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack}.
     *
     * @param gameManager the game manager
     */
    public void updateArenaItemStackInventories(GameManager gameManager) {
        ranked1v1Menu.updateArenaItemStacks(gameManager.getRanked1v1GameQueueMap(), pvpPlayer);
        team2v2ArenaMenu.updateArenaItemStacks(gameManager.getTeam2v2GameQueueMap(), pvpPlayer);
    }

    @Override
    public AbstractInventoryGUI getInventoryGUI(Inventory inventory) {
        if (ranked1v1Menu.getInventory().equals(inventory)) return ranked1v1Menu;
        if (team2v2Menu.getInventory().equals(inventory)) return ranked1v1Menu;
        if (team2v2ArenaMenu.getInventory().equals(inventory)) return team2v2ArenaMenu;
        if (team2v2CraftTeamMenu.getInventory().equals(inventory)) return team2v2ArenaMenu;
        return null;
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
     * Gets team 2v2 menu.
     *
     * @return the team 2v2 menu
     */
    public Team2v2Menu getTeam2v2Menu() {
        return team2v2Menu;
    }

    /**
     * Sets team 2v2 menu.
     *
     * @param team2v2Menu the team 2v2 menu
     */
    public void setTeam2v2Menu(Team2v2Menu team2v2Menu) {
        this.team2v2Menu = team2v2Menu;
    }

    /**
     * Gets team 2v2 arena menu.
     *
     * @return the team 2v2 arena menu
     */
    public Team2v2ArenaMenu getTeam2v2ArenaMenu() {
        return team2v2ArenaMenu;
    }

    /**
     * Sets team 2v2 arena menu.
     *
     * @param team2v2ArenaMenu the team 2v2 arena menu
     */
    public void setTeam2v2ArenaMenu(Team2v2ArenaMenu team2v2ArenaMenu) {
        this.team2v2ArenaMenu = team2v2ArenaMenu;
    }

    /**
     * Gets team 2v2 craft team menu.
     *
     * @return the team 2v2 craft team menu
     */
    public Team2v2CraftTeamMenu getTeam2v2CraftTeamMenu() {
        return team2v2CraftTeamMenu;
    }

    /**
     * Sets team 2v2 craft team menu.
     *
     * @param team2v2CraftTeamMenu the team 2v2 craft team menu
     */
    public void setTeam2v2CraftTeamMenu(Team2v2CraftTeamMenu team2v2CraftTeamMenu) {
        this.team2v2CraftTeamMenu = team2v2CraftTeamMenu;
    }
}
