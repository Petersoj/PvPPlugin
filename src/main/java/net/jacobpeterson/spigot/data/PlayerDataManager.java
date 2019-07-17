package net.jacobpeterson.spigot.data;

import net.jacobpeterson.spigot.PvPPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Manager to manage the PlayerData object.
 */
public class PlayerDataManager {

    private PvPPlugin pvpPlugin;
    private HashMap<Player, PlayerData> playerDatas;

    /**
     * Instantiates a new Player data manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public PlayerDataManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.playerDatas = new HashMap<>();
    }

    /**
     * Creates a new PlayerData and stores it in this Manager.
     *
     * @param player the player
     * @return the player data
     */
    public PlayerData createNewPlayerData(Player player) {
        PlayerData playerData = new PlayerData(player);
        playerDatas.put(player, playerData);
        return playerData;
    }

    /**
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PvPPlugin getPvpPlugin() {
        return pvpPlugin;
    }

    /**
     * Gets player data hashmap.
     *
     * @return the player datas
     */
    public HashMap<Player, PlayerData> getPlayerDatas() {
        return playerDatas;
    }
}
