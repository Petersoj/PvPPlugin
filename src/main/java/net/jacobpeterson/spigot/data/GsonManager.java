package net.jacobpeterson.spigot.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.data.ArenaSerializer;
import net.jacobpeterson.spigot.itemstack.data.ItemStackArraySerializer;
import net.jacobpeterson.spigot.itemstack.data.ItemStackSerializer;
import net.jacobpeterson.spigot.location.LocationSerializer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class GsonManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private JsonParser jsonParser;
    private LocationSerializer locationSerializer;
    private ItemStackSerializer itemStackSerializer;
    private ItemStackArraySerializer itemStackArraySerializer;
    private ArenaSerializer arenaSerializer;
    private Gson gson;
    private Gson prettyGson;

    /**
     * Instantiates a new Gson manager which is used to (de)serialize various objects for this PvPPlugin.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GsonManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.locationSerializer = new LocationSerializer();
        this.itemStackSerializer = new ItemStackSerializer();
        this.itemStackArraySerializer = new ItemStackArraySerializer();
        this.arenaSerializer = new ArenaSerializer(pvpPlugin);
    }

    @Override
    public void init() {
        this.jsonParser = new JsonParser();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Location.class, locationSerializer);
        gsonBuilder.registerTypeAdapter(ItemStack.class, itemStackSerializer);
        gsonBuilder.registerTypeAdapter(ItemStack[].class, itemStackArraySerializer);
        gsonBuilder.registerTypeAdapter(Arena.class, arenaSerializer);

        this.gson = gsonBuilder.create();
        this.prettyGson = gsonBuilder.setPrettyPrinting().create();
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
     * Gets location serializer.
     *
     * @return the location serializer
     */
    public LocationSerializer getLocationSerializer() {
        return locationSerializer;
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

    /**
     * Gets gson with the type adapters.
     *
     * @return the gson
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Gets pretty gson.
     *
     * @return the pretty gson
     */
    public Gson getPrettyGson() {
        return prettyGson;
    }
}
