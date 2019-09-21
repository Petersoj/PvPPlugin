package net.jacobpeterson.pvpplugin.player.game;

import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.Initializers;

public class PlayerGameManager implements Initializers {

    private PvPPlayer pvpPlayer;
    private Game currentGame;
    private DamageTracker damageTracker;

    /**
     * Instantiates a new PlayerGameManager which is used to manage Game instances that the player is involved in.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerGameManager(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
        this.damageTracker = new DamageTracker();
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
     * Gets damage tracker.
     *
     * @return the damage tracker
     */
    public DamageTracker getDamageTracker() {
        return damageTracker;
    }
}
