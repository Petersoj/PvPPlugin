package net.jacobpeterson.spigot.game;

import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.inventory.PlayerInventoryManager;
import net.jacobpeterson.spigot.util.ChatUtil;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class Game implements Initializers {

    protected Arena arena;
    protected ArrayList<PvPPlayer> pvpPlayers;

    /**
     * Instantiates a new Game which represents an instance of a game in an Arena.
     *
     * @param arena the arena
     */
    public Game(Arena arena) {
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

        player.getInventory().setContents(arena.getInventory());
        player.getInventory().setArmorContents(arena.getArmorInventory());

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD +
                "You successfully joined " + arena.getName());
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

        pvpPlayer.getPlayerInventoryManager().loadSpawnInventory();

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You successfully left the " +
                "FFA Arena");
    }

    /**
     * Gets arena.
     *
     * @return the arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Sets arena.
     *
     * @param arena the arena
     */
    public void setArena(Arena arena) {
        this.arena = arena;
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
