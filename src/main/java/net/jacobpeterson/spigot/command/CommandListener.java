package net.jacobpeterson.spigot.command;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.data.PlayerData;
import net.jacobpeterson.spigot.util.ChatUtil;
import net.jacobpeterson.spigot.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

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
                    return handleSetLobbyCommand(pvpPlayer);
                case "lobby":
                    this.handleLobbyCommand(pvpPlayer);
                    break;
                case "record":
                    this.handleRecordCommand(pvpPlayer, args);
                    break;
                case "save":
                    break;
                case "scoreboard":
                    break;
                case "accept":
                    break;

                case "ffa":
                    this.handleFFACommand(pvpPlayer, args);
                    break;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Handle set lobby command.
     *
     * @param pvpPlayer the pvp player
     * @return if the command was successful
     */
    public boolean handleSetLobbyCommand(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();
        Location playerLocation = player.getLocation();
        World levelWorld = Bukkit.getWorlds().get(0); // 0th world is the default world

        levelWorld.setSpawnLocation(playerLocation.getBlockX(), playerLocation.getBlockY(),
                playerLocation.getBlockZ());

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully set the lobby spawn!");

        return true;
    }

    /**
     * Handle lobby command.
     *
     * @param pvpPlayer the pvp player
     * @return if the command was successful
     */
    public boolean handleLobbyCommand(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();
        World levelWorld = Bukkit.getWorlds().get(0); // 0th world is the default world
        player.teleport(levelWorld.getSpawnLocation());

        return true;
    }

    /**
     * Handle record command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     */
    public void handleRecordCommand(PvPPlayer pvpPlayer, String[] args) {
        if (args.length > 0) { // show another player's stats
            Player recordPlayer = Bukkit.getPlayer(args[0]);

            if (recordPlayer == null) { // No online player found so have to query DB
                new BukkitRunnable() { // Run async b/c of database querying
                    @Override
                    public void run() {
                        String recordPlayerName = args[0];
                        // We need to fetch the UUID via Mojang API in order to query database b/c player is offline
                        UUID recordPlayerUUID = PlayerUtil.getMojangUUID(recordPlayerName);

                        if (recordPlayerUUID == null) {
                            CommandListener.this.sendSyncRecordMessage(pvpPlayer, null, null);
                        } else {
                            // Fetch playerdata
                            PlayerData recordPlayerData = null;
                            try {
                                recordPlayerData = pvpPlugin.getPlayerManager().getPlayerDataManager()
                                        .selectPlayerDataFromDatabase(recordPlayerUUID);
                            } catch (SQLException exception) {
                                exception.printStackTrace();
                            } finally {
                                CommandListener.this.sendSyncRecordMessage(pvpPlayer,
                                        recordPlayerName, recordPlayerData);
                            }
                        }
                    }
                }.runTaskAsynchronously(pvpPlugin); // Will run async on next tick
            } else {
                PvPPlayer recordPvPPlayer = pvpPlugin.getPlayerManager().getPvPPlayer(recordPlayer);
                this.sendRecordMessage(pvpPlayer, recordPlayer.getName(), recordPvPPlayer.getPlayerData());
            }
        } else { // show personal stats
            this.sendRecordMessage(pvpPlayer, pvpPlayer.getPlayer().getName(), pvpPlayer.getPlayerData());
        }
    }

    /**
     * Send record message.
     *
     * @param pvpPlayer        the pvp player that is receiving the record data
     * @param recordPlayerName the record player name
     * @param recordPlayerData the record player data (can be null for not found)
     */
    private void sendSyncRecordMessage(PvPPlayer pvpPlayer, String recordPlayerName, PlayerData recordPlayerData) {
        new BukkitRunnable() { // Run sendRecordMessage on main bukkit thread
            @Override
            public void run() {
                CommandListener.this.sendRecordMessage(pvpPlayer, recordPlayerName, recordPlayerData);
            }
        }.runTask(pvpPlugin);
    }

    /**
     * Send record message.
     *
     * @param pvpPlayer        the pvp player that is receiving the record data
     * @param recordPlayerName the record player name
     * @param recordPlayerData the record player data (can be null for not found)
     */
    private void sendRecordMessage(PvPPlayer pvpPlayer, String recordPlayerName, PlayerData recordPlayerData) {
        Player player = pvpPlayer.getPlayer();
        PlayerManager playerManager = pvpPlugin.getPlayerManager();

        if (recordPlayerData == null || recordPlayerName == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player record could not be found.");
        } else {
            String prefix = playerManager.getPlayerGroupPrefix(recordPlayerName);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + recordPlayerName +
                    ChatColor.GOLD + "'s Stats" + ChatColor.DARK_GRAY + ":");
            player.sendMessage(ChatColor.GOLD + "Rank" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA +
                    playerManager.getPlayerGroupName(recordPlayerName));
            player.sendMessage(ChatColor.GOLD + "Ranked 1v1 ELO" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA +
                    recordPlayerData.getELO());
            player.sendMessage(ChatColor.GOLD + "Ranked 1v1 Wins" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD +
                    "Losses" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA + recordPlayerData.getRanked1v1Wins() +
                    ChatColor.DARK_GRAY + "/" + ChatColor.AQUA + recordPlayerData.getRanked1v1Losses());
            player.sendMessage(ChatColor.GOLD + "Ranked 1v1 Kills" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD +
                    "Deaths" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA + recordPlayerData.getRanked1v1Kills() +
                    ChatColor.DARK_GRAY + "/" + ChatColor.AQUA + recordPlayerData.getRanked1v1Deaths());
            player.sendMessage(ChatColor.GOLD + "Unranked FFA Kills" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD +
                    "Deaths" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA + recordPlayerData.getUnrankedFFAKills() +
                    ChatColor.DARK_GRAY + "/" + ChatColor.AQUA + recordPlayerData.getUnrankedFFADeaths());
            player.sendMessage(ChatColor.GOLD + "Team PvP Wins" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA +
                    recordPlayerData.getTeamPvPWins());
            player.sendMessage(ChatColor.GOLD + "Team PvP Losses" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA +
                    recordPlayerData.getTeamPvPLosses());
        }
    }

    /**
     * Handle arena add command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     */
    public void handleArenaAddCommand(PvPPlayer pvpPlayer, String[] args) {
        // TODO make sure info that \n should be used for multiple lines
    }

    /**
     * Handle ffa command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleFFACommand(PvPPlayer pvpPlayer, String[] args) {
        if (args.length <= 0) {
            return false;
        }
        String ffaCommand = args[0];
//        if () {
//
//        }
        return true;
    }
}
