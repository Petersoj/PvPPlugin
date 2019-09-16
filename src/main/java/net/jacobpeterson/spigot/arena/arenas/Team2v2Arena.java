package net.jacobpeterson.spigot.arena.arenas;

import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.ArenaManager;
import org.bukkit.Location;

public class Team2v2Arena extends Arena {

    private Location teamRedSpawnLocation;
    private Location teamBlueSpawnLocation;
    private Location finishLocation;
    private Location spectateLocation;

    /**
     * Instantiates a new Team 2v2 arena.
     *
     * @param arenaManager the arena manager
     * @param name         the name
     */
    public Team2v2Arena(ArenaManager arenaManager, String name) {
        super(arenaManager, name);
    }

    /**
     * Gets team red spawn location.
     *
     * @return the team red spawn location
     */
    public Location getTeamRedSpawnLocation() {
        return teamRedSpawnLocation;
    }

    /**
     * Sets team red spawn location.
     *
     * @param teamRedSpawnLocation the team red spawn location
     */
    public void setTeamRedSpawnLocation(Location teamRedSpawnLocation) {
        this.teamRedSpawnLocation = teamRedSpawnLocation;
    }

    /**
     * Gets team blue spawn location.
     *
     * @return the team blue spawn location
     */
    public Location getTeamBlueSpawnLocation() {
        return teamBlueSpawnLocation;
    }

    /**
     * Sets team blue spawn location.
     *
     * @param teamBlueSpawnLocation the team blue spawn location
     */
    public void setTeamBlueSpawnLocation(Location teamBlueSpawnLocation) {
        this.teamBlueSpawnLocation = teamBlueSpawnLocation;
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

    /**
     * Gets spectate location.
     *
     * @return the spectate location
     */
    public Location getSpectateLocation() {
        return spectateLocation;
    }

    /**
     * Sets spectate location.
     *
     * @param spectateLocation the spectate location
     */
    public void setSpectateLocation(Location spectateLocation) {
        this.spectateLocation = spectateLocation;
    }
}
