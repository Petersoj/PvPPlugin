package net.jacobpeterson.spigot.player;

import net.jacobpeterson.spigot.player.data.PlayerData;
import net.jacobpeterson.spigot.player.gui.PlayerGUIManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.entity.Player;

public class PvPPlayer implements Initializers {

    private final PlayerManager playerManager;
    private final Player player;
    private PlayerData playerData;
    private PlayerGUIManager playerGUIManager;

    /**
     * Instantiates a new PvP Player which is like a pseudo super-class to {@link Player}.
     *
     * @param playerManager the player manager
     * @param player        the player
     */
    // Package-private so that no instance of this are created without the PlayerManager knowing
    PvPPlayer(PlayerManager playerManager, Player player) {
        this.playerManager = playerManager;
        this.player = player;
        this.playerGUIManager = new PlayerGUIManager(this);
    }

    @Override
    public void init() {
        playerGUIManager.init();
    }

    @Override
    public void deinit() {
        playerGUIManager.deinit();
    }

    /**
     * Gets player manager.
     *
     * @return the player manager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
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

    /**
     * Gets player gui manager.
     *
     * @return the player gui manager
     */
    public PlayerGUIManager getPlayerGUIManager() {
        return playerGUIManager;
    }

    /**
     * Sets player gui manager.
     *
     * @param playerGUIManager the player gui manager
     */
    public void setPlayerGUIManager(PlayerGUIManager playerGUIManager) {
        this.playerGUIManager = playerGUIManager;
    }
}
