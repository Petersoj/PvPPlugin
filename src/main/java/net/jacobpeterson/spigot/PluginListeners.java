package net.jacobpeterson.spigot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class PluginListeners implements Listener {

    private PvPPlugin pvpPlugin;

    public PluginListeners(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @EventHandler
    public void onInventoryInteractEvent(InventoryInteractEvent event) {

    }

}
