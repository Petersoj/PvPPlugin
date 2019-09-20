package net.jacobpeterson.spigot.game.listener;

import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.player.PvPPlayer;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public abstract class AbstractGameEventHandlers {

    protected Game game;

    public AbstractGameEventHandlers(Game game) {
        this.game = game;
    }

    /**
     * Handle player quit event.
     *
     * @param event     the event
     * @param pvpPlayer the pvp player
     */
    public abstract void handlePlayerQuitEvent(PlayerQuitEvent event, PvPPlayer pvpPlayer);

    /**
     * Handle entity damage by block event.
     * Used for {@link net.jacobpeterson.spigot.player.game.PlayerGameManager#setLastDamager(PvPPlayer, Object)}.
     *
     * @param event     the event
     * @param pvpPlayer the pvp player
     */
    public void handleEntityDamageByBlockEvent(EntityDamageByBlockEvent event, PvPPlayer pvpPlayer) {
        // TODO set the setLastDamager properly
    }

    /**
     * Handle entity damage by entity event.
     * Used for {@link net.jacobpeterson.spigot.player.game.PlayerGameManager#setLastDamager(PvPPlayer, Object)}.
     *
     * @param event     the event
     * @param pvpPlayer the pvp player
     */
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event, PvPPlayer pvpPlayer) {
        // TODO set the setLastDamager properly
    }

    /**
     * Handle player death event.
     *
     * @param event     the event
     * @param pvpPlayer the pvp player
     */
    public abstract void handlePlayerDeathEvent(PlayerDeathEvent event, PvPPlayer pvpPlayer);

    /**
     * Handle player respawn event.
     *
     * @param event     the event
     * @param pvpPlayer the pvp player
     */
    public abstract void handlePlayerRespawnEvent(PlayerRespawnEvent event, PvPPlayer pvpPlayer);

    /**
     * Send death message.
     *
     * @param deadPvPPlayer the dead pvp player
     */
    public void sendDeathMessage(PvPPlayer deadPvPPlayer) {
        // TODO
    }

    /**
     * Send kill message.
     *
     * @param killerPvPPlayer the killer pvp player
     */
    public void sendKillMessage(PvPPlayer killerPvPPlayer) {
        // TODO
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public abstract Game getGame();
}
