package net.jacobpeterson.spigot.arena.arenas;

import net.jacobpeterson.spigot.arena.Arena;
import org.bukkit.Location;

public class FFAArena extends Arena {

    private Location spawnLocation;
    private Location leaveLocation;

    /**
     * Instantiates a new FFA arena.
     *
     * @param name the name
     */
    public FFAArena(String name) {
        super(name);
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
