package net.jacobpeterson.spigot.game.games.ranked1v1;

import net.jacobpeterson.spigot.arena.arenas.Team2v2Arena;
import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.game.GameManager;
import net.jacobpeterson.spigot.util.Initializers;

public class Ranked1v1Game extends Game implements Initializers {

    /**
     * Instantiates a new Ranked 1v1 Game.
     *
     * @param gameManager  the game manager
     * @param team2v2Arena the team 2v2 arena
     */
    public Ranked1v1Game(GameManager gameManager, Team2v2Arena team2v2Arena) {
        super(gameManager, team2v2Arena);
    }

    @Override
    public void init() {

    }

    @Override
    public void deinit() {

    }
}
