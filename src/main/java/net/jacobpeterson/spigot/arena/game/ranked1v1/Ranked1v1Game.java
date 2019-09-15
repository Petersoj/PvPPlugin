package net.jacobpeterson.spigot.arena.game.ranked1v1;

import net.jacobpeterson.spigot.arena.arenas.Team2v2Arena;
import net.jacobpeterson.spigot.arena.game.Game;
import net.jacobpeterson.spigot.util.Initializers;

public class Ranked1v1Game extends Game implements Initializers {

    /**
     * Instantiates a new Ranked 1v1 Game.
     *
     * @param team2v2Arena the team 2v2 arena
     */
    public Ranked1v1Game(Team2v2Arena team2v2Arena) {
        super(team2v2Arena);
    }

    @Override
    public void init() {

    }

    @Override
    public void deinit() {

    }
}
