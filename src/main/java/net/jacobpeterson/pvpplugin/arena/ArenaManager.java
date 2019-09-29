package net.jacobpeterson.pvpplugin.arena;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.arenas.ffa.FFAArena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.Team2v2Arena;
import net.jacobpeterson.pvpplugin.arena.data.ArenaDataManager;
import net.jacobpeterson.pvpplugin.util.Initializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Arena manager.
 */
public class ArenaManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private ArenaDataManager arenaDataManager;
    private FFAArena ffaArena;
    private ArrayList<Ranked1v1Arena> ranked1v1Arenas;
    private ArrayList<Team2v2Arena> team2v2Arenas;

    /**
     * Instantiates a new Arena manager.
     *
     * @param pvpPlugin the pvp plugin
     */
    public ArenaManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.arenaDataManager = new ArenaDataManager(this, pvpPlugin.getGsonManager());
        // Instantiate these to empty Arraylists because we need the types via getClass() for Gson
        this.ranked1v1Arenas = new ArrayList<>();
        this.team2v2Arenas = new ArrayList<>();
    }

    @Override
    public void init() throws IOException {
        arenaDataManager.init();
    }

    @Override
    public void deinit() throws IOException {
        arenaDataManager.deinit();
    }

    /**
     * Gets arena by name.
     *
     * @param nameIdentifier the name identifier
     * @return the arena by name (null if it doesn't exist)
     */
    public Arena getArenaByNameIdentifier(String nameIdentifier) {
        for (Arena arena : getAllArenas()) {
            if (arena.getNameIdentifier().equalsIgnoreCase(nameIdentifier)) {
                return arena;
            }
        }
        return null;
    }

    /**
     * Gets all arenas in a referencing list.
     *
     * @return all arena references
     */
    public ArrayList<Arena> getAllArenas() {
        ArrayList<Arena> arenas = new ArrayList<>();
        if (ffaArena != null) {
            arenas.add(ffaArena);
        }
        if (ranked1v1Arenas != null) {
            arenas.addAll(ranked1v1Arenas);
        }
        if (team2v2Arenas != null) {
            arenas.addAll(team2v2Arenas);
        }
        return arenas;
    }

    /**
     * Gets random ranked 1v1 arena.
     *
     * @return the random ranked 1v1 arena
     */
    public Arena getRandomRanked1v1Arena() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        return ranked1v1Arenas.get(threadLocalRandom.nextInt(ranked1v1Arenas.size()));
    }

    /**
     * Gets random team 2v2 arena.
     *
     * @return the random team 2v2 arena
     */
    public Arena getRandomTeam2v2Arena() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        return team2v2Arenas.get(threadLocalRandom.nextInt(team2v2Arenas.size()));
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
     * Gets ffa arena.
     *
     * @return the ffa arena
     */
    public FFAArena getFFAArena() {
        return ffaArena;
    }

    /**
     * Sets ffa arena.
     *
     * @param ffaArena the ffa arena
     */
    public void setFFAArena(FFAArena ffaArena) {
        this.ffaArena = ffaArena;
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
