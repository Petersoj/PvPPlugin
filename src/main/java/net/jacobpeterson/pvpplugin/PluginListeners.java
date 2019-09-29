package net.jacobpeterson.pvpplugin;

import net.jacobpeterson.pvpplugin.game.event.GeneralGameEventHandlers;
import net.jacobpeterson.pvpplugin.gui.event.InventoryGUIEventHandlers;
import net.jacobpeterson.pvpplugin.player.event.PlayerEventHandlers;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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
    private GeneralGameEventHandlers generalGameEventHandlers;

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
        this.generalGameEventHandlers = pvpPlugin.getGameManager().getGeneralGameEventHandlers();
    }

    @Override
    public void deinit() {
    }

    /**
     * On player command pre process event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerCommandPreProcessEvent(PlayerCommandPreprocessEvent event) {
        this.pvpPlugin.getCommandHandler().handlePlayerCommandPreProcessEvent(event);
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
        this.generalGameEventHandlers.handlePlayerQuitEvent(event);
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
        this.generalGameEventHandlers.handlePlayerRespawnEvent(event);
    }

    /**
     * On entity damage by block event.
     *
     * @param event the event
     */
    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
        this.generalGameEventHandlers.handleEntityDamageByBlockEvent(event);
    }

    /**
     * On entity damage by entity event.
     *
     * @param event the event
     */
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        this.generalGameEventHandlers.handleEntityDamageByEntityEvent(event);
    }

    /**
     * On player death event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        this.generalGameEventHandlers.handlePlayerDeathEvent(event);
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
    // Lowest Event Priority will happen last that way Essentials will set the format and we can prepend ELO
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        this.playerEventHandlers.handleAsyncPlayerChatEvent(event);
    }
}
