package net.jacobpeterson.spigot.arena;

import net.jacobpeterson.spigot.arena.game.Game;
import net.jacobpeterson.spigot.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.item.PlayerItemManager;
import net.jacobpeterson.spigot.util.ChatUtil;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public abstract class Arena implements Initializers {

    protected transient ArenaManager arenaManager;
    protected String name;
    protected ArenaItemStack arenaItemStack;
    protected ItemStack[] inventory; // Use PlayerInventory#getContents()
    protected ItemStack[] armorInventory;
    protected boolean disabled;
    protected boolean premium;
    protected String builtByName;
    protected String description;
    protected transient LinkedList<Game> gameQueue; // Transient as there is no need for this to persist

    /**
     * Instantiates a new Arena.
     *
     * @param name the name of the Arena
     */
    public Arena(ArenaManager arenaManager, String name) {
        this.arenaManager = arenaManager;
        this.name = name;
        this.arenaItemStack = new ArenaItemStack(this, null);
        this.disabled = false;
        this.premium = false;
        this.builtByName = "";
        this.description = "";
        this.gameQueue = new LinkedList<>();
    }

    @Override
    public void init() {
        if (gameQueue == null) {
            this.gameQueue = new LinkedList<>();
        }
    }

    @Override
    public void deinit() {
    }


    /**
     * Have a player join the arena.
     * Sets the inventory of the player to {@link Arena#getInventory()} and sends a join message.
     *
     * @param pvpPlayer the pvp player
     */
    public void join(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();

        player.getInventory().setContents(inventory);

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You successfully joined " + name);
        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Type " + ChatColor.AQUA +
                "/leave " + ChatColor.GOLD + "to get back to the lobby.");
    }

    /**
     * Have a player leave the arena.
     * Sets the inventory of the player to {@link PlayerItemManager#loadSpawnInventory()} and sends a leave message.
     *
     * @param pvpPlayer the pvp player
     */
    public void leave(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();

        pvpPlayer.getPlayerItemManager().loadSpawnInventory();

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You successfully left the " +
                "FFA Arena");
    }

    /**
     * Gets arena manager.
     *
     * @return the arena manager
     */
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    /**
     * Sets arena manager.
     *
     * @param arenaManager the arena manager
     */
    public void setArenaManager(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

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
     * Gets arena item stack.
     *
     * @return the arena item stack
     */
    public ArenaItemStack getArenaItemStack() {
        return arenaItemStack;
    }

    /**
     * Sets arena item stack.
     *
     * @param arenaItemStack the arena item stack
     */
    public void setArenaItemStack(ArenaItemStack arenaItemStack) {
        this.arenaItemStack = arenaItemStack;
    }

    /**
     * Get inventory item stack [].
     *
     * @return the item stack []
     */
    public ItemStack[] getInventory() {
        return inventory;
    }

    /**
     * Sets inventory.
     *
     * @param inventory the inventory
     */
    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    /**
     * Get armor inventory item stack [].
     *
     * @return the item stack []
     */
    public ItemStack[] getArmorInventory() {
        return armorInventory;
    }

    /**
     * Sets armor inventory.
     *
     * @param armorInventory the armor inventory
     */
    public void setArmorInventory(ItemStack[] armorInventory) {
        this.armorInventory = armorInventory;
    }

    /**
     * Is disabled boolean.
     *
     * @return the boolean
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Sets disabled.
     *
     * @param disabled the disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Is premium boolean.
     *
     * @return the boolean
     */
    public boolean isPremium() {
        return premium;
    }

    /**
     * Sets premium.
     *
     * @param premium the premium
     */
    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    /**
     * Gets built by name.
     *
     * @return the built by name
     */
    public String getBuiltByName() {
        return builtByName;
    }

    /**
     * Sets built by name.
     *
     * @param builtByName the built by name
     */
    public void setBuiltByName(String builtByName) {
        this.builtByName = builtByName;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets game queue.
     *
     * @return the game queue
     */
    public LinkedList<Game> getGameQueue() {
        return gameQueue;
    }

    /**
     * Sets game queue.
     *
     * @param gameQueue the game queue
     */
    public void setGameQueue(LinkedList<Game> gameQueue) {
        this.gameQueue = gameQueue;
    }
}
