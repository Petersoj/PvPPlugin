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
import java.util.HashMap;

public abstract class Game implements Initializers {

    protected GameManager gameManager;
    protected Arena arena;
    protected String name;
    protected boolean inProgress;
    protected boolean duel;
    protected ArrayList<PvPPlayer> invitedPlayers;
    protected AbstractGameEventHandlers gameEventHandlers;
    protected ArrayList<PvPPlayer> pvpPlayers;

    /**
     * Instantiates a new Game which represents an instance of a game in an Arena.
     *
     * @param gameManager the game manager
     * @param arena       the arena
     * @param name        the name
     */
    public Game(GameManager gameManager, Arena arena, String name) {
        this.gameManager = gameManager;
        this.arena = arena;
        this.name = name;
        this.inProgress = false;
        this.duel = false;
        this.invitedPlayers = new ArrayList<>();
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

        // Load/set the player inventory
        PlayerInventory playerInventory = player.getInventory();
        boolean loadDefaultInventory = true;
        if (pvpPlayer.isPremium()) {
            loadDefaultInventory = !pvpPlayer.getPlayerInventoryManager().loadArenaPersistedInventory(arena);
        }
        if (loadDefaultInventory) {
            playerInventory.setContents(arena.getInventory());
            playerInventory.setArmorContents(arena.getArmorInventory());
        }

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD +
                "You successfully joined " + arena.getFormattedName());
        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Type " + ChatColor.AQUA +
                "/leave" + ChatColor.GOLD + " to get back to the lobby.");
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

        // Update the player stats
        HashMap<Arena, Integer> arenaTimesPlayedMap = pvpPlayer.getPlayerData().getArenaTimesPlayedMap();
        int previousTimesPlayed = arenaTimesPlayedMap.get(arena);
        arenaTimesPlayedMap.put(arena, previousTimesPlayed + 1);

        // Save the player inventory if premium
        if (pvpPlayer.isPremium()) {
            pvpPlayer.getPlayerInventoryManager().saveArenaPersistedInventory(arena);
        }

        pvpPlayer.getPlayerInventoryManager().loadSpawnInventory();

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You successfully left " + name);
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Is in progress boolean.
     *
     * @return the boolean
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Is duel boolean.
     *
     * @return the boolean
     */
    public boolean isDuel() {
        return duel;
    }

    /**
     * Sets duel.
     *
     * @param duel the duel
     */
    public void setDuel(boolean duel) {
        this.duel = duel;
    }

    /**
     * Gets invited players.
     *
     * @return the invited players
     */
    public ArrayList<PvPPlayer> getInvitedPlayers() {
        return invitedPlayers;
    }

    /**
     * Sets invited players.
     *
     * @param invitedPlayers the invited players
     */
    public void setInvitedPlayers(ArrayList<PvPPlayer> invitedPlayers) {
        this.invitedPlayers = invitedPlayers;
    }

    /**
     * Gets game event handlers.
     *
     * @return the game event handlers
     */
    public AbstractGameEventHandlers getGameEventHandlers() {
        return gameEventHandlers;
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
