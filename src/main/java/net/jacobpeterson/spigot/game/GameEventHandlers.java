package net.jacobpeterson.spigot.game;

import net.jacobpeterson.spigot.PvPPlugin;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.logging.Logger;

public class GameEventHandlers {

    private final Logger LOGGER;
    private GameManager gameManager;

    public GameEventHandlers(GameManager gameManager) {
        LOGGER = PvPPlugin.getPluginLogger();
        this.gameManager = gameManager;
    }

    public void handleOnPlayerDeathEvent(PlayerDeathEvent event) {

    }

    public void handleOnPlayerRespawnEvent(PlayerRespawnEvent event) {

    }
}
