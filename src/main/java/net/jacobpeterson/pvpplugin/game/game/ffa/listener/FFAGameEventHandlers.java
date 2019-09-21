package net.jacobpeterson.pvpplugin.game.game.ffa.listener;

import net.jacobpeterson.pvpplugin.game.event.AbstractGameEventHandlers;
import net.jacobpeterson.pvpplugin.game.game.ffa.FFAGame;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.data.PlayerData;
import net.jacobpeterson.pvpplugin.player.game.DamageTracker;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;

public class FFAGameEventHandlers extends AbstractGameEventHandlers {

    public FFAGameEventHandlers(FFAGame game) {
        super(game);
    }

    @Override
    public void handlePlayerQuitEvent(PlayerQuitEvent event, PvPPlayer pvpPlayer) {
        this.getGame().leave(pvpPlayer);
    }

    @Override
    public void handlePlayerDeathEvent(PlayerDeathEvent event, PvPPlayer pvpPlayer) {
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
    }

    @Override
    public void handlePlayerRespawnEvent(PlayerRespawnEvent event, PvPPlayer pvpPlayer) {
        // Teleport player back to spawn location for FFA
        event.setRespawnLocation(getGame().getArena().getSpawnLocation());
    }

    @Override
    public FFAGame getGame() {
        return (FFAGame) game;
    }
}
