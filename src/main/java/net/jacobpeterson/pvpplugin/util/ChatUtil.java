package net.jacobpeterson.pvpplugin.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;

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

    /**
     * Gets a String[] that will return arguments separated by spaces, except words that are surrounded in quotes will
     * be combined into one index in the returned String[].
     *
     * @param args the args
     * @return the string []
     */
    @SuppressWarnings("StringConcatenationInLoop") // Idc
    public static String[] getArgsQuoted(String[] args) {
        ArrayList<String> finalArgs = new ArrayList<>();
        boolean currentlyInsideQuotes = false;
        String currentArgBuffer = "";
        for (String arg : args) {
            if (arg.startsWith("\"")) {
                currentlyInsideQuotes = true;
                currentArgBuffer += arg.substring(1) + " ";
            } else if (arg.endsWith("\"")) {
                currentlyInsideQuotes = false;
                currentArgBuffer += arg.substring(0, arg.length() - 1);
                finalArgs.add(currentArgBuffer);
                currentArgBuffer = "";
            } else { // No quotes found on current arg
                if (currentlyInsideQuotes) {
                    currentArgBuffer += arg + " ";
                } else {
                    finalArgs.add(arg);
                }
            }
        }
        return finalArgs.toArray(new String[0]);
    }
}
