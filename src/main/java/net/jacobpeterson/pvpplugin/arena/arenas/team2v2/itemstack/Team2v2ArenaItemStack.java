package net.jacobpeterson.pvpplugin.arena.arenas.team2v2.itemstack;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.Team2v2Arena;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.game.team2v2.Team2v2Game;
import net.jacobpeterson.pvpplugin.itemstack.ItemStackUtil;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Team2v2ArenaItemStack extends ArenaItemStack {

    public Team2v2ArenaItemStack(Team2v2Arena team2v2Arena, Material material) {
        super(team2v2Arena, material);
    }

    @Override
    public void updateItemStack(Game currentGame, PvPPlayer pvpPlayer) {
        super.updateItemStack(currentGame, pvpPlayer);

        if (currentGame != null && !(currentGame instanceof Team2v2Game)) {
            throw new IllegalArgumentException("currentGame must be a Team2v2Game");
        }
        Team2v2Game currentTeam2v2Game = (Team2v2Game) currentGame;

        int currentPlayingIndentSize = 19; // Used to indent a second line for another teams' player names
        String currentlyPlayingStringLine = ChatColor.GOLD + "Currently Playing" + ChatColor.GRAY + ": ";
        if (currentTeam2v2Game != null) {
//            PvPPlayer firstOpponent = currentTeam2v2Game.getPvPPlayers().get(0);
//            PvPPlayer secondOpponent = currentTeam2v2Game.getPvPPlayers().get(1);
//
//            // Set currentlyPlayingStringLine to be
//            if (firstOpponent != null && secondOpponent != null) {
//                currentlyPlayingStringLine += firstOpponent.getPrefixedName() + ChatColor.GRAY + " vs. " +
//                        secondOpponent.getPrefixedName();
//            } else if (firstOpponent != null) {
//                currentlyPlayingStringLine += firstOpponent.getPrefixedName();
//            } else if (secondOpponent != null) {
//                currentlyPlayingStringLine += secondOpponent.getPrefixedName();
//            } else {
//                currentlyPlayingStringLine += ChatColor.AQUA + "None";
//            }
        } else {
            currentlyPlayingStringLine += ChatColor.AQUA + "None";
        }

        ItemStackUtil.formatLore(itemStack, true, ChatUtil.boldColor(ChatColor.YELLOW) + arena.getName(),
                (String[]) super.getStandardLoreFormat(pvpPlayer, new String[]{currentlyPlayingStringLine}).toArray());
    }

    @Override
    public ArenaItemStack clone() {
        return new Team2v2ArenaItemStack((Team2v2Arena) arena, material);
    }

    @Override
    public Team2v2Arena getArena() {
        return (Team2v2Arena) arena;
    }

    @Override
    public void setArena(Arena arena) {
        if (arena != null && !(arena instanceof Team2v2Arena)) {
            throw new IllegalArgumentException("arena must be Team2v2Arena");
        }
        this.arena = arena;
    }
}
