package net.jacobpeterson.pvpplugin.gui.guis;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.gui.AbstractInventoryGUI;
import net.jacobpeterson.pvpplugin.itemstack.ItemStackUtil;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.gui.PlayerGUIManager;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

public abstract class ChooseArenaMenu extends AbstractInventoryGUI {

    protected static ItemStack ANY_ITEM;
    protected static ItemStack BACK_ITEM;

    protected final Logger LOGGER;

    protected final PlayerGUIManager playerGUIManager;
    protected String title;
    protected ArrayList<ArenaItemStack> arenaItemStacks;

    /**
     * Instantiates a new 'Choose Arena' menu. This is meant to be extended by other GUI/menus
     * as this is a general viewer.
     *
     * @param playerGUIManager the player gui manager
     * @param title            the title
     */
    public ChooseArenaMenu(PlayerGUIManager playerGUIManager, String title) {
        this.LOGGER = PvPPlugin.getPluginLogger();
        this.playerGUIManager = playerGUIManager;
        this.title = title;
        this.arenaItemStacks = new ArrayList<>();
    }

    @Override
    public void init() {
        if (ANY_ITEM == null) {
            ANY_ITEM = new ItemStack(Material.EMERALD);
            ItemStackUtil.formatLore(ANY_ITEM, true,
                    ChatUtil.boldColor(ChatColor.YELLOW) + "Any",
                    ChatColor.GOLD + "Play on any arena (fastest)");
        }

        if (BACK_ITEM == null) {
            BACK_ITEM = new ItemStack(Material.BOOK);
            ItemStackUtil.formatLore(BACK_ITEM, true,
                    ChatUtil.boldColor(ChatColor.YELLOW) + "Back", (String[]) null);
        }
    }

    @Override
    public void createInventory() {
        // 5 Arena ItemStacks per chest row
        int chestLines = (int) Math.ceil(arenaItemStacks.size() == 0 ? 1 : arenaItemStacks.size() / 5f);
        if (chestLines > 6) { // Max lines for Bukkit Chest GUI is 6
            throw new UnsupportedOperationException("Pagination not implemented! Too many arenas created!");
        }
        inventory = Bukkit.createInventory(null, chestLines * 9, title);

        inventory.setItem(0, ANY_ITEM);
        inventory.setItem(8, BACK_ITEM);

        if (arenaItemStacks != null) {
            int currentIndex = 2;
            int currentLine = 0;
            for (ArenaItemStack arenaItemStack : arenaItemStacks) {
                if (currentIndex > 7) {
                    currentIndex = 2;
                    currentLine++;
                }

                if (arenaItemStack.getItemStack() == null) {
                    LOGGER.warning("No Arena ItemStack representation for arena: " + arenaItemStack.getArena().getName());
                } else {
                    inventory.setItem((currentLine * 9) + currentIndex, arenaItemStack.getItemStack());
                }

                currentIndex++;
            }
        }
    }

    /**
     * Update arena item stacks lore.
     * Used for updating the player-specific 'times played' for an arena
     *
     * @param <A>          the type parameter for the Arena
     * @param <G>          the type parameter for the Game
     * @param gameQueueMap the game queue map
     * @param pvpPlayer    the pvp player
     */
    // Generics required here because polymorphism with generics don't work I guess
    @SuppressWarnings("SuspiciousMethodCalls") // Suppress this warning because we know that value exists in Map
    public <A extends Arena, G extends Game> void updateArenaItemStacks(HashMap<A, LinkedList<G>> gameQueueMap,
                                                                        PvPPlayer pvpPlayer) {
        arenaItemStacks.clear();

        // Loop through all games add update the itemstacks of the games' arenas
        for (Arena arena : gameQueueMap.keySet()) {
            ArenaItemStack arenaItemStack = arena.getArenaItemStack();
            Game currentGame = gameQueueMap.get(arena).peek();

            // Update the ArenaItemStack and add it to the local list
            arenaItemStack.updateItemStack(currentGame, pvpPlayer);
            arenaItemStacks.add(arenaItemStack);
        }

        // Create Inventory with updated ArenaItemStacks
        this.createInventory();
    }

    /**
     * Gets arena item stacks.
     *
     * @return the arena item stacks
     */
    public ArrayList<ArenaItemStack> getArenaItemStacks() {
        return arenaItemStacks;
    }

    /**
     * Sets arena item stacks.
     *
     * @param arenaItemStacks the arena item stacks
     */
    public void setArenaItemStacks(ArrayList<ArenaItemStack> arenaItemStacks) {
        this.arenaItemStacks = arenaItemStacks;
    }
}
