package net.jacobpeterson.spigot.data;

import org.bukkit.entity.Player;

public class PlayerData {

    private final Player player;
    private int ELO;

    /**
     * Instantiates a new Player data.
     *
     * @param player the player
     */
    public PlayerData(Player player) {
        this.player = player;
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
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
}
