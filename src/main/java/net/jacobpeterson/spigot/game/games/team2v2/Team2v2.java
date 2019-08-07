package net.jacobpeterson.spigot.game.games.team2v2;

import net.jacobpeterson.spigot.game.games.team2v2.itemstack.Team2v2ItemStack;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team2v2 implements Initializers {

    private Team2v2Game team2v2Game;
    private Player teamLeader;
    private Player teamMember;
    private ArrayList<PvPPlayer> prospectivePlayers;
    private Team2v2ItemStack team2v2ItemStack;

    /**
     * Instantiates a new Team 2v2.
     *
     * @param team2v2Game the team 2v2 game
     */
    public Team2v2(Team2v2Game team2v2Game) {
        this.team2v2Game = team2v2Game;
        this.prospectivePlayers = new ArrayList<>();
        this.team2v2ItemStack = new Team2v2ItemStack(this);
    }

    @Override
    public void init() {
        team2v2ItemStack.init();
    }

    @Override
    public void deinit() {
        team2v2ItemStack.deinit();
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

    /**
     * Gets team 2v2 item stack.
     *
     * @return the team 2v2 item stack
     */
    public Team2v2ItemStack getTeam2v2ItemStack() {
        return team2v2ItemStack;
    }

    /**
     * Sets team 2v2 item stack.
     *
     * @param team2v2ItemStack the team 2v2 item stack
     */
    public void setTeam2v2ItemStack(Team2v2ItemStack team2v2ItemStack) {
        this.team2v2ItemStack = team2v2ItemStack;
    }
}
