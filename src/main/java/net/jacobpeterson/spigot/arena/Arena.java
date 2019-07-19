package net.jacobpeterson.spigot.arena;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public abstract class Arena {

    private String name;
    private ItemStack itemStack;
    private Location spawnLocation;

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
     * Gets item stack representing this arena.
     *
     * @return the item stack representing this arena
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Sets item stack representing this arena.
     *
     * @param itemStack the item stack to represent this arena
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Gets spawn location.
     *
     * @return the spawn location
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * Sets spawn location.
     *
     * @param spawnLocation the spawn location
     */
    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
