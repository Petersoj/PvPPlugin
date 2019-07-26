package net.jacobpeterson.spigot.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.data.ArenaSerializer;
import net.jacobpeterson.spigot.itemstack.data.ItemStackArraySerializer;
import net.jacobpeterson.spigot.itemstack.data.ItemStackSerializer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.inventory.ItemStack;

public class GsonManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private JsonParser jsonParser;
    private ItemStackSerializer itemStackSerializer;
    private ItemStackArraySerializer itemStackArraySerializer;
    private ArenaSerializer arenaSerializer;
    private Gson gson;

    /**
     * Instantiates a new Gson manager which is used to (de)serialize various objects for this PvPPlugin.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GsonManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.itemStackSerializer = new ItemStackSerializer();
        this.itemStackArraySerializer = new ItemStackArraySerializer();
        this.arenaSerializer = new ArenaSerializer(pvpPlugin);
    }

    @Override
    public void init() {
        this.jsonParser = new JsonParser();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ItemStack.class, itemStackSerializer);
        gsonBuilder.registerTypeAdapter(ItemStack[].class, itemStackArraySerializer);
        gsonBuilder.registerTypeAdapter(Arena.class, arenaSerializer);

        this.gson = gsonBuilder.create();
    }

    @Override
    public void deinit() {
    }

    /**
     * Gets json parser.
     *
     * @return the json parser
     */
    public JsonParser getJsonParser() {
        return jsonParser;
    }

    /**
     * Gets gson with the type adapters.
     *
     * @return the gson
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Gets item stack serializer.
     *
     * @return the item stack serializer
     */
    public ItemStackSerializer getItemStackSerializer() {
        return itemStackSerializer;
    }

    /**
     * Gets item stack array serializer.
     *
     * @return the item stack array serializer
     */
    public ItemStackArraySerializer getItemStackArraySerializer() {
        return itemStackArraySerializer;
    }

    /**
     * Gets arena serializer.
     *
     * @return the arena serializer
     */
    public ArenaSerializer getArenaSerializer() {
        return arenaSerializer;
    }
}
