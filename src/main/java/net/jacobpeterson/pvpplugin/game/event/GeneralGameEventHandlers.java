package net.jacobpeterson.pvpplugin.game.event;

import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.GameManager;
import net.jacobpeterson.pvpplugin.player.PlayerManager;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GeneralGameEventHandlers implements Initializers {

    private GameManager gameManager;
    private PlayerManager playerManager;

    /**
     * Instantiates a new GeneralGameEventHandlers which is used to distribute events related to pvp/games
     * to the proper game instances and handle general events.
     *
     * @param gameManager the game manager
     */
    public GeneralGameEventHandlers(GameManager gameManager) {
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
     * NOTE: This event is called ONLY when an entity gets damaged, NOT when an entity damages another entity.
     * So it is only called once, say, if one player damages another player.
     *
     * @param event the event
     */
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        PvPPlayer damagerPvPPlayer = (damager instanceof Player ? playerManager.getPvPPlayer((Player) damager) : null);
        PvPPlayer damageePvPPlayer = (damagee instanceof Player ? playerManager.getPvPPlayer((Player) damagee) : null);

        // Set the playerCurrentGame equal to the damagee's game, but if the damageePvPPlayer is null
        // (aka the damagerPvPPlayer damaged a non-PvPPlayer Entity), then use the damager's game.
        Game playerCurrentGame = null;
        if (damageePvPPlayer != null) {
            playerCurrentGame = damageePvPPlayer.getPlayerGameManager().getCurrentGame();
        } else if (damagerPvPPlayer != null) {
            playerCurrentGame = damagerPvPPlayer.getPlayerGameManager().getCurrentGame();
        }

        if (playerCurrentGame != null) {
            playerCurrentGame.getGameEventHandler().handleEntityDamageByEntityEvent(
                    event, damageePvPPlayer, damagerPvPPlayer);
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
