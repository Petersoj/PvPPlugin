package net.jacobpeterson.spigot.player.game;

import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;

public class PlayerGameManager implements Initializers {

    private PvPPlayer pvpPlayer;
    private Game currentGame;

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
}
