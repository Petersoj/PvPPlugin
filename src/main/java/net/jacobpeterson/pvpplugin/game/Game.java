package net.jacobpeterson.pvpplugin.game;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.game.event.AbstractGameEventHandlers;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.inventory.PlayerInventoryManager;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public abstract class Game implements Initializers {

    protected GameManager gameManager;
    protected Arena arena;
    protected boolean inProgress;
    protected AbstractGameEventHandlers gameEventHandler;
    protected ArrayList<PvPPlayer> pvpPlayers;

    /**
     * Instantiates a new Game which represents an instance of a game in an Arena.
     *
     * @param gameManager the game manager
     * @param arena       the arena
     */
    public Game(GameManager gameManager, Arena arena) {
        this.gameManager = gameManager;
        this.arena = arena;
        this.pvpPlayers = new ArrayList<>();
    }

    /**
     * Start the game.
     */
    public abstract void start();

    /**
     * Stop the game.
     */
    public abstract void stop();

    /**
     * Have a player join the arena.
     * Sets the inventory of the player to {@link Arena#getInventory()} and {@link Arena#getArmorInventory()}
     * and sends a join message.
     *
     * @param pvpPlayer the pvp player
     */
    public void join(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();

        pvpPlayers.add(pvpPlayer);

        PlayerInventory playerInventory = player.getInventory();
        if (pvpPlayer.isPremium()) {
            pvpPlayer.getPlayerInventoryManager().loadArenaPersistedInventory(arena);
        } else {
            playerInventory.setContents(arena.getInventory());
            playerInventory.setArmorContents(arena.getArmorInventory());
        }

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD +
                "You successfully joined " + arena.getFormattedName());
        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Type " + ChatColor.AQUA +
                "/leave " + ChatColor.GOLD + "to get back to the lobby.");
    }

    /**
     * Have a player leave the arena.
     * Sets the inventory of the player to {@link PlayerInventoryManager#loadSpawnInventory()} and sends a leave message.
     *
     * @param pvpPlayer the pvp player
     */
    public void leave(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();

        if (pvpPlayers.contains(pvpPlayer)) {
            pvpPlayers.remove(pvpPlayer);
        } else {
            return;
        }

        if (pvpPlayer.isPremium()) {
            pvpPlayer.getPlayerInventoryManager().saveArenaPersistedInventory(arena);
        }

        pvpPlayer.getPlayerInventoryManager().loadSpawnInventory();

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You successfully left the " +
                "FFA Arena");
    }

    /**
     * Gets game manager.
     *
     * @return the game manager
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Gets arena.
     *
     * @return the arena
     */
    public abstract Arena getArena();

    /**
     * Sets arena.
     *
     * @param arena the arena
     */
    public abstract void setArena(Arena arena);

    /**
     * Is in progress boolean.
     *
     * @return the boolean
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Gets game event handler.
     *
     * @return the game event handler
     */
    public AbstractGameEventHandlers getGameEventHandler() {
        return gameEventHandler;
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
