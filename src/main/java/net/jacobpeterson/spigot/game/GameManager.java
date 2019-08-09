package net.jacobpeterson.spigot.game;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.ArenaManager;
import net.jacobpeterson.spigot.game.games.team2v2.Team2v2Game;
import net.jacobpeterson.spigot.player.PvPPlayer;
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
     * Gets team 2v2 game.
     *
     * @param teamLeader the team leader
     * @return the team 2v2 game
     */
    public Team2v2Game getTeam2v2Game(PvPPlayer teamLeader) {
//        for (Team2v2Arena team2v2Arena : arenaManager.getTeam2v2Arenas()) {
//            team2v2Arena.getGameQueue();
//        }
        // TODO finish method
        return null;
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
