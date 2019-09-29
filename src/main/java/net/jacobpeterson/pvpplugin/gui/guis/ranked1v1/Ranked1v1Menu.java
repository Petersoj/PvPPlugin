package net.jacobpeterson.pvpplugin.gui.guis.ranked1v1;

import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.game.ranked1v1.Ranked1v1Game;
import net.jacobpeterson.pvpplugin.gui.guis.ChooseArenaMenu;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.game.PlayerGameManager;
import net.jacobpeterson.pvpplugin.player.gui.PlayerGUIManager;
import net.jacobpeterson.pvpplugin.util.CharUtil;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class Ranked1v1Menu extends ChooseArenaMenu {

    public Ranked1v1Menu(ArenaManager arenaManager, PlayerGUIManager playerGUIManager) {
        super(arenaManager, playerGUIManager,
                ChatColor.DARK_GRAY + "Play " + CharUtil.DOUBLE_RIGHT_ARROW + " Ranked 1v1");
    }

    @Override
    public void onInventoryClickEvent(PvPPlayer pvpPlayer, InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = pvpPlayer.getPlayer();
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null || currentItem.getType() == Material.AIR) {
            return;
        }


        if (currentItem.equals(ANY_ITEM)) {
            this.handleGUIArenaJoin(pvpPlayer, arenaManager.getRandomRanked1v1Arena());
            player.closeInventory();
        } else if (currentItem.equals(BACK_ITEM)) {
            player.openInventory(playerGUIManager.getGUIManager().getMainMenu().getInventory());
        } else {
            for (ArenaItemStack arenaItemStack : arenaItemStacks) {
                if (arenaItemStack.getItemStack().equals(currentItem)) {
                    this.handleGUIArenaJoin(pvpPlayer, arenaItemStack.getArena());
                    player.closeInventory();
                }
            }
        }
    }

    private void handleGUIArenaJoin(PvPPlayer pvpPlayer, Arena arena) {
        if (arena == null) {
            return;
        }

        Player player = pvpPlayer.getPlayer();
        PlayerGameManager playerGameManager = pvpPlayer.getPlayerGameManager();
        Game playerCurrentGame = playerGameManager.getCurrentGame();

        // Check if current game is null (be matched with another high ELO)
        if (playerCurrentGame == null) {

        } else { // Game is not null (they are either dueler or acceptor)
            if (!(playerCurrentGame instanceof Ranked1v1Game)) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're currently in another " +
                        "game queue!");
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Use " + ChatColor.WHITE +
                        "/leave queue" + ChatColor.RED + " to leave your current game queue.");
            }

            Ranked1v1Game playerCurrentRanked1v1Game = (Ranked1v1Game) playerCurrentGame;
        }
        /*
        Random Queue (this text shows up if you are in the random queue in 1v1): &6You are in position &b<position> &6of the queue.
        In Queue against player (this will show up if you have been challenged by a player and accepted): &6You and <displayname> &6are in position &b<position> &6of the queue.
         */

    }
}