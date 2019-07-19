package net.jacobpeterson.spigot.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.data.itemstack.ItemStackArraySerializer;
import net.jacobpeterson.spigot.data.itemstack.ItemStackSerializer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.inventory.ItemStack;

public class GsonManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private JsonParser jsonParser;
    private Gson gson;

    /**
     * Instantiates a new Gson manager which is used to (de)serialize various objects for this PvPPlugin.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GsonManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() {
        this.jsonParser = new JsonParser();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackSerializer());
        gsonBuilder.registerTypeAdapter(new TypeToken<ItemStack[]>() {
        }.getType(), new ItemStackArraySerializer());

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
}
