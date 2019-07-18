package net.jacobpeterson.spigot.data;

import net.jacobpeterson.spigot.gamemode.Arena;
import net.jacobpeterson.spigot.player.PvPPlayer;

import java.util.HashMap;

/**
 * A POJO for data on a PvPPlayer.
 */
public class PlayerData {

    private transient final PvPPlayer pvpPlayer; // transient = don't serialize
    private int ELO;
    private HashMap<Arena, Integer> arenaTimesPlayed;
    private int unrankedFFAKills;
    private int unrankedFFADeaths;
    private int ranked1v1Kills;
    private int ranked1v1Deaths;
    private int teamPvPWins;
    private int teamPvPLosses;

    /**
     * Instantiates a new Player data.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerData(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
        this.arenaTimesPlayed = new HashMap<>();
    }

    /**
     * Gets pvp player.
     *
     * @return the pvp player
     */
    public PvPPlayer getPvPPlayer() {
        return pvpPlayer;
    }

    /**
     * Gets elo.
     *
     * @return the elo
     */
    public int getELO() {
        return ELO;
    }

    /**
     * Sets elo.
     *
     * @param ELO the elo
     */
    public void setELO(int ELO) {
        this.ELO = ELO;
    }

    /**
     * Gets arena times played.
     *
     * @return the arena times played
     */
    public HashMap<Arena, Integer> getArenaTimesPlayed() {
        return arenaTimesPlayed;
    }

    /**
     * Sets arena times played.
     *
     * @param arenaTimesPlayed the arena times played
     */
    public void setArenaTimesPlayed(HashMap<Arena, Integer> arenaTimesPlayed) {
        this.arenaTimesPlayed = arenaTimesPlayed;
    }

    /**
     * Gets unranked ffa kills.
     *
     * @return the unranked ffa kills
     */
    public int getUnrankedFFAKills() {
        return unrankedFFAKills;
    }

    /**
     * Sets unranked ffa kills.
     *
     * @param unrankedFFAKills the unranked ffa kills
     */
    public void setUnrankedFFAKills(int unrankedFFAKills) {
        this.unrankedFFAKills = unrankedFFAKills;
    }

    /**
     * Gets unranked ffa deaths.
     *
     * @return the unranked ffa deaths
     */
    public int getUnrankedFFADeaths() {
        return unrankedFFADeaths;
    }

    /**
     * Sets unranked ffa deaths.
     *
     * @param unrankedFFADeaths the unranked ffa deaths
     */
    public void setUnrankedFFADeaths(int unrankedFFADeaths) {
        this.unrankedFFADeaths = unrankedFFADeaths;
    }

    /**
     * Gets ranked 1 v 1 kills.
     *
     * @return the ranked 1 v 1 kills
     */
    public int getRanked1v1Kills() {
        return ranked1v1Kills;
    }

    /**
     * Sets ranked 1 v 1 kills.
     *
     * @param ranked1v1Kills the ranked 1 v 1 kills
     */
    public void setRanked1v1Kills(int ranked1v1Kills) {
        this.ranked1v1Kills = ranked1v1Kills;
    }

    /**
     * Gets ranked 1 v 1 deaths.
     *
     * @return the ranked 1 v 1 deaths
     */
    public int getRanked1v1Deaths() {
        return ranked1v1Deaths;
    }

    /**
     * Sets ranked 1 v 1 deaths.
     *
     * @param ranked1v1Deaths the ranked 1 v 1 deaths
     */
    public void setRanked1v1Deaths(int ranked1v1Deaths) {
        this.ranked1v1Deaths = ranked1v1Deaths;
    }

    /**
     * Gets team pvp wins.
     *
     * @return the team pvp wins
     */
    public int getTeamPvPWins() {
        return teamPvPWins;
    }

    /**
     * Sets team pvp wins.
     *
     * @param teamPvPWins the team pvp wins
     */
    public void setTeamPvPWins(int teamPvPWins) {
        this.teamPvPWins = teamPvPWins;
    }

    /**
     * Gets team pvp losses.
     *
     * @return the team pvp losses
     */
    public int getTeamPvPLosses() {
        return teamPvPLosses;
    }

    /**
     * Sets team pvp losses.
     *
     * @param teamPvPLosses the team pvp losses
     */
    public void setTeamPvPLosses(int teamPvPLosses) {
        this.teamPvPLosses = teamPvPLosses;
    }
}
