package net.jacobpeterson.pvpplugin.player;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.gui.GUIManager;
import net.jacobpeterson.pvpplugin.player.data.PlayerData;
import net.jacobpeterson.pvpplugin.player.game.PlayerGameManager;
import net.jacobpeterson.pvpplugin.player.gui.PlayerGUIManager;
import net.jacobpeterson.pvpplugin.player.inventory.PlayerInventoryManager;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.entity.Player;

public class PvPPlayer implements Initializers {

    private final PlayerManager playerManager;
    private final Player player;
    private PlayerGameManager playerGameManager;
    private PlayerGUIManager playerGUIManager;
    private PlayerInventoryManager playerInventoryManager;
    private PlayerData playerData;

    /**
     * Instantiates a new PvP Player which is like a pseudo super-class to {@link Player}.
     *
     * @param playerManager the player manager
     * @param player        the player
     */
    // Package-private so that no instance of this are created without the PlayerManager knowing
    PvPPlayer(PlayerManager playerManager, Player player) {
        PvPPlugin pvpPlugin = playerManager.getPvPPlugin();
        GUIManager guiManager = pvpPlugin.getGUIManager();

        this.playerManager = playerManager;
        this.player = player;
        this.playerGameManager = new PlayerGameManager(this);
        this.playerGUIManager = new PlayerGUIManager(guiManager, this);
        this.playerInventoryManager = new PlayerInventoryManager(this);
    }

    @Override
    public void init() {
        playerGameManager.init();
        playerGUIManager.init();
        playerInventoryManager.init();
    }

    @Override
    public void deinit() {
        playerGameManager.deinit();
        playerGUIManager.deinit();
        playerInventoryManager.deinit();
    }

    /**
     * Gets {@link ru.tehkode.permissions.bukkit.PermissionsEx} prefixed name.
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
     * Has elevated privileges boolean.
     * Basically just check is the player has the "game.inventory" permission.
     *
     * @return the boolean
     * @see PlayerInventoryManager#canManipulateInventory()
     */
    public boolean hasElevatedPrivileges() {
        return playerInventoryManager.canManipulateInventory();
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
     * Sets player game manager.
     *
     * @param playerGameManager the player game manager
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
     * Gets player inventory manager.
     *
     * @return the player inventory manager
     */
    public PlayerInventoryManager getPlayerInventoryManager() {
        return playerInventoryManager;
    }

    /**
     * Sets player inventory manager.
     *
     * @param playerInventoryManager the player inventory manager
     */
    public void setPlayerInventoryManager(PlayerInventoryManager playerInventoryManager) {
        this.playerInventoryManager = playerInventoryManager;
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
