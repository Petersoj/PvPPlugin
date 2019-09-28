package net.jacobpeterson.pvpplugin.arena.arenas.team2v2;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.itemstack.Team2v2ArenaItemStack;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import org.bukkit.Location;

import java.io.Serializable;

public class Team2v2Arena extends Arena implements Serializable {

    private Location teamRedSpawnLocation;
    private Location teamBlueSpawnLocation;
    private Location finishLocation;
    private Location spectateLocation;

    /**
     * Instantiates a new Team2v2Arena.
     *
     * @param arenaManager   the arena manager
     * @param nameIdentifier the name identifier (no color codes)
     * @param formattedName  the formatted name (can have color codes)
     */
    public Team2v2Arena(ArenaManager arenaManager, String nameIdentifier, String formattedName) {
        super(arenaManager, nameIdentifier, formattedName);
    }

    @Override
    public Team2v2ArenaItemStack getArenaItemStack() {
        return (Team2v2ArenaItemStack) arenaItemStack;
    }

    @Override
    public void setArenaItemStack(ArenaItemStack arenaItemStack) {
        if (arenaItemStack != null && !(arenaItemStack instanceof Team2v2ArenaItemStack)) {
            throw new IllegalArgumentException("Arena must be a Team2v2ArenaItemStack!");
        }
        this.arenaItemStack = arenaItemStack;
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
