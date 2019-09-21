package net.jacobpeterson.pvpplugin.game.game.ffa;

import net.jacobpeterson.pvpplugin.arena.arenas.FFAArena;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.GameManager;
import net.jacobpeterson.pvpplugin.game.game.ffa.listener.FFAGameEventHandlers;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;

public class FFAGame extends Game {

    /**
     * Instantiates a new FFA game which serves as a game instance for the Arena.
     *
     * @param gameManager the game manager
     * @param ffaArena    the ffa arena
     */
    public FFAGame(GameManager gameManager, FFAArena ffaArena) {
        super(gameManager, ffaArena);

        this.gameEventHandler = new FFAGameEventHandlers(this);
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

        this.updateMainMenuCurrentlyPlaying();

        pvpPlayer.getPlayer().teleport(this.getArena().getSpawnLocation());
        pvpPlayer.getPlayerGameManager().setCurrentGame(this);
    }

    @Override
    public void leave(PvPPlayer pvpPlayer) {
        super.leave(pvpPlayer);

        this.updateMainMenuCurrentlyPlaying();

        pvpPlayer.getPlayer().teleport(this.getArena().getLeaveLocation());
        pvpPlayer.getPlayerGameManager().setCurrentGame(null);
    }

    /**
     * Update main menu currently playing.
     */
    private void updateMainMenuCurrentlyPlaying() {
        gameManager.getPvPPlugin().getGUIManager().getMainMenu().updateFFACurrentlyPlaying(pvpPlayers.size());
    }

    @Override
    public FFAArena getArena() {
        return (FFAArena) arena;
    }
}
