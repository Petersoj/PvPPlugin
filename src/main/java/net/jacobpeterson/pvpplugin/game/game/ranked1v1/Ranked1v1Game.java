package net.jacobpeterson.pvpplugin.game.game.ranked1v1;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.GameManager;
import net.jacobpeterson.pvpplugin.game.game.ranked1v1.listener.Ranked1v1EventHandlers;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.Initializers;
import net.jacobpeterson.pvpplugin.util.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Ranked1v1Game extends Game implements Initializers {

    private PvPPlayer dueler;
    private PvPPlayer acceptor;

    /**
     * Instantiates a new Ranked 1v1 Game which serves as a game instance for the Arena.
     *
     * @param gameManager    the game manager
     * @param ranked1v1Arena the ranked 1 v 1 arena
     */
    public Ranked1v1Game(GameManager gameManager, Ranked1v1Arena ranked1v1Arena) {
        super(gameManager, ranked1v1Arena, "Ranked 1v1");

        this.gameEventHandlers = new Ranked1v1EventHandlers(this);
    }

    @Override
    public void init() {
        teleportCountDownRunnable = new BukkitRunnable() {
            int teleportCountDown = 4;

            @Override
            public void run() {
                if (teleportCountDown == 0) {
                    this.cancel();
                    // The rest is handled in the Game#startCountDownRunnable
                } else {
                    teleportCountDown--;
                }

                String teleportCountDownString = String.valueOf(teleportCountDown);
                String subtitleString = ChatColor.WHITE + "second" + (teleportCountDown != 1 ? "s" : "") +
                        " to arena teleport!";

                PlayerUtil.sendTitle(dueler.getPlayer(), teleportCountDownString, subtitleString, 0, 20, 0);
                PlayerUtil.sendTitle(acceptor.getPlayer(), teleportCountDownString, subtitleString, 0, 20, 0);
            }
        };

        this.startCountDownRunnable = new BukkitRunnable() {
            @Override
            public void run() {

            }
        };
    }

    @Override
    public void deinit() {
    }

    @Override
    public void start() {
        inProgress = true;

        teleportCountDownRunnable.runTaskTimer(gameManager.getPvPPlugin(), 0, 20);
        startCountDownRunnable.runTaskTimer(gameManager.getPvPPlugin(), 20 * 4, 20);
    }

    @Override
    public void stop() {
        inProgress = false;

        teleportCountDownRunnable.cancel();
        startCountDownRunnable.cancel();
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

    @Override
    public void setArena(Arena arena) {
        if (arena != null && !(arena instanceof Ranked1v1Arena)) {
            throw new IllegalArgumentException("Arena must be a Ranked1v1Arena!");
        }
        this.arena = arena;
    }

    /**
     * Gets dueler.
     *
     * @return the dueler
     */
    public PvPPlayer getDueler() {
        return dueler;
    }

    /**
     * Sets dueler.
     *
     * @param dueler the dueler
     */
    public void setDueler(PvPPlayer dueler) {
        this.dueler = dueler;
    }

    /**
     * Gets acceptor.
     *
     * @return the acceptor
     */
    public PvPPlayer getAcceptor() {
        return acceptor;
    }

    /**
     * Sets acceptor.
     *
     * @param acceptor the acceptor
     */
    public void setAcceptor(PvPPlayer acceptor) {
        this.acceptor = acceptor;
    }
}
