package net.jacobpeterson.pvpplugin.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.data.ArenaSerializer;
import net.jacobpeterson.pvpplugin.itemstack.data.ItemStackArraySerializer;
import net.jacobpeterson.pvpplugin.itemstack.data.ItemStackSerializer;
import net.jacobpeterson.pvpplugin.location.data.LocationSerializer;
import net.jacobpeterson.pvpplugin.util.Initializers;
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
    private Gson noArenaSerializerGson;

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

        // Create GsonBuilder and register mandatory type adapters
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization(); // Will allow keys in Maps to be serialized as Objects
        gsonBuilder.registerTypeAdapter(Location.class, locationSerializer);
        gsonBuilder.registerTypeAdapter(ItemStack.class, itemStackSerializer);
        gsonBuilder.registerTypeAdapter(ItemStack[].class, itemStackArraySerializer);

        // Create Gson with no Arena serializer adapter
        this.noArenaSerializerGson = gsonBuilder.create();

        gsonBuilder.registerTypeHierarchyAdapter(Arena.class, arenaSerializer);

        // Create Gson with all type adapters registered Gson
        this.gson = gsonBuilder.create();

        // Create Gson all type adapters registered with pretty printing
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

    /**
     * Gets no arena serializer gson.
     *
     * @return the no arena serializer gson
     */
    public Gson getNoArenaSerializerGson() {
        return noArenaSerializerGson;
    }
}
