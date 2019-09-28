package net.jacobpeterson.pvpplugin.arena;

import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public abstract class Arena implements Serializable {

    protected transient ArenaManager arenaManager;
    protected String nameIdentifier;
    protected String formattedName;
    protected ArenaItemStack arenaItemStack;
    protected ItemStack[] inventory; // Use PlayerInventory#getContents()
    protected ItemStack[] armorInventory; // Use PlayerInventory#getArmorContents()
    protected boolean disabled;
    protected boolean premium;
    protected String builtByName;
    protected String description;

    /**
     * Instantiates a new Arena.
     *
     * @param arenaManager   the arena manager
     * @param nameIdentifier the name identifier (no color codes)
     * @param formattedName  the formatted name (can have color codes)
     */
    public Arena(ArenaManager arenaManager, String nameIdentifier, String formattedName) {
        this.arenaManager = arenaManager;
        this.nameIdentifier = nameIdentifier;
        this.formattedName = formattedName;
        this.disabled = false;
        this.premium = false;
        this.builtByName = "";
        this.description = "";
    }

    /**
     * Gets arena manager.
     *
     * @return the arena manager
     */
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    /**
     * Sets arena manager.
     *
     * @param arenaManager the arena manager
     */
    public void setArenaManager(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    /**
     * Gets name identifier.
     *
     * @return the name identifier
     */
    public String getNameIdentifier() {
        return nameIdentifier;
    }

    /**
     * Sets name identifier.
     *
     * @param nameIdentifier the name identifier
     */
    public void setNameIdentifier(String nameIdentifier) {
        this.nameIdentifier = nameIdentifier;
    }

    /**
     * Gets formatted name.
     *
     * @return the formatted name
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Sets formatted name.
     *
     * @param formattedName the formatted name
     */
    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    /**
     * Gets arena item stack.
     *
     * @return the arena item stack
     */
    public abstract ArenaItemStack getArenaItemStack();

    /**
     * Sets arena item stack.
     *
     * @param arenaItemStack the arena item stack
     */
    public abstract void setArenaItemStack(ArenaItemStack arenaItemStack);

    /**
     * Get inventory item stack [].
     *
     * @return the item stack []
     */
    public ItemStack[] getInventory() {
        return inventory;
    }

    /**
     * Sets inventory.
     *
     * @param inventory the inventory
     */
    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    /**
     * Get armor inventory item stack [].
     *
     * @return the item stack []
     */
    public ItemStack[] getArmorInventory() {
        return armorInventory;
    }

    /**
     * Sets armor inventory.
     *
     * @param armorInventory the armor inventory
     */
    public void setArmorInventory(ItemStack[] armorInventory) {
        this.armorInventory = armorInventory;
    }

    /**
     * Is disabled boolean.
     *
     * @return the boolean
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Sets disabled.
     *
     * @param disabled the disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Is premium boolean.
     *
     * @return the boolean
     */
    public boolean isPremium() {
        return premium;
    }

    /**
     * Sets premium.
     *
     * @param premium the premium
     */
    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    /**
     * Gets built by name.
     *
     * @return the built by name
     */
    public String getBuiltByName() {
        return builtByName;
    }

    /**
     * Sets built by name.
     *
     * @param builtByName the built by name
     */
    public void setBuiltByName(String builtByName) {
        this.builtByName = builtByName;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}