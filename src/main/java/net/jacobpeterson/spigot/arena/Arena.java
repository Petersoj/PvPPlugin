package net.jacobpeterson.spigot.arena;

import net.jacobpeterson.spigot.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.spigot.game.Game;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public abstract class Arena {

    protected String name;
    protected ArenaItemStack arenaItemStack;
    protected ItemStack[] inventory;
    protected boolean disabled;
    protected boolean premium;
    protected String builtByName;
    protected String description;
    protected transient LinkedList<Game> gameQueue;

    /**
     * Instantiates a new Arena.
     *
     * @param name the name of the Arena
     */
    public Arena(String name) {
        this.name = name;
        this.arenaItemStack = new ArenaItemStack(this, null);
        this.disabled = false;
        this.premium = false;
        this.builtByName = "";
        this.description = "";
        this.gameQueue = new LinkedList<>();
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
