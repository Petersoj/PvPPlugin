package net.jacobpeterson.pvpplugin.gui.guis.ranked1v1;

import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.GameManager;
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

    public Ranked1v1Menu(ArenaManager arenaManager, GameManager gameManager, PlayerGUIManager playerGUIManager) {
        super(arenaManager, playerGUIManager, gameManager,
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

        // Check if player clicked 'Any Arena'
        if (currentItem.equals(ANY_ITEM)) {
            this.handleGUIArenaJoin(pvpPlayer, arenaManager.getRandomRanked1v1Arena());
            player.closeInventory();
        } else if (currentItem.equals(BACK_ITEM)) { // Check if player clicked 'Back'
            player.openInventory(playerGUIManager.getGUIManager().getMainMenu().getInventory());
        } else { // Player clicked an ArenaItemStack
            for (ArenaItemStack arenaItemStack : arenaItemStacks) {
                // Check if clicked ArenaItemStack is one in loop
                if (arenaItemStack.getItemStack().equals(currentItem)) {
                    this.handleGUIArenaJoin(pvpPlayer, (Ranked1v1Arena) arenaItemStack.getArena());
                    player.closeInventory();
                }
            }
        }
    }

    private void handleGUIArenaJoin(PvPPlayer pvpPlayer, Ranked1v1Arena ranked1v1Arena) {
        if (ranked1v1Arena == null) {
            return;
        }

        Player player = pvpPlayer.getPlayer();
        PlayerGameManager playerGameManager = pvpPlayer.getPlayerGameManager();
        Game playerCurrentGame = playerGameManager.getCurrentGame();

        // Check if current game is null (be matched with another similar ELO)
        if (playerCurrentGame == null) {

        } else { // Game is not null (they are either dueler or acceptor)

            // Check if current player's game is in progress
            if (playerCurrentGame.isInProgress()) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're currently in a game!");
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Use " + ChatColor.WHITE +
                        "/leave" + ChatColor.RED + " to leave your current game.");
                return;
            }

            // Check if current game is Ranked1v1Game
            if (!(playerCurrentGame instanceof Ranked1v1Game)) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're currently in another " +
                        "game queue!");
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Use " + ChatColor.WHITE +
                        "/leave queue" + ChatColor.RED + " to leave your current game queue.");
                return;
            }

            Ranked1v1Game playerCurrentRanked1v1Game = (Ranked1v1Game) playerCurrentGame;

            // Check if current player is the acceptor and not dueler
            if (playerCurrentRanked1v1Game.getAcceptor().equals(pvpPlayer) ||
                    !playerCurrentRanked1v1Game.getDueler().equals(pvpPlayer)) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must be the challenger to " +
                        "select an arena!");
                return;
            }

            // Check if opponent doesn't exist
            if (playerCurrentRanked1v1Game.getAcceptor() == null) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must have an opponent to " +
                        "select an arena!");
                return;
            }

            // Dueler can be added to queue now

            // Add the game to the queue
            gameManager.getRanked1v1GameQueueMap().get(ranked1v1Arena).add(playerCurrentRanked1v1Game);
        }
    }
}