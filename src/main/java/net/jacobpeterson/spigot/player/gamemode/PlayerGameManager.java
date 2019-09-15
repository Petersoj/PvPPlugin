package net.jacobpeterson.spigot.player.gamemode;

import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.game.games.team2v2.Team2v2;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;

public class PlayerGameManager implements Initializers {

    private PvPPlayer pvpPlayer;
    private Arena currentArena;
    private Team2v2 team2v2;

    /**
     * Instantiates a new PlayerGameManager which is used to manage gamemodes on a per-player basis.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerGameManager(PvPPlayer pvpPlayer) {
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
}
