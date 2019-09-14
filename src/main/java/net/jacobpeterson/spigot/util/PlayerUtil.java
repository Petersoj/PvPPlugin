package net.jacobpeterson.spigot.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public final class PlayerUtil {

    /**
     * Regex pattern to add '-' in UUID strings
     */
    // Thanks to https://stackoverflow.com/a/47238049
    private static final Pattern UUID_DASH_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    /**
     * Gets a player UUID from a player name.
     * This first will check the server UUID cache and then will go to Mojang's servers.
     * Should be execute async!
     *
     * @param playerName the player name
     * @return the uuid
     * @see <a href=https://wiki.vg/Mojang_API#Username_-.3E_UUID_at_time>Mojang_API#Username -> UUID at time</a>
     */
    public static UUID getSingleMojangUUID(String playerName) {
        UUID potentialUUID = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        // Check if the playerName->UUID was cached by the MC server and Bukkit is not faking the UUID of an
        // offline player.
        if (potentialUUID != PlayerUtil.getBukkitFormattedFakeOfflinePlayerUUID(playerName)) {
            return potentialUUID;
        } else {
            try {
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
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Gets multiple Mojang UUIDs.
     * This first will check the server UUID cache and then will go to Mojang's servers.
     * DO NOT USE {@link #getSingleMojangUUID(String)} for a lot of player names. Use this
     * method as it ensures that you don't exceed the rate limit of Mojang's API.
     * Should be execute async!
     *
     * @param playerNames the player names arrayList (MUST NOT EXCEED 50 NAMES (use batching if necessary))
     * @return the multiple mojang uuids map (key = name, value = UUID)
     * @throws IOException the IOException
     * @see <a href=https://wiki.vg/Mojang_API#Playernames_-.3E_UUIDs>Mojang_API#Playernames -> UUIDs</a>
     */
    public static HashMap<String, UUID> getMultipleMojangUUIDs(List<String> playerNames)
            throws IOException {
        if (playerNames.size() > 50) {
            throw new IllegalStateException("Player Names count cannot exceed 50! Use batching!");
        }

        HashMap<String, UUID> playerUUIDs = new HashMap<>();
        ArrayList<String> namesToMojangFetch = new ArrayList<>();

        // Go through player names and check if they're cached
        for (String playerName : playerNames) {
            UUID potentialUUID = Bukkit.getOfflinePlayer(playerName).getUniqueId();

            // Check if the playerName->UUID was cached by the MC server and Bukkit is not faking the UUID of an
            // offline player.
            if (potentialUUID != PlayerUtil.getBukkitFormattedFakeOfflinePlayerUUID(playerName)) {
                playerUUIDs.put(playerName, potentialUUID);
            } else {
                namesToMojangFetch.add(playerName);
            }
        }

        // Now create request to the Mojang API
        String uuidAPIURLString = "https://api.mojang.com/users/profiles/minecraft";

        String fetchNamesJsonArray = new Gson().toJson(namesToMojangFetch);
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
                String key = jsonArrayReader.nextName();
                if (key.equals("name")) { // Get the name so that we can map the UUID properly
                    name = jsonArrayReader.nextString();
                } else if (key.equals("uuid")) { // Get the UUID
                    uuid = uuidFromString(jsonArrayReader.nextString());
                }
            }

            if (name == null || uuid == null) { // Don't add if name or id (UUID) wasn't found in the JSON response
                continue;
            } else {
                playerUUIDs.put(name, uuid);
            }
            jsonArrayReader.endObject(); // End ID Object
        }
        jsonArrayReader.endArray(); // End off the array JSON response
        jsonArrayReader.close();

        return playerUUIDs;
    }

    public static String getPlayerName(UUID uuid) {
        // TODO MAYBE USE OFFLINE PLAYER LIKE HERE:
        // https://www.spigotmc.org/threads/player-name-uuid.248375/#post-2478270
        return null;
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
