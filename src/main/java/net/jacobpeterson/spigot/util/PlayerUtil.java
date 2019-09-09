package net.jacobpeterson.spigot.util;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;

public final class PlayerUtil {

    /**
     * Regex pattern to add '-' in UUID strings
     */
    private static final Pattern UUID_DASH_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    /**
     * Gets a player UUID from .
     *
     * @param playerName the player name
     * @return the uuid
     */
    public static UUID getMojangUUID(String playerName) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

            String UUIDJson = IOUtils.toString(new URL(url));

            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);

            String stringUUID = UUIDObject.get("id").toString();
            return stringUUID == null ? null : uuidFromString(stringUUID);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * UUID from string uuid with or without the hyphens.
     *
     * @param stringUUID the string uuid
     * @return the uuid
     */
    public static UUID uuidFromString(String stringUUID) {
        if (stringUUID.contains("-")) {
            return UUID.fromString(stringUUID);
        } else {
            String dashedUUID = UUID_DASH_PATTERN.matcher(stringUUID).replaceAll("$1-$2-$3-$4-$5");
            return UUID.fromString(dashedUUID);
        }
    }
}
