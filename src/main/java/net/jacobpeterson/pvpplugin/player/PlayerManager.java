package net.jacobpeterson.pvpplugin.player;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.player.data.PlayerDataManager;
import net.jacobpeterson.pvpplugin.player.event.PlayerEventHandlers;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * Create new PvPPlayer from a Player object, does NOT initialize it,
     * and adds it to {@link PlayerManager#getPvPPlayers()}.
     *
     * @param player the player
     * @return the pvp player
     */
    public PvPPlayer createNewPvPPlayer(Player player) {
        PvPPlayer pvpPlayer = new PvPPlayer(this, player);

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
     * Gets player group prefix from the PermissionsEx spigot plugin.
     *
     * @param playerName the player name
     * @return the player's PermissionsEx group prefix (will be empty string if prefix doesn't exist)
     */
    public String getPlayerGroupPrefix(String playerName) {
        return ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(playerName).getPrefix());
    }

    /**
     * Gets player group name.
     *
     * @param playerName the player name
     * @return the player group name
     */
    public String getPlayerGroupName(String playerName) {
        List<PermissionGroup> permissionGroups = PermissionsEx.getUser(playerName).getParents();

        // Just first permission group since there shouldn't be any other permission groups
        return permissionGroups.get(0).getName();
    }

    /**
     * Checks if a player is premium (aka if the player has the 'game.save.premium' permission which can be
     * granted via PermissionsEx in a given group).
     *
     * @param player the player
     * @return the premium boolean
     */
    public boolean isPlayerPremium(Player player) {
        return player.hasPermission("game.save.premium");
    }

    /**
     * Kicks player synchronously via {@link org.bukkit.scheduler.BukkitRunnable#runTask(Plugin)} on the
     * next tick.
     *
     * @param pvpPlayer the pvp player
     */
    public void kickPlayerSync(PvPPlayer pvpPlayer, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                pvpPlayer.getPlayer().kickPlayer(message);
            }
        }.runTask(pvpPlugin);
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
