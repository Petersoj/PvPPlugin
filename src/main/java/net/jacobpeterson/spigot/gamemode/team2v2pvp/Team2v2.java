package net.jacobpeterson.spigot.gamemode.team2v2pvp;

import org.bukkit.entity.Player;

public class Team2v2 {

    private Player teamLeader;
    private Player teamMember;

    /**
     * Instantiates a new Team 2v2.
     */
    public Team2v2() {
    }

    /**
     * Instantiates a new Team 2v2.
     *
     * @param teamLeader the team leader
     */
    public Team2v2(Player teamLeader) {
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
}
