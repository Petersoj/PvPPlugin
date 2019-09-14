package net.jacobpeterson.spigot.player;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.data.PlayerData;
import net.jacobpeterson.spigot.player.gamemode.PlayerGameManager;
import net.jacobpeterson.spigot.player.gui.PlayerGUIManager;
import net.jacobpeterson.spigot.player.item.PlayerItemManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.entity.Player;

public class PvPPlayer implements Initializers {

    private final PlayerManager playerManager;
    private final Player player;
    private PlayerGameManager playerGameManager;
    private PlayerGUIManager playerGUIManager;
    private PlayerItemManager playerItemManager;
    private PlayerData playerData;

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
        this.playerGameManager = new PlayerGameManager(this);
        this.playerGUIManager = new PlayerGUIManager(this);
        this.playerItemManager = new PlayerItemManager(this);
    }

    @Override
    public void init() {
        playerGameManager.init();
        playerGUIManager.init();
        playerItemManager.init();
    }

    @Override
    public void deinit() {
        playerGameManager.deinit();
        playerGUIManager.deinit();
        playerItemManager.deinit();
    }

    /**
     * Gets {@link PvPPlugin#getPermissionsEx()} prefixed name.
     *
     * @return the prefixed name
     */
    public String getPrefixedName() {
        return playerManager.getPlayerGroupPrefix(player.getName()) + player.getName();
    }

    /**
     * {@link PlayerManager#isPlayerPremium(Player)}
     *
     * @return the boolean
     */
    public boolean isPremium() {
        return playerManager.isPlayerPremium(this.getPlayer());
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
     * Gets player game manager.
     *
     * @return the player game manager
     */
    public PlayerGameManager getPlayerGameManager() {
        return playerGameManager;
    }

    /**
     * Sets player gamemode manager.
     *
     * @param playerGameManager the player gamemode manager
     */
    public void setPlayerGameManager(PlayerGameManager playerGameManager) {
        this.playerGameManager = playerGameManager;
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

    /**
     * Gets player item manager.
     *
     * @return the player item manager
     */
    public PlayerItemManager getPlayerItemManager() {
        return playerItemManager;
    }

    /**
     * Sets player item manager.
     *
     * @param playerItemManager the player item manager
     */
    public void setPlayerItemManager(PlayerItemManager playerItemManager) {
        this.playerItemManager = playerItemManager;
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
