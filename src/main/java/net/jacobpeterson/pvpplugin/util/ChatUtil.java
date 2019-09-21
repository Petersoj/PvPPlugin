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

    /**
     * Format player health string.
     * e.g. "3.5 ❤"
     *
     * @param health the health
     * @return the string
     */
    public static String formatPlayerHealth(float health) {
        return String.format("%.1f ", health) + ChatColor.RED + CharUtil.HEART;
    }

}
