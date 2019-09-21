package net.jacobpeterson.pvpplugin;

import net.jacobpeterson.pvpplugin.game.event.GameEventHandlersDistributor;
import net.jacobpeterson.pvpplugin.gui.event.InventoryGUIEventHandlers;
import net.jacobpeterson.pvpplugin.player.event.PlayerEventHandlers;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PluginListeners implements Listener, Initializers {

    private PvPPlugin pvpPlugin;
    private PlayerEventHandlers playerEventHandlers;
    private InventoryGUIEventHandlers inventoryGUIEventHandlers;
    private GameEventHandlersDistributor gameEventHandlersDistributor;

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
        this.gameEventHandlersDistributor = pvpPlugin.getGameManager().getGameEventHandlersDistributor();
    }

    @Override
    public void deinit() {
    }

    /**
     * On player join event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        this.playerEventHandlers.handlePlayerJoinEvent(event);
    }

    /**
     * On player quit event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        this.gameEventHandlersDistributor.handlePlayerQuitEvent(event);
        this.playerEventHandlers.handlePlayerQuitEvent(event);
    }

    /**
     * On player spawn location event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
        this.playerEventHandlers.handlePlayerSpawnLocationEvent(event);
    }

    /**
     * On player respawn event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        this.gameEventHandlersDistributor.handlePlayerRespawnEvent(event);
    }

    /**
     * On entity damage by block event.
     *
     * @param event the event
     */
    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
        this.gameEventHandlersDistributor.handleEntityDamageByBlockEvent(event);
    }

    /**
     * On entity damage by entity event.
     *
     * @param event the event
     */
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        this.gameEventHandlersDistributor.handleEntityDamageByEntityEvent(event);
    }

    /**
     * On player death event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        this.gameEventHandlersDistributor.handlePlayerDeathEvent(event);
    }

    /**
     * On player interact event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        this.playerEventHandlers.handlePlayerInteractEvent(event);
    }

    /**
     * On player drop item event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        this.playerEventHandlers.handlePlayerDropItemEvent(event);
    }

    /**
     * On inventory click event.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        this.playerEventHandlers.handleInventoryClickEvent(event);
        this.inventoryGUIEventHandlers.handleInventoryClickEvent(event);
    }

    /**
     * On async player chat event.
     *
     * @param event the event
     */
    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        this.playerEventHandlers.handleAsyncPlayerChatEvent(event);
    }
}
