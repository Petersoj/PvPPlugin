package net.jacobpeterson.pvpplugin.arena.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.data.GsonManager;
import net.jacobpeterson.pvpplugin.util.Initializers;

import java.lang.reflect.Type;

public class ArenaSerializer implements Initializers, JsonSerializer<Arena>, JsonDeserializer<Arena> {

    private PvPPlugin pvpPlugin;
    private GsonManager gsonManager;
    private boolean referenceSerialization;
    private boolean referenceDeserialization;

    /**
     * Instantiates a new Arena serializer, used to JSON (de)serialize Arena objects by referencing them already
     * (de)serialized in ArenaDataManager.
     *
     * @param pvpPlugin the pvp plugin is used to get the ArenaManager because this class is instantiated
     *                  before ArenaManager in PvPPlugin init
     */
    public ArenaSerializer(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() {
        this.gsonManager = pvpPlugin.getGsonManager();
    }

    @Override
    public void deinit() {
    }

    @Override
    public JsonElement serialize(Arena arena, Type type, JsonSerializationContext jsonSerializationContext) {
        if (!referenceSerialization) {
            return gsonManager.getNoArenaSerializerGson().toJsonTree(arena, type);
        } else {
            JsonObject arenaNameObject = new JsonObject();
            // Set the name in the Json Object which is used as the reference to the Arena Object
            arenaNameObject.addProperty("name", arena.getName());
            return arenaNameObject;
        }
    }

    @Override
    public Arena deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        if (!referenceDeserialization) {
            gsonManager.getArenaItemStackInstanceCreator().setArenaContextType(type);

            // Deserialize the Arena and setup
            Arena arena = gsonManager.getNoArenaSerializerGson().fromJson(jsonElement, type);
            arena.setArenaManager(pvpPlugin.getArenaManager());

            // Setup the ArenaItemStack
            if (arena.getArenaItemStack() != null) {
                arena.getArenaItemStack().setArena(arena);
            }

            return arena;
        } else {
            if (!(jsonElement instanceof JsonObject)) {
                throw new JsonParseException("Arena must be JSON Object!");
            }

            ArenaManager arenaManager = pvpPlugin.getArenaManager();

            // Get the name from the Json Object which is used as the reference to the Arena Object
            String arenaName = ((JsonObject) jsonElement).get("name").getAsString();

            for (Arena arena : arenaManager.getAllArenas()) {
                if (arena.getName().equals(arenaName)) {
                    return arena;
                }
            }

            return null;
        }
    }

    /**
     * Is reference serialization boolean.
     *
     * @return the boolean
     */
    public boolean isReferenceSerialization() {
        return referenceSerialization;
    }

    /**
     * Sets reference serialization.
     *
     * @param referenceSerialization the reference serialization
     */
    public void setReferenceSerialization(boolean referenceSerialization) {
        this.referenceSerialization = referenceSerialization;
    }

    /**
     * Is reference deserialization boolean.
     *
     * @return the boolean
     */
    public boolean isReferenceDeserialization() {
        return referenceDeserialization;
    }

    /**
     * Sets reference deserialization.
     *
     * @param referenceDeserialization the reference deserialization
     */
    public void setReferenceDeserialization(boolean referenceDeserialization) {
        this.referenceDeserialization = referenceDeserialization;
    }
}
