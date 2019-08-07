package net.jacobpeterson.spigot.game.games.team2v2.itemstack;

import net.jacobpeterson.spigot.game.GameManager;
import net.jacobpeterson.spigot.game.games.team2v2.Team2v2;
import net.jacobpeterson.spigot.gui.guis.team2v2.Team2v2Menu;
import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.util.Initializers;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class Team2v2ItemStack implements Initializers {

    private static Wool WHITE_WOOL;
    private static Wool YELLOW_WOOL;
    private static Wool GREEN_WOOL;

    private final Team2v2 team2v2;
    private ItemStack itemStack;

    /**
     * Instantiates a new Team 2v2 ItemStack which is used in the
     * {@link Team2v2Menu}.
     *
     * @param team2v2 the team 2v2
     */
    public Team2v2ItemStack(Team2v2 team2v2) {
        this.team2v2 = team2v2;
    }

    @Override
    public void init() {
        if (WHITE_WOOL != null) {
            WHITE_WOOL = new Wool(DyeColor.WHITE);
        }
        if (YELLOW_WOOL != null) {
            YELLOW_WOOL = new Wool(DyeColor.YELLOW);
        }
        if (GREEN_WOOL != null) {
            GREEN_WOOL = new Wool(DyeColor.GREEN);
        }

        this.itemStack = new ItemStack(Material.WOOL, 1);
    }

    @Override
    public void deinit() {
    }

    /**
     * Updates this Team 2v2 item stack (lore, wool color).
     *
     * @param pvpPlayer   the pvp player that sees this ItemStack
     * @param gameManager the game manager
     */
    public void updateItemStack(PvPPlayer pvpPlayer, GameManager gameManager) {
        int queuePosition = gameManager.getTeam2v2GameQueue().indexOf(team2v2.getTeam2v2Game());
        int queueSize = gameManager.getTeam2v2GameQueue().size();

        String teamJoiningStatus;
        if (team2v2.getProspectivePlayers().contains(pvpPlayer)) {
            teamJoiningStatus = "You are still waiting on a team acceptance.";
            itemStack.setData(YELLOW_WOOL);
        } else if (team2v2.getTeamMember() == pvpPlayer) {
            teamJoiningStatus = "You are a member of this team!";
            itemStack.setData(GREEN_WOOL);
        } else {
            teamJoiningStatus = "Click to request to be added to team.";
            itemStack.setData(WHITE_WOOL);
        }

        ItemStackUtil.formatLore(itemStack, true, ChatColor.GOLD + "Team " + pvpPlayer.getPrefixedName(),
                ChatColor.GOLD + "Type" + ChatColor.GRAY + ": " + ChatColor.AQUA + "2v2",
                ChatColor.GOLD + "Arena" + ChatColor.GRAY + ": " +
                        ChatColor.AQUA + team2v2.getTeam2v2Game().getArena().getName(),
                ChatColor.GOLD + "Queue Position" + ChatColor.GRAY + ": " +
                        ChatColor.AQUA + (queuePosition == -1 ? "None" : queuePosition + 1) +
                        ChatColor.GOLD + " of " + ChatColor.AQUA + queueSize,
                ChatColor.GOLD + "Victories" + ChatColor.GRAY + ": " +
                        team2v2.getTeamLeader().getPlayerData().getTeamPvPWins(),
                ChatColor.GOLD + "Defeats" + ChatColor.GRAY + ": " +
                        team2v2.getTeamLeader().getPlayerData().getTeamPvPLosses(),
                ChatColor.GOLD + teamJoiningStatus);
    }

    /**
     * Gets item stack.
     *
     * @return the item stack
     */
    public ItemStack getItemStack() {
        return itemStack;
    }
}
