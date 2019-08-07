package net.jacobpeterson.spigot.game;

import net.jacobpeterson.spigot.arena.Arena;

public abstract class Game {

    protected GameManager gameManager;
    protected Arena arena;

    /**
     * Instantiates a new Game.
     *
     * @param gameManager the game manager
     * @param arena       the arena
     */
    public Game(GameManager gameManager, Arena arena) {
        this.gameManager = gameManager;
        this.arena = arena;
    }

    /**
     * Gets game manager.
     *
     * @return the game manager
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Sets game manager.
     *
     * @param gameManager the game manager
     */
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Gets arena.
     *
     * @return the arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Sets arena.
     *
     * @param arena the arena
     */
    public void setArena(Arena arena) {
        this.arena = arena;
    }
}
