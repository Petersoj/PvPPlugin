package net.jacobpeterson.spigot.game.game.ffa.listener;

import net.jacobpeterson.spigot.game.game.ffa.FFAGame;
import net.jacobpeterson.spigot.game.listener.AbstractGameEventHandlers;
import net.jacobpeterson.spigot.player.PvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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

        player.getInventory().setContents(getGame().getArena().getInventory());
        player.getInventory().setArmorContents(getGame().getArena().getArmorInventory());

        pvpPlayer.getPlayer().spigot().respawn(); // Force a respawn
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
