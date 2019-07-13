package net.jacobpeterson.spigot.command;

import net.jacobpeterson.spigot.PVPPlugin;

public class CommandHandler {

    private PVPPlugin pvpPlugin;

    /**
     * Instantiates a new Command handler.
     *
     * @param pvpPlugin the pvp plugin
     */
    public CommandHandler(PVPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    /**
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PVPPlugin getPvpPlugin() {
        return pvpPlugin;
    }
}
