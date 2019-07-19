package net.jacobpeterson.spigot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.jacobpeterson.spigot.data.serializers.ItemStackArraySerializer;
import net.jacobpeterson.spigot.data.serializers.ItemStackSerializer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.nio.file.Files;

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

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) throws Exception {
        if (event.getMessage().equalsIgnoreCase("/writeInventory")) {
            File file = new File("/Users/jacob/Desktop", "itemstack.json");
            Gson gson = new GsonBuilder().registerTypeAdapter(
                    new TypeToken<ItemStack[]>() {
                    }.getType(), new ItemStackArraySerializer())
                    .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
                    .setPrettyPrinting()
                    .create();
            String playerInventoryJson = gson.toJson(event.getPlayer().getInventory().getContents(), ItemStack[].class);
            Files.write(file.toPath(), playerInventoryJson.getBytes());

            event.getPlayer().getInventory().clear();
        } else if (event.getMessage().equalsIgnoreCase("/readInventory")) {
            File file = new File("/Users/jacob/Desktop", "itemstack.json");
            Gson gson = new GsonBuilder().registerTypeAdapter(
                    new TypeToken<ItemStack[]>() {
                    }.getType(), new ItemStackArraySerializer())
                    .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
                    .setPrettyPrinting()
                    .create();
            ItemStack[] contents = gson.fromJson(new String(Files.readAllBytes(file.toPath())), ItemStack[].class);

            event.getPlayer().getInventory().setContents(contents);
        }
    }

}
