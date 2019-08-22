package net.jacobpeterson.spigot.command;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.data.PlayerData;
import net.jacobpeterson.spigot.util.CharUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandListener implements CommandExecutor {

    private PvPPlugin pvpPlugin;

    /**
     * Instantiates a new Command listener.
     *
     * @param pvpPlugin the pvp plugin
     */
    public CommandListener(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            PvPPlayer pvpPlayer = pvpPlugin.getPlayerManager().getPvPPlayer(player);

            if (pvpPlayer == null) {
                return false;
            }

            String commandName = command.getName();

            switch (commandName) {
                case "setlobby":
                    this.handleSetLobby(pvpPlayer);
                    break;
                case "lobby":
                    this.handleLobby(pvpPlayer);
                    break;
                case "record":
                    this.handleRecord(pvpPlayer, args);
                    break;
            }


            return true;
        } else {
            return false;
        }
    }

    /**
     * Handle set lobby.
     *
     * @param pvpPlayer the pvp player
     */
    public void handleSetLobby(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();
        Location playerLocation = player.getLocation();
        World levelWorld = Bukkit.getWorlds().get(0); // 0th world is the default world

        levelWorld.setSpawnLocation(playerLocation.getBlockX(), playerLocation.getBlockY(),
                playerLocation.getBlockZ());

        player.sendMessage(CharUtil.SERVER_MESSAGE_PREFIX + ChatColor.GREEN + "Successfully set the lobby spawn!");
    }

    /**
     * Handle lobby.
     *
     * @param pvpPlayer the pvp player
     */
    public void handleLobby(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();
        World levelWorld = Bukkit.getWorlds().get(0); // 0th world is the default world
        player.teleport(levelWorld.getSpawnLocation());
    }

    /**
     * Handle record.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     */
    public void handleRecord(PvPPlayer pvpPlayer, String[] args) {
        if (args.length > 0) { // show another player's stats

            new BukkitRunnable() {
                @Override
                public void run() {

                }
            }.runTaskAsynchronously(pvpPlugin);

        } else { // show personal stats

        }
    }

    /**
     * Send record message.
     *
     * @param pvpPlayer  the pvp player
     * @param playerData the player data
     */
    private void sendRecordMessage(PvPPlayer pvpPlayer, PlayerData playerData) {
        Player player = pvpPlayer.getPlayer();
        // player.sendMessage(pvp);

        /*
        <displayname>&6's Stats&8:
        &6Rank&8: &b%vault_rank%
        &6Ranked 1v1 ELO&8/&6Game Rank&8: &b1v1_elo>&8- #&b<game_rank>
        &6Ranked 1v1 Wins&8/&6Losses&8: &b<1v1_wins>&8/&b<1v1_losses>
        &6Unranked FFA Kills&8/&6Deaths&8: &b<ffa_kills>&8/&b<ffa_deaths>
        &6Team PvP Victories&8: &b<teampvp_wins>
        &6Team PvP Defeats&8: &b<teampvp_losses>
         */
    }

    public void handleArenaAddCommand() {
        // TODO make sure info that \n should be used for multiple lines
    }
}
