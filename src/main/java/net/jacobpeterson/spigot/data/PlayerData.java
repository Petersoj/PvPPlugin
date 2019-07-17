package net.jacobpeterson.spigot.data;

import net.jacobpeterson.spigot.gamemode.Arena;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerData {

    private final Player player;
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
     * @param player the player
     */
    public PlayerData(Player player) {
        this.player = player;
        this.arenaTimesPlayed = new HashMap<>();
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
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

}
