package net.jacobpeterson.spigot.game.games.ffa;

import net.jacobpeterson.spigot.arena.arenas.FFAArena;
import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.game.GameManager;
import net.jacobpeterson.spigot.player.PvPPlayer;

import java.util.ArrayList;

public class FFAGame extends Game {

    private ArrayList<PvPPlayer> pvpPlayers;

    /**
     * Instantiates a new FFA game.
     *
     * @param gameManager the game manager
     * @param ffaArena    the ffa arena
     */
    public FFAGame(GameManager gameManager, FFAArena ffaArena) {
        super(gameManager, ffaArena);
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
