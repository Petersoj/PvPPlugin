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
     * Strips any color codes from the input (including § and &).
     *
     * @param input the input
     * @return the string
     */
    public static String stripAnyColorCodes(String input) {
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', input));
    }

    /**
     * Translate any color codes from the input (usually '&').
     *
     * @param input the input
     * @return the string
     */
    public static String translateAnyColorCodes(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * Gets a String[] that will return arguments separated by spaces, except words that are surrounded in quotes will
     * be combined into one index in the returned String[]. Using a /" will not be parsed as a being/end quote for
     * and argument.
     *
     * @param args the args
     * @return the string []
     */
    @SuppressWarnings("StringConcatenationInLoop") // Idc
    public static String[] getArgsQuoted(String[] args) {
        ArrayList<String> finalArgs = new ArrayList<>();
        boolean currentlyInsideQuotes = false;
        String currentArgString = "";
        for (String arg : args) {
            if (arg.startsWith("\"") && !arg.startsWith("/\"")) {
                if (!currentlyInsideQuotes) {
                    currentlyInsideQuotes = true;
                    currentArgString += arg.substring(1) + " ";
                } else {
                    finalArgs.add(arg);
                }
            } else if (arg.endsWith("\"") && !arg.endsWith("/\"")) {
                if (currentlyInsideQuotes) {
                    currentlyInsideQuotes = false;
                    currentArgString += arg.substring(0, arg.length() - 1);
                    finalArgs.add(currentArgString);
                    currentArgString = "";
                } else {
                    finalArgs.add(arg);
                }
            } else if (arg.equals("\"")) {
                if (currentlyInsideQuotes) {
                    currentlyInsideQuotes = false;
                    currentArgString += " ";
                    finalArgs.add(currentArgString);
                    currentArgString = "";
                } else {
                    currentlyInsideQuotes = true;
                    currentArgString += " ";
                }
            } else { // No quotes found on current arg
                if (currentlyInsideQuotes) {
                    currentArgString += arg + " ";
                } else {
                    finalArgs.add(arg);
                }
            }
        }
        return finalArgs.toArray(new String[0]);
    }
}
