package net.jacobpeterson.pvpplugin.player.game;

import net.jacobpeterson.pvpplugin.player.PvPPlayer;

public class DamageTracker {

    // Objects that this player is damaged by
    private PvPPlayer lastDamagerPvPPlayer;
    private Object lastDamagerObject;
    private double lastDamagerAmount;

    // Objects that this player inflicts damage
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
     * Gets last damager pvp player.
     *
     * @return the last damager pvp player
     */
    public PvPPlayer getLastDamagerPvPPlayer() {
        return lastDamagerPvPPlayer;
    }

    /**
     * Sets last damager pvp player.
     *
     * @param lastDamagerPvPPlayer the last damager pvp player
     */
    public void setLastDamagerPvPPlayer(PvPPlayer lastDamagerPvPPlayer) {
        this.lastDamagerPvPPlayer = lastDamagerPvPPlayer;
    }

    /**
     * Gets last damager object.
     *
     * @return the last damager object
     */
    public Object getLastDamagerObject() {
        return lastDamagerObject;
    }

    /**
     * Sets last damager object.
     *
     * @param lastDamagerObject the last damager object
     */
    public void setLastDamagerObject(Object lastDamagerObject) {
        this.lastDamagerObject = lastDamagerObject;
    }

    /**
     * Gets last damager amount.
     *
     * @return the last damager amount
     */
    public double getLastDamagerAmount() {
        return lastDamagerAmount;
    }

    /**
     * Sets last damager amount.
     *
     * @param lastDamagerAmount the last damager amount
     */
    public void setLastDamagerAmount(double lastDamagerAmount) {
        this.lastDamagerAmount = lastDamagerAmount;
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
