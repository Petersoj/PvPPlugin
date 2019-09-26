package net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.itemstack.Ranked1v1ArenaItemStack;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import org.bukkit.Location;

import java.io.Serializable;

public class Ranked1v1Arena extends Arena implements Serializable {

    private Location spawnLocation1;
    private Location spawnLocation2;
    private Location finishLocation;

    /**
     * Instantiates a new Ranked 1v1 arena.
     *
     * @param arenaManager the arena manager
     * @param name         the name
     */
    public Ranked1v1Arena(ArenaManager arenaManager, String name) {
        super(arenaManager, name);
    }

    @Override
    public Ranked1v1ArenaItemStack getArenaItemStack() {
        return (Ranked1v1ArenaItemStack) arenaItemStack;
    }

    @Override
    public void setArenaItemStack(ArenaItemStack arenaItemStack) {
        if (arenaItemStack != null && !(arenaItemStack instanceof Ranked1v1ArenaItemStack)) {
            throw new IllegalArgumentException("Arena must be a Ranked1v1ArenaItemStack!");
        }
        this.arenaItemStack = arenaItemStack;
    }

    /**
     * Gets spawn location 1.
     *
     * @return the spawn location 1
     */
    public Location getSpawnLocation1() {
        return spawnLocation1;
    }

    /**
     * Sets spawn location 1.
     *
     * @param spawnLocation1 the spawn location 1
     */
    public void setSpawnLocation1(Location spawnLocation1) {
        this.spawnLocation1 = spawnLocation1;
    }

    /**
     * Gets spawn location 2.
     *
     * @return the spawn location 2
     */
    public Location getSpawnLocation2() {
        return spawnLocation2;
    }

    /**
     * Sets spawn location 2.
     *
     * @param spawnLocation2 the spawn location 2
     */
    public void setSpawnLocation2(Location spawnLocation2) {
        this.spawnLocation2 = spawnLocation2;
    }

    /**
     * Gets finish location.
     *
     * @return the finish location
     */
    public Location getFinishLocation() {
        return finishLocation;
    }

    /**
     * Sets finish location.
     *
     * @param finishLocation the finish location
     */
    public void setFinishLocation(Location finishLocation) {
        this.finishLocation = finishLocation;
    }
}
