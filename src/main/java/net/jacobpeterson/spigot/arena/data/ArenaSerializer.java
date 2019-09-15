package net.jacobpeterson.spigot.arena.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.ArenaManager;

import java.lang.reflect.Type;

public class ArenaSerializer implements JsonSerializer<Arena>, JsonDeserializer<Arena> {

    private PvPPlugin pvpPlugin;
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
    public JsonElement serialize(Arena arena, Type type, JsonSerializationContext jsonSerializationContext) {

        if (!referenceSerialization) {
            return jsonSerializationContext.serialize(arena, type);
        } else {
            JsonObject arenaNameObject = new JsonObject();
            arenaNameObject.addProperty("name", arena.getName());
            return arenaNameObject;
        }
    }

    @Override
    public Arena deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        if (!referenceDeserialization) {

            Arena arena = jsonDeserializationContext.deserialize(jsonElement, type);
            arena.setArenaManager(pvpPlugin.getArenaManager());
            arena.getArenaItemStack().setArena(arena); // Set the reference in ArenaItemStack
            return arena;

        } else {

            if (!(jsonElement instanceof JsonObject)) {
                throw new JsonParseException("Arena must be JSON Object!");
            }

            ArenaManager arenaManager = pvpPlugin.getArenaManager();

            String arenaName = ((JsonObject) jsonElement).get("name").getAsString();

            for (Arena arena : arenaManager.getAllArenas()) {
                if (arena.getName().equals(arenaName)) {
                    return arena;
                }
            }
            return new Arena(null, arenaName) {
            }; // Create a new arena that doesn't exist just so there is record of a player playing that arena for
            // player "arena times played"
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
