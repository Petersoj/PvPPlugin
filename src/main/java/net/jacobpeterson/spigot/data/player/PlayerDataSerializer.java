package net.jacobpeterson.spigot.data.player;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PlayerDataSerializer implements JsonSerializer<PlayerData>, JsonDeserializer<PlayerData> {

    @Override
    public JsonElement serialize(PlayerData playerData, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public PlayerData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        return null;
    }
}
