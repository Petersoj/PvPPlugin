package net.jacobpeterson.spigot.player;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.data.PlayerDataManager;
import net.jacobpeterson.spigot.player.listener.PlayerEventHandlers;
import net.jacobpeterson.spigot.util.Initializers;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Manager to manage the PvPPlayer players.
 */
public class PlayerManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private PlayerEventHandlers playerEventHandlers;
    private PlayerDataManager playerDataManager;
    private ArrayList<PvPPlayer> pvpPlayers;

    /**
     * Instantiates a new Player data manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public PlayerManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.playerEventHandlers = new PlayerEventHandlers(this);
        this.pvpPlayers = new ArrayList<>();
        this.playerDataManager = new PlayerDataManager(this, pvpPlugin.getGsonManager(), pvpPlugin.getDatabaseManager());
    }

    @Override
    public void init() throws SQLException {
        playerDataManager.init();
    }

    @Override
    public void deinit() {
        playerDataManager.deinit();
    }

    /**
     * Create new PvPPlayer from a Player object, initializes it, and adds it to {@link PlayerManager#getPvPPlayers()}.
     *
     * @param player the player
     * @return the pvp player
     */
    public PvPPlayer createNewPvPPlayer(Player player) {
        PvPPlayer pvpPlayer = new PvPPlayer(this, player);
        pvpPlayer.init();

        pvpPlayers.add(pvpPlayer);
        return pvpPlayer;
    }

    /**
     * Gets a PvPPlayer from a Player object.
     *
     * @param player the player
     * @return the PvPPlayer
     */
    public PvPPlayer getPvPPlayer(Player player) {
        for (PvPPlayer pvpPlayer : pvpPlayers) {
            if (pvpPlayer.getPlayer() == player) {
                return pvpPlayer;
            }
        }
        return null;
    }

    /**
     * Gets player group prefix from the GroupManager spigot plugin.
     *
     * @param player the player
     * @return the player's GroupManager group prefix (will be empty string if prefix doesn't exist)
     */
    public String getPlayerGroupPrefix(Player player) {
        AnjoPermissionsHandler anjoPermissionsHandler =
                pvpPlugin.getGroupManager().getWorldsHolder().getWorldPermissions(player);
        if (anjoPermissionsHandler != null) {
            String userPrefix = anjoPermissionsHandler.getUserPrefix(player.getName());
            if (userPrefix != null) {
                return userPrefix;
            }
        }
        return "";
    }

    /**
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PvPPlugin getPvPPlugin() {
        return pvpPlugin;
    }

    /**
     * Gets player event handlers.
     *
     * @return the player event handlers
     */
    public PlayerEventHandlers getPlayerEventHandlers() {
        return playerEventHandlers;
    }

    /**
     * Gets player data manager.
     *
     * @return the player data manager
     */
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    /**
     * Gets pvp players.
     *
     * @return the pvp players
     */
    public ArrayList<PvPPlayer> getPvPPlayers() {
        return pvpPlayers;
    }
}
