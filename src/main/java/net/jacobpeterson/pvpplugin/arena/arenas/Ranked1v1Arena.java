package net.jacobpeterson.pvpplugin.arena.arenas;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import org.bukkit.Location;

public class Ranked1v1Arena extends Arena {

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
