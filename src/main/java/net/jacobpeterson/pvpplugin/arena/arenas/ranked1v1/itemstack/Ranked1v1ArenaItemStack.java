package net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.itemstack;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.game.ranked1v1.Ranked1v1Game;
import net.jacobpeterson.pvpplugin.itemstack.ItemStackUtil;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Ranked1v1ArenaItemStack extends ArenaItemStack {

    public Ranked1v1ArenaItemStack() {
        super();
    }

    public Ranked1v1ArenaItemStack(Ranked1v1Arena ranked1v1Arena, Material material) {
        super(ranked1v1Arena, material);
    }

    @Override
    public void updateItemStack(Game currentGame, PvPPlayer pvpPlayer) {
        super.updateItemStack(currentGame, pvpPlayer);

        if (currentGame != null && !(currentGame instanceof Ranked1v1Game)) {
            throw new IllegalArgumentException("currentGame must be a Ranked1v1Game");
        }
        Ranked1v1Game currentRanked1v1Game = (Ranked1v1Game) currentGame;

        String currentlyPlayingStringLine = ChatColor.GOLD + "Currently Playing" + ChatColor.GRAY + ": ";
        if (currentRanked1v1Game != null) {
            PvPPlayer firstOpponent = currentRanked1v1Game.getPvPPlayers().get(0);
            PvPPlayer secondOpponent = currentRanked1v1Game.getPvPPlayers().get(1);

            // Set currentlyPlayingStringLine to be
            if (firstOpponent != null && secondOpponent != null) {
                currentlyPlayingStringLine += firstOpponent.getPrefixedName() + ChatColor.GRAY + " vs. " +
                        secondOpponent.getPrefixedName();
            } else if (firstOpponent != null) {
                currentlyPlayingStringLine += firstOpponent.getPrefixedName();
            } else if (secondOpponent != null) {
                currentlyPlayingStringLine += secondOpponent.getPrefixedName();
            } else {
                currentlyPlayingStringLine += ChatColor.AQUA + "None";
            }
        } else {
            currentlyPlayingStringLine += ChatColor.AQUA + "None";
        }

        ItemStackUtil.formatLore(itemStack, true, ChatUtil.translateAnyColorCodes(arena.getFormattedName()),
                super.getStandardLoreFormat(pvpPlayer, currentlyPlayingStringLine));
    }

    @Override
    public ArenaItemStack clone() {
        return new Ranked1v1ArenaItemStack((Ranked1v1Arena) arena, material);
    }

    @Override
    public Ranked1v1Arena getArena() {
        return (Ranked1v1Arena) arena;
    }

    @Override
    public void setArena(Arena arena) {
        if (arena != null && !(arena instanceof Ranked1v1Arena)) {
            throw new IllegalArgumentException("arena must be Ranked1v1Arena");
        }
        this.arena = arena;
    }
}
