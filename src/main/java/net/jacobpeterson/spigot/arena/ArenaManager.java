package net.jacobpeterson.spigot.arena;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.arenas.FFAArena;
import net.jacobpeterson.spigot.arena.arenas.Ranked1v1Arena;
import net.jacobpeterson.spigot.arena.arenas.Team2v2Arena;
import net.jacobpeterson.spigot.arena.data.ArenaDataManager;
import net.jacobpeterson.spigot.util.Initializers;

import java.util.ArrayList;

public class ArenaManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private ArenaDataManager arenaDataManager;
    private ArrayList<FFAArena> ffaArenas;
    private ArrayList<Ranked1v1Arena> ranked1v1Arenas;
    private ArrayList<Team2v2Arena> team2v2Arenas;

    public ArenaManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.arenaDataManager = new ArenaDataManager(this);
    }

    @Override
    public void init() {

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
