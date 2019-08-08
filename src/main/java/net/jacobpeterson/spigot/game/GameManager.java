package net.jacobpeterson.spigot.game;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.ArenaManager;
import net.jacobpeterson.spigot.util.Initializers;

public class GameManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private ArenaManager arenaManager;

    /**
     * Instantiates a new Game manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GameManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public void init() {
        this.arenaManager = pvpPlugin.getArenaManager();

    }

    @Override
    public void deinit() {
    }

    /**
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PvPPlugin getPvPPlugin() {
        return pvpPlugin;
    }
}
