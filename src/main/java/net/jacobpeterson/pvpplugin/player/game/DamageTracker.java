package net.jacobpeterson.pvpplugin.player.game;

import net.jacobpeterson.pvpplugin.player.PvPPlayer;

public class DamageTracker {

    private PvPPlayer lastDamagingPvPPlayer;
    private Object lastDamagingObject;
    private double lastDamagingAmount;

    /**
     * Instantiates a new Damage tracker which is used to track the player, object, and amount of damage inflicted
     * upon a player.
     */
    public DamageTracker() {
    }

    /**
     * Gets last damaging pvp player.
     *
     * @return the last damaging pvp player
     */
    public PvPPlayer getLastDamagingPvPPlayer() {
        return lastDamagingPvPPlayer;
    }

    /**
     * Sets last damaging pvp player.
     *
     * @param lastDamagingPvPPlayer the last damaging pvp player
     */
    public void setLastDamagingPvPPlayer(PvPPlayer lastDamagingPvPPlayer) {
        this.lastDamagingPvPPlayer = lastDamagingPvPPlayer;
    }

    /**
     * Gets last damaging object.
     *
     * @return the last damaging object
     */
    public Object getLastDamagingObject() {
        return lastDamagingObject;
    }

    /**
     * Sets last damaging object.
     *
     * @param lastDamagingObject the last damaging object
     */
    public void setLastDamagingObject(Object lastDamagingObject) {
        this.lastDamagingObject = lastDamagingObject;
    }

    /**
     * Gets last damaging amount.
     *
     * @return the last damaging amount
     */
    public double getLastDamagingAmount() {
        return lastDamagingAmount;
    }

    /**
     * Sets last damaging amount.
     *
     * @param lastDamagingAmount the last damaging amount
     */
    public void setLastDamagingAmount(double lastDamagingAmount) {
        this.lastDamagingAmount = lastDamagingAmount;
    }
}
