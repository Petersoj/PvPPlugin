package net.jacobpeterson.spigot.player;

import net.jacobpeterson.spigot.data.PlayerData;
import org.bukkit.entity.Player;

public class PvPPlayer {

    private final Player player;
    private PlayerData playerData;

    /**
     * Instantiates a new PvP Player which is like a pseudo super-class to {@link Player}.
     *
     * @param player the player
     */
    public PvPPlayer(Player player) {
        this.player = player;
        this.playerData = new PlayerData(this);
    }

    /**
     * Instantiates a new PvP Player with PlayerData which is like a pseudo super-class to {@link Player}.
     *
     * @param player     the player
     * @param playerData the player data
     */
    public PvPPlayer(Player player, PlayerData playerData) {
        this.player = player;
        this.playerData = playerData;
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
     * Gets player data.
     *
     * @return the player data
     */
    public PlayerData getPlayerData() {
        return playerData;
    }

    /**
     * Sets player data.
     *
     * @param playerData the player data
     */
    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }
}
