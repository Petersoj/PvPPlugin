package net.jacobpeterson.pvpplugin.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Just use https://github.com/NyxCode/MojangAPI in the future!
 */
public final class PlayerUtil {

    /**
     * Regex pattern to add '-' in UUID strings
     */
    // Thanks to https://stackoverflow.com/a/47238049
    private static final Pattern UUID_DASH_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    /**
     * Sends title to player via NMS.
     *
     * @param player      the player
     * @param title       the title (can be null)
     * @param subtitle    the subtitle (can be null)
     * @param fadeInTime  the fade in time in ticks
     * @param showTime    the show time in ticks
     * @param fadeOutTime the fade out time in ticks
     */
    public static void sendTitle(Player player, String title, String subtitle,
                                 int fadeInTime, int showTime, int fadeOutTime) {
        // Send the times first
        PacketPlayOutTitle packetPlayOutTitleTimes = new PacketPlayOutTitle(fadeInTime, showTime, fadeOutTime);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitleTimes);

        if (title != null) {
            IChatBaseComponent titleChatBaseComponent = IChatBaseComponent.ChatSerializer.
                    a("{\"text\": \"" + title + "\"}");

            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.TITLE, titleChatBaseComponent);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitle);
        }
        if (subtitle != null) {
            IChatBaseComponent subTitleChatBaseComponent = IChatBaseComponent.ChatSerializer.
                    a("{\"text\": \"" + subtitle + "\"}");

            PacketPlayOutTitle packetPlayOutSubtitle = new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subTitleChatBaseComponent);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutSubtitle);
        }
    }

    /**
     * Clears title on player screen via NMS.
     *
     * @param player the player
     */
    public static void clearTitle(Player player) {
        PacketPlayOutTitle packetPlayOutTitleClear = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.CLEAR, null);
        PacketPlayOutTitle packetPlayOutTitleReset = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.RESET, null);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitleClear);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitleReset);
    }

    /**
     * Gets a player UUID from a player name via Mojang's RestAPI.
     * Should be execute async!
     *
     * @param playerName the player name
     * @return the uuid
     * @throws IOException the IOException
     * @see <a href=https://wiki.vg/Mojang_API#Username_-.3E_UUID_at_time>Mojang_API#Username -> UUID at time</a>
     */
    public static UUID getSingleMojangUUID(String playerName) throws IOException {
        // Create GET request to API
        String uuidAPIURLString = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
        URL uuidAPIURL = new URL(uuidAPIURLString);
        HttpURLConnection uuidAPIConnection = (HttpURLConnection) uuidAPIURL.openConnection();
        uuidAPIConnection.setRequestMethod("GET");

        // Create input stream reading
        BufferedReader responseBufferedReader = new BufferedReader(
                new InputStreamReader(uuidAPIConnection.getInputStream()));
        String responseLine;
        StringBuilder finalResponseBuilder = new StringBuilder();

        // Read response lines
        while ((responseLine = responseBufferedReader.readLine()) != null) {
            finalResponseBuilder.append(responseLine);
        }

        // Parse JSON response and get as JsonObject
        JsonElement responseJsonElement = new JsonParser().parse(finalResponseBuilder.toString());
        JsonObject responseJsonObject = responseJsonElement.getAsJsonObject();

        // Get the 'id' value and convert to UUID
        String stringUUID = responseJsonObject.get("id").getAsString();
        return stringUUID == null ? null : PlayerUtil.uuidFromString(stringUUID);
    }

    /**
     * Gets multiple Mojang UUIDs via Mojang's RestAPI.
     * DO NOT USE {@link #getSingleMojangUUID(String)} for a lot of player names. Use this
     * method as it ensures that you don't exceed the rate limit of Mojang's API.
     * Should be execute async!
     *
     * @param playerNames the player names arrayList (MUST NOT EXCEED 50 NAMES (use batching if necessary))
     * @return the multiple Mojang UUIDs map (key = name, value = UUID)
     * @throws IOException the IOException
     * @see <a href=https://wiki.vg/Mojang_API#Playernames_-.3E_UUIDs>Mojang_API#Playernames -> UUIDs</a>
     */
    @SuppressWarnings("deprecation")
    public static HashMap<String, UUID> getMultipleMojangUUIDs(List<String> playerNames)
            throws IOException {
        if (playerNames.size() > 50) {
            throw new IllegalStateException("Player Names count cannot exceed 50! Use batching!");
        }

        HashMap<String, UUID> playerUUIDs = new HashMap<>();

        // Now create request to the Mojang API
        String uuidAPIURLString = "https://api.mojang.com/profiles/minecraft";

        String fetchNamesJsonArray = new Gson().toJson(playerNames);
        byte[] outputData = fetchNamesJsonArray.getBytes(StandardCharsets.UTF_8);
        int outputDataLength = outputData.length;

        // Create POST Request to Mojang API
        URL uuidAPIURL = new URL(uuidAPIURLString);
        HttpURLConnection uuidAPIConnection = (HttpURLConnection) uuidAPIURL.openConnection();
        uuidAPIConnection.setRequestMethod("POST");
        uuidAPIConnection.setDoOutput(true);
        uuidAPIConnection.setFixedLengthStreamingMode(outputDataLength);
        uuidAPIConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // Write out all player names to the POST request
        OutputStream apiOutputStream = uuidAPIConnection.getOutputStream();
        apiOutputStream.write(outputData);
        apiOutputStream.close();

        // Create a buffered reader and then JsonReader to easily parse the data as it's coming in
        BufferedReader apiInputStream = new BufferedReader(new InputStreamReader(uuidAPIConnection.getInputStream()));
        JsonReader jsonArrayReader = new JsonReader(apiInputStream);

        // Check if there is even an array from the response
        if (jsonArrayReader.hasNext()) {
            jsonArrayReader.beginArray(); // Star the array for iteration below
        } else {
            return playerUUIDs;
        }

        // Start going through the response JSON
        // The following code is expecting a response in the same format as giving in:
        // https://wiki.vg/Mojang_API#Playernames_-.3E_UUIDs
        while (jsonArrayReader.hasNext()) {
            jsonArrayReader.beginObject(); // Start ID Object

            String name = null;
            UUID uuid = null;

            while (jsonArrayReader.hasNext()) { // Iterate through all key/values in ID Object
                String key = jsonArrayReader.nextName(); // Get key
                if (key.equals("name")) { // Get the name so that we can map the UUID properly
                    name = jsonArrayReader.nextString();
                } else if (key.equals("id")) { // Get the UUID value
                    uuid = uuidFromString(jsonArrayReader.nextString());
                } else {
                    jsonArrayReader.skipValue(); //
                }
            }
            jsonArrayReader.endObject(); // End ID Object

            if (name != null && uuid != null) { // Add name or id (UUID) if it was found in the JSON response
                playerUUIDs.put(name, uuid);
            }
        }
        jsonArrayReader.endArray(); // End off the array JSON response
        jsonArrayReader.close();

        return playerUUIDs;
    }

    /**
     * Gets a player name from UUID via the Mojang API.
     * The number of calls to this method cannot exceed 1 per second.
     * Implement a timed repeating task if you need to call this method many times in a short duration.
     *
     * @param uuid the uuid
     * @return the current player name
     * @throws IOException the IOException
     */
    public static String getMojangPlayerName(UUID uuid) throws IOException {
        String nameHistoryAPIURLString = "https://api.mojang.com/user/profiles/" +
                PlayerUtil.uuidToDashlessUUID(uuid) + "/names";

        URL nameHistoryAPIURL = new URL(nameHistoryAPIURLString);
        HttpURLConnection nameHistoryAPIConnection = (HttpURLConnection) nameHistoryAPIURL.openConnection();
        nameHistoryAPIConnection.setRequestMethod("GET");

        // Create input stream reading
        BufferedReader responseBufferedReader = new BufferedReader(
                new InputStreamReader(nameHistoryAPIConnection.getInputStream()));
        String responseLine;
        StringBuilder finalResponseBuilder = new StringBuilder();

        // Read response lines
        while ((responseLine = responseBufferedReader.readLine()) != null) {
            finalResponseBuilder.append(responseLine);
        }

        // Parse JSON response and get as JsonObject
        JsonElement responseJsonElement = new JsonParser().parse(finalResponseBuilder.toString());
        JsonArray responseJsonArray = responseJsonElement.getAsJsonArray();

        String changedToAtKey = "changedToAt";
        String nameKey = "name";
        long lastChangedToAt = 0;
        String currentName = null;
        for (JsonElement arrayElement : responseJsonArray) {
            JsonObject nameObject = arrayElement.getAsJsonObject();

            if (nameObject.has(changedToAtKey)) {
                long changedToAt = nameObject.get(changedToAtKey).getAsLong();
                if (changedToAt > lastChangedToAt) {
                    currentName = nameObject.get(nameKey).getAsString();
                }
            } else {
                currentName = nameObject.get(nameKey).getAsString();
            }
        }

        if (currentName == null) {
            throw new RuntimeException("Could not get current player name from API!");
        } else {
            return currentName;
        }
    }

    /**
     * UUID from string uuid with or without the hyphens.
     *
     * @param stringUUID the string uuid
     * @return the uuid
     */
    // Thanks to https://stackoverflow.com/a/47238049
    public static UUID uuidFromString(String stringUUID) {
        if (stringUUID.contains("-")) {
            return UUID.fromString(stringUUID);
        } else {
            String dashedUUID = UUID_DASH_PATTERN.matcher(stringUUID).replaceAll("$1-$2-$3-$4-$5");
            return UUID.fromString(dashedUUID);
        }
    }

    /**
     * Converts UUID to String UUID without any dashes in it. (Mojang API requires this).
     *
     * @param uuid the uuid
     * @return the string
     */
    public static String uuidToDashlessUUID(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    /**
     * Gets the fake UUID that is in the same format as Bukkit when it's getting a UUID of an OfflinePlayer
     * that can't get it's real UUID attached via {@link org.bukkit.Bukkit#getOfflinePlayer(String)}.
     * This is used for {@link #getSingleMojangUUID(String)} or {@link #} so that it can validate if
     * {@link org.bukkit.Bukkit#getOfflinePlayer(String)} doesn't contain a real player UUID.
     *
     * @param playerName the player name
     * @return the bukkit formatted fake offline player uuid
     */
    private static UUID getBukkitFormattedFakeOfflinePlayerUUID(String playerName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8));
    }
}
