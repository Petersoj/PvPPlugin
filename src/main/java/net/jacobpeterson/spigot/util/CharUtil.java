package net.jacobpeterson.spigot.util;

import org.bukkit.ChatColor;

import java.nio.charset.Charset;

public final class CharUtil {

    private static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");

    public static final String DOUBLE_RIGHT_ARROW = fromUTFCode("\u2192");

    public static String boldColor(ChatColor color) {
        return color.toString() + ChatColor.BOLD.toString();
    }

    public static String prependReset(ChatColor color) {
        return ChatColor.RESET + color.toString();
    }

    private static String fromUTFCode(String code) {
        return new String(code.getBytes(UTF_8_CHARSET), UTF_8_CHARSET);
    }

}
