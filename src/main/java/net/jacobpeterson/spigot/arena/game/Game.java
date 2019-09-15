package net.jacobpeterson.spigot.arena.game;

import net.jacobpeterson.spigot.arena.Arena;

public abstract class Game {

    protected Arena arena;

    /**
     * Instantiates a new Game which represents an instance of a game in an Arena.
     *
     * @param arena the arena
     */
    public Game(Arena arena) {
        this.arena = arena;
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
