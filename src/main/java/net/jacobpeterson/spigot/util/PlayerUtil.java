package net.jacobpeterson.spigot.util;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;
import java.util.UUID;

public class PlayerUtil {

    /**
     * Gets a player UUID from .
     *
     * @param playerName the player name
     * @return the uuid
     */
    public static UUID getUUID(String playerName) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

            String UUIDJson = IOUtils.toString(new URL(url));

            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);

            String stringUUID = UUIDObject.get("id").toString();
            return stringUUID == null ? null : UUID.fromString(stringUUID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
