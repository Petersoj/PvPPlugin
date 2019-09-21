package net.jacobpeterson.pvpplugin.itemstack.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackArraySerializer implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {

    @Override
    public JsonElement serialize(ItemStack[] contents, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        // The size of the array is necessary to reconstruct the right size when deserializing
        JsonObject sizeObject = new JsonObject();
        sizeObject.addProperty("size", contents.length);
        jsonArray.add(sizeObject); // Must be the first object in the array for deserialization

        for (int index = 0; index < contents.length; index++) {
            ItemStack itemStack = contents[index];

            if (itemStack != null && itemStack.getType() != Material.AIR) {

                JsonObject inventorySlotObject = new JsonObject();
                inventorySlotObject.addProperty("slot", index);
                // ItemStack will be serialized with the net.jacobpeterson.spigot.itemstack.data.ItemStackSerializer
                inventorySlotObject.add("item", jsonSerializationContext.serialize(itemStack, ItemStack.class));

                jsonArray.add(inventorySlotObject);
            }
        }
        return jsonArray;
    }

    @Override
    public ItemStack[] deserialize(JsonElement jsonElement, Type type,
                                   JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ItemStack[] contents = null;
        if (!(jsonElement instanceof JsonArray)) {
            throw new JsonParseException("ItemStack Array must be JSON Array!");
        }

        JsonArray jsonItemStackArray = (JsonArray) jsonElement;

        if (jsonItemStackArray.size() > 0) {
            int size = jsonItemStackArray.get(0).getAsJsonObject().getAsJsonPrimitive("size").getAsInt();
            contents = new ItemStack[size];
        }

        for (int index = 1; index < jsonItemStackArray.size(); index++) {
            JsonObject slotJsonObject = jsonItemStackArray.get(index).getAsJsonObject();
            ItemStack itemStack = jsonDeserializationContext.deserialize(slotJsonObject.getAsJsonObject("item"),
                    ItemStack.class);
            contents[slotJsonObject.getAsJsonPrimitive("slot").getAsInt()] = itemStack;
        }

        return contents;
    }
}
