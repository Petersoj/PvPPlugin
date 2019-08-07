package net.jacobpeterson.spigot.game.games.team2v2;

import net.jacobpeterson.spigot.arena.arenas.Team2v2Arena;
import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.game.GameManager;
import net.jacobpeterson.spigot.util.Initializers;

public class Team2v2Game extends Game implements Initializers {

    private Team2v2 blueTeam2v2;
    private Team2v2 redTeam2v2;

    /**
     * Instantiates a new Team 2v2 Game.
     *
     * @param gameManager  the game manager
     * @param team2v2Arena the team 2v2 arena
     */
    public Team2v2Game(GameManager gameManager, Team2v2Arena team2v2Arena) {
        super(gameManager, team2v2Arena);
    }

    @Override
    public void init() {

    }

    @Override
    public void deinit() {
    }
}
