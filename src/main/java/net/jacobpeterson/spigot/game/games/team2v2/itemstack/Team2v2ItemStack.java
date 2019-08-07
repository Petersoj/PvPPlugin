package net.jacobpeterson.spigot.game.games.team2v2.itemstack;

import net.jacobpeterson.spigot.game.games.team2v2.Team2v2;
import net.jacobpeterson.spigot.util.Initializers;
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
     */
    public void updateItemStack() {
        // TODO
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
