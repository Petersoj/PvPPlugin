package net.jacobpeterson.spigot.command;

import net.jacobpeterson.spigot.PvPPlugin;

public class CommandHandler {

    private PvPPlugin pvpPlugin;

    /**
     * Instantiates a new Command handler.
     *
     * @param pvpPlugin the pvp plugin
     */
    public CommandHandler(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    public void handleArenaAddCommand() {
        // TODO make sure infor that \n should be used for multiple lines
    }

    /**
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PvPPlugin getPVPPlugin() {
        return pvpPlugin;
    }
}
