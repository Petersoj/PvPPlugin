package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.player.listener.PlayerListeners;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PluginListeners implements Listener, Initializers {

    private PvPPlugin pvpPlugin;
    private PlayerListeners playerListeners;

    public PluginListeners(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, pvpPlugin);

        this.playerListeners = pvpPlugin.getPlayerManager().getPlayerListeners();
    }

    @Override
    public void deinit() {
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        this.playerListeners.handleOnPlayerJoinEvent(event);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

    }

    @EventHandler
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        // TODO InventoryListeners
    }
}
