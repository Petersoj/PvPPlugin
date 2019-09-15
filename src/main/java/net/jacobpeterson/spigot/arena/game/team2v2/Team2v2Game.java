package net.jacobpeterson.spigot.arena.game.team2v2;

import net.jacobpeterson.spigot.arena.arenas.Team2v2Arena;
import net.jacobpeterson.spigot.arena.game.Game;
import net.jacobpeterson.spigot.util.Initializers;

public class Team2v2Game extends Game implements Initializers {

    private Team2v2 blueTeam2v2;
    private Team2v2 redTeam2v2;

    /**
     * Instantiates a new Team 2v2 Game.
     *
     * @param team2v2Arena the team 2v2 arena
     */
    public Team2v2Game(Team2v2Arena team2v2Arena) {
        super(team2v2Arena);
    }

    @Override
    public void init() {

    }

    @Override
    public void deinit() {
    }
}