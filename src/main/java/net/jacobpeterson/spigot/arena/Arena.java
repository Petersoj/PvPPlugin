package net.jacobpeterson.spigot.arena;

import net.jacobpeterson.spigot.arena.itemstack.ArenaItemStack;
import org.bukkit.inventory.ItemStack;

public abstract class Arena {

    private String name;
    private ArenaItemStack arenaItemStack;
    private ItemStack[] inventory;
    private boolean disabled;

    /**
     * Instantiates a new Arena.
     *
     * @param name the name of the Arena
     */
    public Arena(String name) {
        this.name = name;
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
}
