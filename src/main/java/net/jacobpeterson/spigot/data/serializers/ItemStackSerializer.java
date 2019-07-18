package net.jacobpeterson.spigot.data.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Map;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        // Call the ItemStack serialization method provided by Bukkit which is meant for YAML, but we want JSON.
        return jsonSerializationContext.serialize(itemStack.serialize());
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        // Call the ItemStack deserialization method provided by Bukkit which is meant for YAML, but we want from JSON.
        return ItemStack.deserialize(jsonDeserializationContext.deserialize(jsonElement,
                new TypeToken<Map<String, Object>>() {
                }.getType()));
    }
}
