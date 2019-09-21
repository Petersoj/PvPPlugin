package net.jacobpeterson.pvpplugin.util;

import org.bukkit.ChatColor;

public final class ChatUtil {

    public static final String SERVER_CHAT_PREFIX = ChatColor.YELLOW + "Siege " +
            ChatColor.DARK_GRAY + CharUtil.DOUBLE_RIGHT_ARROW + ChatColor.RESET + " ";

    /**
     * Adds the bold format character in addition to a color format character.
     *
     * @param color the color
     * @return the string
     */
    public static String boldColor(ChatColor color) {
        return color.toString() + ChatColor.BOLD.toString();
    }

    /**
     * Prepends the reset chat character.
     *
     * @param color the color
     * @return the string
     */
    public static String prependReset(ChatColor color) {
        return ChatColor.RESET + color.toString();
    }

}
