package net.jacobpeterson.spigot.arena.data;

import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.ArenaManager;

public class ArenaDataManager {

    private ArenaManager arenaManager;

    /**
     * Instantiates a new Arena Data Manager which is used to read/write data for {@link Arena}.
     *
     * @param arenaManager the arena manager
     */
    public ArenaDataManager(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

}
