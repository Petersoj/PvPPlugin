package net.jacobpeterson.pvpplugin.game;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.arenas.FFAArena;
import net.jacobpeterson.pvpplugin.arena.arenas.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.Team2v2Arena;
import net.jacobpeterson.pvpplugin.game.game.ffa.FFAGame;
import net.jacobpeterson.pvpplugin.game.game.ranked1v1.Ranked1v1Game;
import net.jacobpeterson.pvpplugin.game.game.team2v2.Team2v2Game;
import net.jacobpeterson.pvpplugin.game.event.GameEventHandlersDistributor;
import net.jacobpeterson.pvpplugin.util.Initializers;

import java.util.HashMap;
import java.util.LinkedList;

public class GameManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private GameEventHandlersDistributor gameEventHandlersDistributor;
    private FFAGame ffaGame;
    private HashMap<Ranked1v1Arena, LinkedList<Ranked1v1Game>> ranked1v1GameQueueMap;
    private HashMap<Team2v2Arena, LinkedList<Team2v2Game>> team2v2GameQueueMap;

    public GameManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.gameEventHandlersDistributor = new GameEventHandlersDistributor(this);
        this.ranked1v1GameQueueMap = new HashMap<>();
        this.team2v2GameQueueMap = new HashMap<>();
    }

    @Override
    public void init() {
        this.updateArenaReferences(pvpPlugin.getArenaManager());

        this.gameEventHandlersDistributor.init();
    }

    @Override
    public void deinit() {
        // Stop and deinit all game instances

        // Stop game instance for FFA
        if (ffaGame != null) {
            ffaGame.stop();
            ffaGame.deinit();
        }

        // Loop through all arenas' game queues for Ranked1v1
        for (LinkedList<Ranked1v1Game> ranked1v1GameQueue : ranked1v1GameQueueMap.values()) {
            for (Ranked1v1Game ranked1v1Game : ranked1v1GameQueue) { // Loop through all game instances for arena
                ranked1v1Game.stop();
                ranked1v1Game.deinit();
            }
        }

        // Loop through all arenas' game queues for Team2v2
        for (LinkedList<Team2v2Game> team2v2GameQueue : team2v2GameQueueMap.values()) {
            for (Team2v2Game team2v2Game : team2v2GameQueue) { // Loop through all game instances for arena
                team2v2Game.stop();
                team2v2Game.deinit();
            }
        }

        this.gameEventHandlersDistributor.deinit();
    }

    /**
     * Update arena references. Will basically check if an arena exists and there isn't a game instance or queue for
     * it. If there isn't, create a game instance or queue.
     * Should be called whenever there is an addition/removal of arenas
     *
     * @param arenaManager the arena manager
     */
    @SuppressWarnings("SuspiciousMethodCalls") // Suppress this warning because we know that value exists in Map
    public void updateArenaReferences(ArenaManager arenaManager) {
        FFAArena ffaArena = arenaManager.getFFAArena();

        if (ffaArena != null && ffaGame == null) { // Check if FFAGame needs to be created
            this.ffaGame = new FFAGame(this, ffaArena);
        } else if (ffaGame != null) { // Destroy FFAGame instance because the arena no longer exists
            this.ffaGame.stop();
            this.ffaGame.deinit();
            this.ffaGame = null;
        }

        for (Ranked1v1Arena ranked1v1Arena : arenaManager.getRanked1v1Arenas()) { // Loop through all current Arenas
            LinkedList<Ranked1v1Game> ranked1v1GameQueue = ranked1v1GameQueueMap.get(ranked1v1Arena);

            if (ranked1v1GameQueue == null) { // Check if Ranked1v1Game queue needs to be created
                ranked1v1GameQueueMap.put(ranked1v1Arena, new LinkedList<>());
            } else {
                // Destroy all Ranked1v1Game instances because the arena no longer exists
                for (Ranked1v1Game ranked1v1Game : ranked1v1GameQueue) {
                    ranked1v1Game.stop();
                    ranked1v1Game.deinit();
                }
                ranked1v1GameQueueMap.remove(ranked1v1GameQueue);
            }
        }

        for (Team2v2Arena team2v2Arena : arenaManager.getTeam2v2Arenas()) { // Loop through all current Arenas
            LinkedList<Team2v2Game> team2v2GameQueue = team2v2GameQueueMap.get(team2v2Arena);

            if (team2v2GameQueue == null) { // Check if Team2v2Game queue needs to be created
                team2v2GameQueueMap.put(team2v2Arena, new LinkedList<>());
            } else {
                // Destroy all Team2v2Game instances because the arena no longer exists
                for (Team2v2Game team2v2Game : team2v2GameQueue) {
                    team2v2Game.stop();
                    team2v2Game.deinit();
                }
                ranked1v1GameQueueMap.remove(team2v2GameQueue);
            }
        }
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
     * Gets game event handlers distributor.
     *
     * @return the game event handlers distributor
     */
    public GameEventHandlersDistributor getGameEventHandlersDistributor() {
        return gameEventHandlersDistributor;
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
     * Gets ranked 1v1 game queue map.
     *
     * @return the ranked 1v1 game queue map
     */
    public HashMap<Ranked1v1Arena, LinkedList<Ranked1v1Game>> getRanked1v1GameQueueMap() {
        return ranked1v1GameQueueMap;
    }

    /**
     * Gets team 2v2 game queue map.
     *
     * @return the team 2v2 game queue map
     */
    public HashMap<Team2v2Arena, LinkedList<Team2v2Game>> getTeam2v2GameQueueMap() {
        return team2v2GameQueueMap;
    }
}
