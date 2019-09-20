package net.jacobpeterson.spigot.game.listener;

import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.game.GameManager;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameEventHandlersDistributor implements Initializers {

    private GameManager gameManager;
    private PlayerManager playerManager;

    /**
     * Instantiates a new DistributingGameEventHandler which is used to distribute events related to pvp/games
     * to the proper game instances.
     *
     * @param gameManager the game manager
     */
    public GameEventHandlersDistributor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void init() {
        this.playerManager = gameManager.getPvPPlugin().getPlayerManager();
    }

    @Override
    public void deinit() {
    }

    /**
     * Handle player quit event.
     *
     * @param event the event
     */
    public void handlePlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        Game playerCurrentGame = pvpPlayer.getPlayerGameManager().getCurrentGame();
        if (playerCurrentGame != null) {
            playerCurrentGame.getGameEventHandler().handlePlayerQuitEvent(event, pvpPlayer);
        }
    }

    /**
     * Handle entity damage by block event.
     *
     * @param event the event
     */
    public void handleEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        Game playerCurrentGame = pvpPlayer.getPlayerGameManager().getCurrentGame();
        if (playerCurrentGame != null) {
            playerCurrentGame.getGameEventHandler().handleEntityDamageByBlockEvent(event, pvpPlayer);
        }
    }

    /**
     * Handle entity damage by entity event.
     *
     * @param event the event
     */
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        Game playerCurrentGame = pvpPlayer.getPlayerGameManager().getCurrentGame();
        if (playerCurrentGame != null) {
            playerCurrentGame.getGameEventHandler().handleEntityDamageByEntityEvent(event, pvpPlayer);
        }
    }

    /**
     * Handle player death event.
     *
     * @param event the event
     */
    public void handlePlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        Game playerCurrentGame = pvpPlayer.getPlayerGameManager().getCurrentGame();
        if (playerCurrentGame != null) {
            playerCurrentGame.getGameEventHandler().handlePlayerDeathEvent(event, pvpPlayer);
        }
    }

    /**
     * Handle player respawn event.
     *
     * @param event the event
     */
    public void handlePlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PvPPlayer pvpPlayer = playerManager.getPvPPlayer(player);

        if (pvpPlayer == null) {
            return;
        }

        Game playerCurrentGame = pvpPlayer.getPlayerGameManager().getCurrentGame();
        if (playerCurrentGame != null) {
            playerCurrentGame.getGameEventHandler().handlePlayerRespawnEvent(event, pvpPlayer);
        }
    }
}
