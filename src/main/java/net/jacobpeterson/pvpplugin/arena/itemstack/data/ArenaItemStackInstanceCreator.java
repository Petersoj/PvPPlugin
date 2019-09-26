package net.jacobpeterson.pvpplugin.arena.itemstack.data;

import com.google.gson.InstanceCreator;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.itemstack.Ranked1v1ArenaItemStack;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.Team2v2Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.itemstack.Team2v2ArenaItemStack;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;

import java.lang.reflect.Type;

public class ArenaItemStackInstanceCreator implements InstanceCreator<ArenaItemStack> {

    private Type arenaContextType;

    @Override
    public ArenaItemStack createInstance(Type type) {
        if (arenaContextType == Ranked1v1Arena.class) {
            return new Ranked1v1ArenaItemStack();
        } else if (arenaContextType == Team2v2Arena.class) {
            return new Team2v2ArenaItemStack();
        }
        return null;
    }

    /**
     * Gets arena context type (aka the type of the object that is enclosing this ArenaItemStack).
     *
     * @return the arena context type
     */
    public Type getArenaContextType() {
        return arenaContextType;
    }

    /**
     * Sets arena context type (aka the type of the object that is enclosing this ArenaItemStack).
     *
     * @param arenaContextType the arena context type
     */
    public void setArenaContextType(Type arenaContextType) {
        this.arenaContextType = arenaContextType;
    }

}
