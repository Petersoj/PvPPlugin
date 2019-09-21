package net.jacobpeterson.pvpplugin.arena.arenas;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import org.bukkit.Location;

public class FFAArena extends Arena {

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