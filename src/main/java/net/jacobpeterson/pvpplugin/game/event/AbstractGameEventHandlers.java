package net.jacobpeterson.pvpplugin.game.event;

import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.game.DamageTracker;
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
     * Used for {@link net.jacobpeterson.pvpplugin.player.game.DamageTracker}.
     *
     * @param event     the event
     * @param pvpPlayer the pvp player
     */
    public void handleEntityDamageByBlockEvent(EntityDamageByBlockEvent event, PvPPlayer pvpPlayer) {
        DamageTracker damageTracker = pvpPlayer.getPlayerGameManager().getDamageTracker();
        damageTracker.setLastDamagingPvPPlayer(null);
        damageTracker.setLastDamagingObject(event.getDamager());
        damageTracker.setLastDamagingAmount(event.getDamage());
    }

    /**
     * Handle entity damage by entity event.
     * Used for {@link net.jacobpeterson.pvpplugin.player.game.DamageTracker}.
     *
     * @param event     the event
     * @param pvpPlayer the pvp player
     */
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event, PvPPlayer pvpPlayer) {
        DamageTracker damageTracker = pvpPlayer.getPlayerGameManager().getDamageTracker();
        damageTracker.setLastDamagingPvPPlayer(null);
        damageTracker.setLastDamagingObject(event.getDamager());
        damageTracker.setLastDamagingAmount(event.getDamage());
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
        // <displayname> &6had &b<health> &6health left when they killed you.
    }

    /**
     * Send kill message.
     *
     * @param killerPvPPlayer the killer pvp player
     */
    public void sendKillMessage(PvPPlayer killerPvPPlayer) {
        // &6You have killed <displayname>&6!
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public abstract Game getGame();
}
