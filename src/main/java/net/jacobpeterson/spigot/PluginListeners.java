package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.gui.listener.InventoryGUIEventHandlers;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.listener.PlayerEventHandlers;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PluginListeners implements Listener, Initializers {

    private PvPPlugin pvpPlugin;
    private PlayerEventHandlers playerEventHandlers;
    private InventoryGUIEventHandlers inventoryGUIEventHandlers;

    /**
     * Instantiates a new Plugin Listeners class which calls various event handler classes.
     *
     * @param pvpPlugin the pvp plugin
     */
    public PluginListeners(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, pvpPlugin);

        this.playerEventHandlers = pvpPlugin.getPlayerManager().getPlayerEventHandlers();
        this.inventoryGUIEventHandlers = pvpPlugin.getGUIManager().getInventoryGUIEventHandlers();
    }

    @Override
    public void deinit() {
    }

    @EventHandler
    public void onTESTPlayerCommandPreProcessEvent(PlayerCommandPreprocessEvent event) {
        PvPPlayer pvpPlayer = pvpPlugin.getPlayerManager().getPvPPlayer(event.getPlayer());
        pvpPlayer.getPlayerData().setELO(pvpPlayer.getPlayerData().getELO() + 3);
        event.getPlayer().openInventory(pvpPlugin.getGUIManager().getMainMenu().getInventory());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        this.playerEventHandlers.handlePlayerJoinEvent(event);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        this.playerEventHandlers.handlePlayerQuitEvent(event);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        this.playerEventHandlers.handlePlayerInteractEvent(event);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        this.playerEventHandlers.handlePlayerDropItemEvent(event);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        this.playerEventHandlers.handleInventoryClickEvent(event);
        this.inventoryGUIEventHandlers.handleOnInventoryClickEvent(event);
    }
}
