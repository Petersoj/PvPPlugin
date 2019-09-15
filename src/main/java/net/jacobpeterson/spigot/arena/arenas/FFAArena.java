package net.jacobpeterson.spigot.arena.arenas;

import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.ArenaManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
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

    @Override
    public void join(PvPPlayer pvpPlayer) {
        super.join(pvpPlayer);

        pvpPlayer.getPlayer().teleport(spawnLocation);
        pvpPlayer.getPlayerArenaManager().setCurrentArena(this);
    }

    @Override
    public void leave(PvPPlayer pvpPlayer) {
        super.leave(pvpPlayer);

        pvpPlayer.getPlayer().teleport(leaveLocation);
        pvpPlayer.getPlayerArenaManager().setCurrentArena(null);
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
