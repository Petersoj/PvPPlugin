package net.jacobpeterson.spigot.arena;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.arenas.FFAArena;
import net.jacobpeterson.spigot.arena.arenas.Ranked1v1Arena;
import net.jacobpeterson.spigot.arena.arenas.Team2v2Arena;
import net.jacobpeterson.spigot.arena.data.ArenaDataManager;
import net.jacobpeterson.spigot.util.Initializers;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The type Arena manager.
 */
public class ArenaManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private ArenaDataManager arenaDataManager;
    private ArrayList<FFAArena> ffaArenas;
    private ArrayList<Ranked1v1Arena> ranked1v1Arenas;
    private ArrayList<Team2v2Arena> team2v2Arenas;

    public ArenaManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.arenaDataManager = new ArenaDataManager(this);
        // Instantiate these to empty Arraylists because we need the types via getClass() for Gson
        this.ffaArenas = new ArrayList<>();
        this.ranked1v1Arenas = new ArrayList<>();
        this.team2v2Arenas = new ArrayList<>();
    }

    @Override
    public void init() throws IOException {
        arenaDataManager.init();
    }

    @Override
    public void deinit() {
        arenaDataManager.deinit();
    }

    /**
     * Checks if arena name already exists.
     *
     * @return whether or not the arena exists boolean
     */
    public boolean doesArenaExist(String name) {
        // TODO check if name == arena.getName();
        for (FFAArena ffaArena : ffaArenas) {

        }
        for (Ranked1v1Arena ranked1v1Arena : ranked1v1Arenas) {

        }
        for (Team2v2Arena team2v2Arena : team2v2Arenas) {

        }
        return false;
    }

    /**
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PvPPlugin getPvPPlugin() {
        return pvpPlugin;
    }

    /**
     * Gets arena data manager.
     *
     * @return the arena data manager
     */
    public ArenaDataManager getArenaDataManager() {
        return arenaDataManager;
    }

    /**
     * Gets ffa arenas.
     *
     * @return the ffa arenas
     */
    public ArrayList<FFAArena> getFFAArenas() {
        return ffaArenas;
    }

    /**
     * Sets ffa arenas.
     *
     * @param ffaArenas the ffa arenas
     */
    public void setFFAArenas(ArrayList<FFAArena> ffaArenas) {
        this.ffaArenas = ffaArenas;
    }

    /**
     * Gets ranked 1v1 arenas.
     *
     * @return the ranked 1v1 arenas
     */
    public ArrayList<Ranked1v1Arena> getRanked1v1Arenas() {
        return ranked1v1Arenas;
    }

    /**
     * Sets ranked 1v1 arenas.
     *
     * @param ranked1v1Arenas the ranked 1v1 arenas
     */
    public void setRanked1v1Arenas(ArrayList<Ranked1v1Arena> ranked1v1Arenas) {
        this.ranked1v1Arenas = ranked1v1Arenas;
    }

    /**
     * Gets team 2v2 arenas.
     *
     * @return the team 2v2 arenas
     */
    public ArrayList<Team2v2Arena> getTeam2v2Arenas() {
        return team2v2Arenas;
    }

    /**
     * Sets team 2v2 arenas.
     *
     * @param team2v2Arenas the team 2v2 arenas
     */
    public void setTeam2v2Arenas(ArrayList<Team2v2Arena> team2v2Arenas) {
        this.team2v2Arenas = team2v2Arenas;
    }
}