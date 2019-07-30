package net.jacobpeterson.spigot.player.gui;

import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;

public class PlayerGUIManager implements Initializers {

    private PvPPlayer pvpPlayer;

    /**
     * Instantiates a new Player GUI Manager which is used to handle player-specific InventoryGUIs.
     *
     * @param pvpPlayer the pvp player
     */
    public PlayerGUIManager(PvPPlayer pvpPlayer) {
        this.pvpPlayer = pvpPlayer;
    }

    @Override
    public void init() {

    }

    @Override
    public void deinit() {
    }
}
