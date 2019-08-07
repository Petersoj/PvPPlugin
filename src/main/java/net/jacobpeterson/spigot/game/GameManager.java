package net.jacobpeterson.spigot.game;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.ArenaManager;
import net.jacobpeterson.spigot.game.games.ffa.FFAGame;
import net.jacobpeterson.spigot.game.games.ranked1v1.Ranked1v1Game;
import net.jacobpeterson.spigot.game.games.team2v2.Team2v2Game;
import net.jacobpeterson.spigot.util.Initializers;

import java.util.LinkedList;
import java.util.Queue;

public class GameManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private ArenaManager arenaManager;
    private FFAGame ffaGame;
    private Queue<Ranked1v1Game> ranked1v1GameQueue;
    private Queue<Team2v2Game> team2v2GameQueue;

    public GameManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.ranked1v1GameQueue = new LinkedList<>();
        this.team2v2GameQueue = new LinkedList<>();
    }

    @Override
    public void init() {
        this.arenaManager = pvpPlugin.getArenaManager();

        this.ffaGame = new FFAGame(this, arenaManager.getFFAArena()); // FFAArena can be null here, but that's fine
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

    /**
     * Gets ffa game.
     *
     * @return the ffa game
     */
    public FFAGame getFFAGame() {
        return ffaGame;
    }

    /**
     * Gets ranked 1v1 game queue.
     *
     * @return the ranked 1v1 game queue
     */
    public Queue<Ranked1v1Game> getRanked1V1GameQueue() {
        return ranked1v1GameQueue;
    }

    /**
     * Gets team 2v2 game queue.
     *
     * @return the team 2v2 game queue
     */
    public Queue<Team2v2Game> getTeam2v2GameQueue() {
        return team2v2GameQueue;
    }
}
