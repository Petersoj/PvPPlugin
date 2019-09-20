package net.jacobpeterson.spigot.player.game;

import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;

import java.util.AbstractMap;

public class PlayerGameManager implements Initializers {

    private PvPPlayer pvpPlayer;
    private Game currentGame;
    private AbstractMap.SimpleEntry<PvPPlayer, Object> lastDamager;

    /**
     * Instantiates a new PlayerGameManager which is used to manage Game instances that the player is involved in.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerGameManager(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
    }

    @Override
    public void init() {
    }

    @Override
    public void deinit() {
    }

    /**
     * Gets pvp player.
     *
     * @return the pvp player
     */
    public PvPPlayer getPvPPlayer() {
        return pvpPlayer;
    }

    /**
     * Gets current game.
     *
     * @return the current game
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * Sets current game.
     *
     * @param currentGame the current game
     */
    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Gets last damager.
     *
     * @return the last damager
     */
    public AbstractMap.SimpleEntry<PvPPlayer, Object> getLastDamager() {
        return lastDamager;
    }

    /**
     * Sets last damager.
     *
     * @param pvpPlayer      the pvp player
     * @param damagingObject the damaging object
     */
    public void setLastDamager(PvPPlayer pvpPlayer, Object damagingObject) {
        this.lastDamager = new AbstractMap.SimpleEntry<>(pvpPlayer, damagingObject);
    }
}
