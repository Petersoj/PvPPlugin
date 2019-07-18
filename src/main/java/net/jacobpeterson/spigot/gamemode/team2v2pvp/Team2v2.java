package net.jacobpeterson.spigot.gamemode.team2v2pvp;

import net.jacobpeterson.spigot.player.PvPPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team2v2 {

    private Player teamLeader;
    private Player teamMember;
    private ArrayList<PvPPlayer> prospectivePlayers;

    /**
     * Instantiates a new Team 2v2.
     */
    public Team2v2() {
        this.prospectivePlayers = new ArrayList<>();
    }

    /**
     * Instantiates a new Team 2v2.
     *
     * @param teamLeader the team leader
     */
    public Team2v2(Player teamLeader) {
        this();
        this.teamLeader = teamLeader;
    }

    /**
     * Gets team leader.
     *
     * @return the team leader
     */
    public Player getTeamLeader() {
        return teamLeader;
    }

    /**
     * Sets team leader.
     *
     * @param teamLeader the team leader
     */
    public void setTeamLeader(Player teamLeader) {
        this.teamLeader = teamLeader;
    }

    /**
     * Gets team member.
     *
     * @return the team member
     */
    public Player getTeamMember() {
        return teamMember;
    }

    /**
     * Sets team member.
     *
     * @param teamMember the team member
     */
    public void setTeamMember(Player teamMember) {
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
