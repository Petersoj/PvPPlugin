package net.jacobpeterson.spigot;

import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class PvPListeners implements Listener, Initializers {

    private PvPPlugin pvpPlugin;

    public PvPListeners(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, pvpPlugin);
    }

    @Override
    public void deinit() {
    }

    @EventHandler
    public void onInventoryInteractEvent(InventoryInteractEvent event) {

    }

}
