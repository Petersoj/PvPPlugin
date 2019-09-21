package net.jacobpeterson.pvpplugin.player.data;

import net.jacobpeterson.pvpplugin.arena.Arena;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * A POJO for data on a PvPPlayer.
 */
public class PlayerData {

    private int elo;
    private HashMap<Arena, Integer> arenaTimesPlayed;
    private HashMap<Arena, ItemStack[]> arenaInventory;
    private int unrankedFFAKills;
    private int unrankedFFADeaths;
    private int ranked1v1Kills;
    private int ranked1v1Deaths;
    private int ranked1v1Wins;
    private int ranked1v1Losses;
    private int teamPvPWins;
    private int teamPvPLosses;

    /**
     * Instantiates a new Player data.
     */
    public PlayerData() {
        this.elo = 1000; // For first-timers
        // Instantiate so that getClass() of these objects for Gson is not null
        this.arenaTimesPlayed = new HashMap<>();
        this.arenaInventory = new HashMap<>();
    }

    /**
     * Gets ELO.
     *
     * @return the ELO
     */
    public int getELO() {
        return elo;
    }

    /**
     * Sets ELO.
     *
     * @param elo the ELO
     */
    public void setELO(int elo) {
        this.elo = elo;
    }

    /**
     * Gets arena times played map.
     *
     * @return the arena times played map
     */
    public HashMap<Arena, Integer> getArenaTimesPlayedMap() {
        return arenaTimesPlayed;
    }

    /**
     * Sets arena times played map.
     *
     * @param arenaTimesPlayed the arena times played map
     */
    public void setArenaTimesPlayedMap(HashMap<Arena, Integer> arenaTimesPlayed) {
        this.arenaTimesPlayed = arenaTimesPlayed;
    }

    /**
     * Gets arena inventory.
     *
     * @return the arena inventory
     */
    public HashMap<Arena, ItemStack[]> getArenaInventory() {
        return arenaInventory;
    }

    /**
     * Sets arena inventory.
     *
     * @param arenaInventory the arena inventory
     */
    public void setArenaInventory(HashMap<Arena, ItemStack[]> arenaInventory) {
        this.arenaInventory = arenaInventory;
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
     * Gets ranked 1v1 kills.
     *
     * @return the ranked 1v1 kills
     */
    public int getRanked1v1Kills() {
        return ranked1v1Kills;
    }

    /**
     * Sets ranked 1v1 kills.
     *
     * @param ranked1v1Kills the ranked 1v1 kills
     */
    public void setRanked1v1Kills(int ranked1v1Kills) {
        this.ranked1v1Kills = ranked1v1Kills;
    }

    /**
     * Gets ranked 1v1 deaths.
     *
     * @return the ranked 1v1 deaths
     */
    public int getRanked1v1Deaths() {
        return ranked1v1Deaths;
    }

    /**
     * Sets ranked 1v1 deaths.
     *
     * @param ranked1v1Deaths the ranked 1v1 deaths
     */
    public void setRanked1v1Deaths(int ranked1v1Deaths) {
        this.ranked1v1Deaths = ranked1v1Deaths;
    }

    /**
     * Gets ranked 1v1 wins.
     *
     * @return the ranked 1v1 wins
     */
    public int getRanked1v1Wins() {
        return ranked1v1Wins;
    }

    /**
     * Sets ranked 1v1 wins.
     *
     * @param ranked1v1Wins the ranked 1v1 wins
     */
    public void setRanked1v1Wins(int ranked1v1Wins) {
        this.ranked1v1Wins = ranked1v1Wins;
    }

    /**
     * Gets ranked 1v1 losses.
     *
     * @return the ranked 1v1 losses
     */
    public int getRanked1v1Losses() {
        return ranked1v1Losses;
    }

    /**
     * Sets ranked 1v1 losses.
     *
     * @param ranked1v1Losses the ranked 1v1 losses
     */
    public void setRanked1v1Losses(int ranked1v1Losses) {
        this.ranked1v1Losses = ranked1v1Losses;
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
