package net.jacobpeterson.spigot.arena;

import net.jacobpeterson.spigot.arena.itemstack.ArenaItemStack;
import org.bukkit.Location;

public abstract class Arena {

    private String name;
    private ArenaItemStack arenaItemStack;
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
