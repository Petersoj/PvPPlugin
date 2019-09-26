package net.jacobpeterson.pvpplugin.arena.arenas.ffa;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import org.bukkit.Location;

import java.io.Serializable;

public class FFAArena extends Arena implements Serializable {

    private Location spawnLocation;
    private Location leaveLocation;

    /**
     * Instantiates a new FFA arena.
     *
     * @param arenaManager the arena manager
     * @param name         the name
     */
    public FFAArena(ArenaManager arenaManager, String name) {
        super(arenaManager, name);
    }

    @Override
    public ArenaItemStack getArenaItemStack() {
        return arenaItemStack;
    }

    @Override
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

    /**
     * Gets leave location.
     *
     * @return the leave location
     */
    public Location getLeaveLocation() {
        return leaveLocation;
    }

    /**
     * Sets leave location.
     *
     * @param leaveLocation the leave location
     */
    public void setLeaveLocation(Location leaveLocation) {
        this.leaveLocation = leaveLocation;
    }
}
