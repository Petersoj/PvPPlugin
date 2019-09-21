package net.jacobpeterson.pvpplugin.location.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        JsonObject locationObject = new JsonObject();
        locationObject.addProperty("world", location.getWorld().getName());
        locationObject.addProperty("x", location.getX());
        locationObject.addProperty("y", location.getY());
        locationObject.addProperty("z", location.getZ());
        locationObject.addProperty("pitch", location.getPitch());
        locationObject.addProperty("yaw", location.getYaw());
        return locationObject;
    }

    @Override
    public Location deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        if (!(element instanceof JsonObject)) {
            throw new JsonParseException("Location must be JSON Object!");
        }

        JsonObject locationObject = (JsonObject) element;

        World world = Bukkit.getWorld(locationObject.get("world").getAsString());
        double x = locationObject.get("x").getAsDouble();
        double y = locationObject.get("y").getAsDouble();
        double z = locationObject.get("z").getAsDouble();
        float pitch = locationObject.get("pitch").getAsFloat();
        float yaw = locationObject.get("yaw").getAsFloat();

        return new Location(world, x, y, z, yaw, pitch);
    }
}
