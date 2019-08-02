package net.jacobpeterson.spigot.gui.guis;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.itemstack.ArenaItemStack;
import net.jacobpeterson.spigot.gui.AbstractInventoryGUI;
import net.jacobpeterson.spigot.itemstack.ItemStackUtil;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.data.PlayerData;
import net.jacobpeterson.spigot.player.gui.PlayerGUIManager;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.logging.Logger;

public abstract class ChooseArenaMenu extends AbstractInventoryGUI {

    protected static ItemStack ANY_ITEM;
    protected static ItemStack BACK_ITEM;

    protected final Logger LOGGER;

    protected final PlayerGUIManager playerGUIManager;
    protected String title;
    protected ArrayList<ArenaItemStack> arenaItemStacks;

    /**
     * Instantiates a new 'Choose Arena' menu. This is meant to be extended by other GUI/menus as this is a general viewer.
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
                    CharUtil.boldColor(ChatColor.YELLOW) + "Any",
                    ChatColor.GOLD + "Play on any arena (fastest)");
        }

        if (BACK_ITEM == null) {
            BACK_ITEM = new ItemStack(Material.BOOK);
            ItemStackUtil.formatLore(BACK_ITEM, true,
                    CharUtil.boldColor(ChatColor.YELLOW) + "Back",
                    "");
        }

        this.createInventory();
    }

    @Override
    public void createInventory() {
        // 5 Arena ItemStacks per chest row
        int chestLines = (int) Math.ceil((arenaItemStacks.size() == 0 ? 2 : arenaItemStacks.size()) / 5);
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
     * @param pvpPlayer              the pvp player
     * @param arenas                 the arenas
     * @param createAllNewItemStacks whether or not to update previously created {@link ArenaItemStack} or create new ones
     */
    public void updateArenaItemStacks(PvPPlayer pvpPlayer, ArrayList<Arena> arenas, boolean createAllNewItemStacks) {
        PlayerData playerData = pvpPlayer.getPlayerData();

        if (createAllNewItemStacks) {
            for (Arena arena : arenas) {
                ArenaItemStack arenaItemStack = new ArenaItemStack(arena.getArenaItemStack()); // Copies ArenaItemStack
                Integer timesPlayed = playerData.getArenaTimesPlayedMap().get(arena);

                if (timesPlayed == null) {
                    LOGGER.warning("PlayerData does not contain 'times played' entry for: " + arena.getName() + " arena.");
                    timesPlayed = 0;
                }

                arenaItemStack.updateTimesPlayed(timesPlayed);
                arenaItemStacks.add(arenaItemStack);
            }
        } else {
            for (ArenaItemStack arenaItemStack : arenaItemStacks) {
                Arena arena = arenaItemStack.getArena();
                Integer timesPlayed = playerData.getArenaTimesPlayedMap().get(arena);

                if (timesPlayed == null) {
                    LOGGER.warning("PlayerData does not contain 'times played' entry for: " + arena.getName() + " arena.");
                    timesPlayed = 0;
                }

                arenaItemStack.updateTimesPlayed(timesPlayed);
            }
        }
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
