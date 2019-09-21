package net.jacobpeterson.pvpplugin.game.event;

import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.game.DamageTracker;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import org.bukkit.ChatColor;
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
        damageTracker.setLastDamagerPvPPlayer(null);
        damageTracker.setLastDamagerObject(event.getDamager());
        damageTracker.setLastDamagerAmount(event.getDamage());
    }

    /**
     * Handle entity damage by entity event.
     * Used for {@link net.jacobpeterson.pvpplugin.player.game.DamageTracker}.
     *
     * @param event            the event
     * @param damageePvPPlayer the damagee pvp player (player who was damaged)
     * @param damagerPvPPlayer the damager pvp player (player who inflicted damaged)
     */
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event, PvPPlayer damageePvPPlayer,
                                                PvPPlayer damagerPvPPlayer) {
        // Set the DamageTracker values for the damagee
        if (damageePvPPlayer != null) {
            DamageTracker damageeDamageTracker = damageePvPPlayer.getPlayerGameManager().getDamageTracker();

            // Set the damager of the damagee
            if (damagerPvPPlayer != null) {
                damageeDamageTracker.setLastDamagerPvPPlayer(damagerPvPPlayer);
            } else {
                damageeDamageTracker.setLastDamagerPvPPlayer(null); // Set to null to reset
            }

            damageeDamageTracker.setLastDamagerObject(event.getDamager());
            damageeDamageTracker.setLastDamagerAmount(event.getDamage());
        }

        // Set the DamageTracker values for the damager
        if (damagerPvPPlayer != null) {
            DamageTracker damagerDamageTracker = damagerPvPPlayer.getPlayerGameManager().getDamageTracker();

            // Set the damager of the damager
            if (damageePvPPlayer != null) {
                damagerDamageTracker.setLastDamagingPvPPlayer(damageePvPPlayer);
            } else {
                damagerDamageTracker.setLastDamagingPvPPlayer(null); // Set to null to reset
            }

            damagerDamageTracker.setLastDamagingObject(event.getEntity()); // getEntity is the damaged object/entity
            damagerDamageTracker.setLastDamagingAmount(event.getDamage());
        }
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
    protected void sendDeathMessage(PvPPlayer deadPvPPlayer) {
        DamageTracker damageTracker = deadPvPPlayer.getPlayerGameManager().getDamageTracker();
        PvPPlayer lastDamagingPvPPlayer = damageTracker.getLastDamagerPvPPlayer();

        // Format: <displayname> &6had &b<health> &6health left when they killed you.
        String deathMessage = ChatUtil.SERVER_CHAT_PREFIX + lastDamagingPvPPlayer.getPrefixedName() +
                ChatColor.GOLD + " had " + ChatColor.AQUA +
                ChatUtil.formatPlayerHealth((float) lastDamagingPvPPlayer.getPlayer().getHealth()) +
                ChatColor.AQUA + " when they killed you.";

        deadPvPPlayer.getPlayer().sendMessage(deathMessage);
    }

    /**
     * Send kill message.
     *
     * @param killerPvPPlayer the killer pvp player
     */
    protected void sendKillMessage(PvPPlayer killerPvPPlayer) {
        // Format: &6You have killed <displayname>&6!
        String killMessage = ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You have killed ";

        PvPPlayer lastDamagingPvPPlayer = killerPvPPlayer.getPlayerGameManager().getDamageTracker()
                .getLastDamagingPvPPlayer();
        if (lastDamagingPvPPlayer != null) {
            killMessage += lastDamagingPvPPlayer.getPrefixedName() + ChatColor.GOLD + "!";
        } else {
            killMessage += "an entity.";
        }

        killerPvPPlayer.getPlayer().sendMessage(killMessage);
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public abstract Game getGame();
}
