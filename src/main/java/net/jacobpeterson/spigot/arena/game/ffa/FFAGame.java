package net.jacobpeterson.spigot.arena.game.ffa;

import net.jacobpeterson.spigot.arena.arenas.FFAArena;
import net.jacobpeterson.spigot.arena.game.Game;
import net.jacobpeterson.spigot.player.PvPPlayer;

import java.util.ArrayList;

public class FFAGame extends Game {

    private ArrayList<PvPPlayer> pvpPlayers;

    /**
     * Instantiates a new FFA game.
     *
     * @param ffaArena the ffa arena
     */
    public FFAGame(FFAArena ffaArena) {
        super(ffaArena);
        this.pvpPlayers = new ArrayList<>();
    }

    /**
     * Gets pvp players.
     *
     * @return the pvp players
     */
    public ArrayList<PvPPlayer> getPvPPlayers() {
        return pvpPlayers;
    }
}
