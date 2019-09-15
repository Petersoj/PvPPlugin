package net.jacobpeterson.spigot.player.arena;

import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;

public class PlayerArenaManager implements Initializers {

    private PvPPlayer pvpPlayer;
    private Arena currentArena;

    /**
     * Instantiates a new PlayerArenaManager which is used to manage Arenas on a per-player basis.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerArenaManager(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
    }

    @Override
    public void init() {

    }

    @Override
    public void deinit() {

    }

    /**
     * Gets pvp player.
     *
     * @return the pvp player
     */
    public PvPPlayer getPvPPlayer() {
        return pvpPlayer;
    }

    /**
     * Gets current arena.
     *
     * @return the current arena
     */
    public Arena getCurrentArena() {
        return currentArena;
    }

    /**
     * Sets current arena.
     *
     * @param currentArena the current arena
     */
    public void setCurrentArena(Arena currentArena) {
        this.currentArena = currentArena;
    }
}
