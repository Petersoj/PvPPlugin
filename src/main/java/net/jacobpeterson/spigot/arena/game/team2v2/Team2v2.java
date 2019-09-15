package net.jacobpeterson.spigot.arena.game.team2v2;

import net.jacobpeterson.spigot.player.PvPPlayer;

import java.util.ArrayList;

public class Team2v2 {

    private Team2v2Game team2v2Game;
    private PvPPlayer teamLeader;
    private PvPPlayer teamMember;
    private ArrayList<PvPPlayer> prospectivePlayers;

    /**
     * Instantiates a new Team 2v2.
     *
     * @param team2v2Game the team 2v2 game
     */
    public Team2v2(Team2v2Game team2v2Game) {
        this.team2v2Game = team2v2Game;
        this.prospectivePlayers = new ArrayList<>();
    }

    /**
     * Gets team 2v2 game.
     *
     * @return the team 2v2 game
     */
    public Team2v2Game getTeam2v2Game() {
        return team2v2Game;
    }

    /**
     * Gets team leader.
     *
     * @return the team leader
     */
    public PvPPlayer getTeamLeader() {
        return teamLeader;
    }

    /**
     * Sets team leader.
     *
     * @param teamLeader the team leader
     */
    public void setTeamLeader(PvPPlayer teamLeader) {
        this.teamLeader = teamLeader;
    }

    /**
     * Gets team member.
     *
     * @return the team member
     */
    public PvPPlayer getTeamMember() {
        return teamMember;
    }

    /**
     * Sets team member.
     *
     * @param teamMember the team member
     */
    public void setTeamMember(PvPPlayer teamMember) {
        this.teamMember = teamMember;
    }

    /**
     * Gets prospective players.
     *
     * @return the prospective players
     */
    public ArrayList<PvPPlayer> getProspectivePlayers() {
        return prospectivePlayers;
    }

    /**
     * Sets prospective players.
     *
     * @param prospectivePlayers the prospective players
     */
    public void setProspectivePlayers(ArrayList<PvPPlayer> prospectivePlayers) {
        this.prospectivePlayers = prospectivePlayers;
    }
}
