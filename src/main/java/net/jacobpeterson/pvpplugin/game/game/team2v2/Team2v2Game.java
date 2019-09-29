package net.jacobpeterson.pvpplugin.game.game.team2v2;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.Team2v2Arena;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.GameManager;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.Initializers;

import java.util.Queue;

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
        super(gameManager, team2v2Arena, "Team 2v2");
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

    /**
     * Gets player Team2v2.
     *
     * @param pvpPlayer the pvp player
     * @return the player team
     */
    public Team2v2 getPlayerTeam(PvPPlayer pvpPlayer) {
        if (blueTeam2v2 != null &&
                (blueTeam2v2.getTeamLeader().equals(pvpPlayer) || blueTeam2v2.getTeamMember().equals(pvpPlayer))) {
            return blueTeam2v2;
        }
        if (redTeam2v2 != null &&
                (redTeam2v2.getTeamLeader().equals(pvpPlayer) || redTeam2v2.getTeamMember().equals(pvpPlayer))) {
            return redTeam2v2;
        }
        return null;
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

    /**
     * Gets blue team 2v2.
     *
     * @return the blue team 2v2
     */
    public Team2v2 getBlueTeam2v2() {
        return blueTeam2v2;
    }

    /**
     * Sets blue team 2v2.
     *
     * @param blueTeam2v2 the blue team 2v2
     */
    public void setBlueTeam2v2(Team2v2 blueTeam2v2) {
        this.blueTeam2v2 = blueTeam2v2;
    }

    /**
     * Gets red team 2v2.
     *
     * @return the red team 2v2
     */
    public Team2v2 getRedTeam2v2() {
        return redTeam2v2;
    }

    /**
     * Sets red team 2v2.
     *
     * @param redTeam2v2 the red team 2v2
     */
    public void setRedTeam2v2(Team2v2 redTeam2v2) {
        this.redTeam2v2 = redTeam2v2;
    }
}
