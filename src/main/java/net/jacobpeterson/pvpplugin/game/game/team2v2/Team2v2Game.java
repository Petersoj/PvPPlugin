package net.jacobpeterson.pvpplugin.game.game.team2v2;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.Team2v2Arena;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.GameManager;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.Initializers;

public class Team2v2Game extends Game implements Initializers {

    private Team2v2 blueTeam2v2;
    private Team2v2 redTeam2v2;

    /**
     * Instantiates a new Team 2v2 Game which serves as a game instance for the Arena.
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

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void join(PvPPlayer pvpPlayer) {
        super.join(pvpPlayer);
    }

    @Override
    public void leave(PvPPlayer pvpPlayer) {
        super.leave(pvpPlayer);
    }

    @Override
    public Team2v2Arena getArena() {
        return (Team2v2Arena) arena;
    }

    @Override
    public void setArena(Arena arena) {
        if (arena != null && !(arena instanceof Team2v2Arena)) {
            throw new IllegalArgumentException("Arena must be a Team2v2Arena!");
        }
        this.arena = arena;
    }
}
