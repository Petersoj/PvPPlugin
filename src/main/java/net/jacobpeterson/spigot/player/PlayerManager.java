package net.jacobpeterson.spigot.player;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.data.PlayerDataManager;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Manager to manage the PvPPlayer players.
 */
public class PlayerManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private PlayerDataManager playerDataManager;
    private ArrayList<PvPPlayer> pvpPlayers;

    /**
     * Instantiates a new Player data manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public PlayerManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.pvpPlayers = new ArrayList<>();
        this.playerDataManager = new PlayerDataManager(this, pvpPlugin.getGsonManager(), pvpPlugin.getDatabaseManager());
    }

    @Override
    public void init() {
    }

    @Override
    public void deinit() {
    }

    /**
     * Create new PvPPlayer from a Player object.
     *
     * @param player the player
     * @return the pvp player
     */
    public PvPPlayer createNewPvPPlayer(Player player) {
        PvPPlayer pvpPlayer = new PvPPlayer(player);
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
     * @return the player's GroupManager group prefix
     */
    public String getPlayerGroupPrefix(Player player) {
        return pvpPlugin.getGroupManager().getWorldsHolder().getWorldPermissions(player).getUserPrefix(player.getName());
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
     * Gets pvp players.
     *
     * @return the pvp players
     */
    public ArrayList<PvPPlayer> getPvpPlayers() {
        return pvpPlayers;
    }
}
