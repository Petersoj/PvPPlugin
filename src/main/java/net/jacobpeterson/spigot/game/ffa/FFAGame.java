package net.jacobpeterson.spigot.game.ffa;

import net.jacobpeterson.spigot.arena.arenas.FFAArena;
import net.jacobpeterson.spigot.game.Game;
import net.jacobpeterson.spigot.player.PvPPlayer;

public class FFAGame extends Game {

    /**
     * Instantiates a new FFA game which serves as a game instance for the Arena.
     *
     * @param ffaArena the ffa arena
     */
    public FFAGame(FFAArena ffaArena) {
        super(ffaArena);
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

        pvpPlayer.getPlayer().teleport(this.getArena().getSpawnLocation());
        pvpPlayer.getPlayerGameManager().setCurrentGame(this);
    }

    @Override
    public void leave(PvPPlayer pvpPlayer) {
        super.leave(pvpPlayer);

        pvpPlayer.getPlayer().teleport(this.getArena().getLeaveLocation());
        pvpPlayer.getPlayerGameManager().setCurrentGame(null);
    }

    @Override
    public FFAArena getArena() {
        return (FFAArena) super.getArena();
    }
}
