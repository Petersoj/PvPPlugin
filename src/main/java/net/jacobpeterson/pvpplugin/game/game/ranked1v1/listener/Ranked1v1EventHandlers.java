package net.jacobpeterson.pvpplugin.game.game.ranked1v1.listener;

import net.jacobpeterson.pvpplugin.game.event.AbstractGameEventHandlers;
import net.jacobpeterson.pvpplugin.game.game.ranked1v1.Ranked1v1Game;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Ranked1v1EventHandlers extends AbstractGameEventHandlers {

    /**
     * Instantiates a new Ranked1v1EventHandlers which is used for handling events for Ranked1v1Game.
     *
     * @param ranked1v1Game the ranked 1v1 game
     */
    public Ranked1v1EventHandlers(Ranked1v1Game ranked1v1Game) {
        super(ranked1v1Game);
    }

    @Override
    public void handlePlayerQuitEvent(PlayerQuitEvent event, PvPPlayer pvpPlayer) {
        this.getGame().leave(pvpPlayer);
    }

    @Override
    public void handlePlayerDeathEvent(PlayerDeathEvent event, PvPPlayer pvpPlayer) {
        /*
        Player player = pvpPlayer.getPlayer();

        event.setDeathMessage(null); // Cancels death message
        event.setKeepInventory(true); // Don't drop anything
        // Zero out all level/xp
        event.setKeepLevel(true);
        event.setNewExp(0);
        event.setNewLevel(0);
        event.setDroppedExp(0);

        // Update death stats
        PlayerData playerData = pvpPlayer.getPlayerData();
        playerData.setUnrankedFFADeaths(playerData.getUnrankedFFADeaths() + 1);

        // Update killer stats
        DamageTracker damageTracker = pvpPlayer.getPlayerGameManager().getDamageTracker();
        PvPPlayer lastDamagerPvPPlayer = damageTracker.getLastDamagerPvPPlayer(); // Damager = killer
        if (lastDamagerPvPPlayer != null) { // Check if last damaging PvPPlayer exists

            // Update the killers's stats
            PlayerData lastDamagerPlayerData = lastDamagerPvPPlayer.getPlayerData();
            lastDamagerPlayerData.setUnrankedFFAKills(lastDamagerPlayerData.getUnrankedFFAKills() + 1);

            // Send the killer a kill message
            this.sendKillMessage(lastDamagerPvPPlayer);
        }

        // Send the dead player a death message
        this.sendDeathMessage(pvpPlayer);

        // Set inventory to FFAGame Inventory
        player.getInventory().setContents(getGame().getArena().getInventory());
        player.getInventory().setArmorContents(getGame().getArena().getArmorInventory());

        // Force a respawn and set velocity to 0
        player.setVelocity(new Vector(0, 0, 0));
        player.spigot().respawn();
         */
    }

    @Override
    public void handlePlayerRespawnEvent(PlayerRespawnEvent event, PvPPlayer pvpPlayer) {
        event.setRespawnLocation(getGame().getArena().getFinishLocation());
    }

    @Override
    public Ranked1v1Game getGame() {
        return (Ranked1v1Game) game;
    }
}
