package net.jacobpeterson.spigot.game.game.ranked1v1;

import net.jacobpeterson.spigot.arena.arenas.Ranked1v1Arena;
import net.jacobpeterson.spigot.arena.arenas.Team2v2Arena;
import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.game.GameManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;

public class Ranked1v1Game extends Game implements Initializers {

    /**
     * Instantiates a new Ranked 1v1 Game which serves as a game instance for the Arena.
     *
     * @param gameManager  the game manager
     * @param team2v2Arena the team 2v2 arena
     */
    public Ranked1v1Game(GameManager gameManager, Team2v2Arena team2v2Arena) {
        super(gameManager, team2v2Arena);
    }

    @Override
    public void init() {

    }

    @Override
    public void deinit() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void join(PvPPlayer pvpPlayer) {
        super.join(pvpPlayer);
    }

    @Override
    public void leave(PvPPlayer pvpPlayer) {
        super.leave(pvpPlayer);
    }

    @Override
    public Ranked1v1Arena getArena() {
        return (Ranked1v1Arena) arena;
    }
}
