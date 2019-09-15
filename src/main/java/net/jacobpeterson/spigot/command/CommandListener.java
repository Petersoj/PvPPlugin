package net.jacobpeterson.spigot.command;

import net.jacobpeterson.spigot.PvPPlugin;
import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.ArenaManager;
import net.jacobpeterson.spigot.arena.arenas.FFAArena;
import net.jacobpeterson.spigot.player.PlayerManager;
import net.jacobpeterson.spigot.player.PvPPlayer;
import net.jacobpeterson.spigot.player.data.PlayerData;
import net.jacobpeterson.spigot.player.data.PlayerDataManager;
import net.jacobpeterson.spigot.util.ChatUtil;
import net.jacobpeterson.spigot.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class CommandListener implements CommandExecutor {

    private final Logger LOGGER;
    private PvPPlugin pvpPlugin;

    /**
     * Instantiates a new Command listener.
     *
     * @param pvpPlugin the pvp plugin
     */
    public CommandListener(PvPPlugin pvpPlugin) {
        this.LOGGER = PvPPlugin.getPluginLogger();
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
                    return this.handleLobbyCommand(pvpPlayer);
                case "record":
                    return this.handleRecordCommand(pvpPlayer, args);
                case "save":
                    return this.handleSaveCommand(pvpPlayer, args);
                case "scoreboard":
                    return this.handleScoreboardCommand(pvpPlayer, args);
                case "accept":
                    return this.handleAcceptCommand(pvpPlayer);
                case "leave":
                    return this.handleLeaveCommand(pvpPlayer);
                case "challenge":

                case "ffa":
                    return this.handleFFACommand(pvpPlayer, args);


                case "1v1":


                case "2v2":

                default:
                    return false;
            }
        } else {
            commandSender.sendMessage("You must be a player to execute PvPPlugin commands.");
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
     * @return if the command was successful
     */
    public boolean handleRecordCommand(PvPPlayer pvpPlayer, String[] args) {
        // Do entire thing async because '/record' command needs ELO Rank which is fetched via Database
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerManager playerManager = pvpPlugin.getPlayerManager();
                PlayerDataManager playerDataManager = playerManager.getPlayerDataManager();

                if (args.length > 0) { // show another player's stats
                    Player recordPlayer = Bukkit.getPlayer(args[0]);

                    // No online player found so have to query DB
                    if (recordPlayer == null) {
                        String recordPlayerName = args[0];
                        PlayerData recordPlayerData = null;
                        int eloRank = -1;
                        try {
                            recordPlayerData = playerDataManager.selectPlayerDataFromDatabase(recordPlayerName);
                            eloRank = playerDataManager.getPlayerELORank(recordPlayerName);
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        } finally {
                            CommandListener.this.sendSyncRecordMessage(pvpPlayer,
                                    recordPlayerName, recordPlayerData, eloRank);
                        }
                    } else { // Found online player
                        PvPPlayer recordPvPPlayer = pvpPlugin.getPlayerManager().getPvPPlayer(recordPlayer);
                        int eloRank = -1;
                        try {
                            // First update the DB
                            playerDataManager.updatePlayerDataInDatabase(recordPvPPlayer);

                            // Now fetch Rank
                            eloRank = playerDataManager.getPlayerELORank(recordPlayer.getName());
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }

                        CommandListener.this.sendSyncRecordMessage(pvpPlayer,
                                recordPvPPlayer.getPlayer().getName(), recordPvPPlayer.getPlayerData(), eloRank);
                    }
                } else { // show personal stats
                    int eloRank = -1;
                    try {
                        // First update the DB
                        playerDataManager.updatePlayerDataInDatabase(pvpPlayer);

                        // Now fetch Rank
                        eloRank = playerDataManager.getPlayerELORank(pvpPlayer.getPlayer().getName());
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                    CommandListener.this.sendSyncRecordMessage(pvpPlayer,
                            pvpPlayer.getPlayer().getName(), pvpPlayer.getPlayerData(), eloRank);
                }
            }
        }.runTaskAsynchronously(pvpPlugin); // Will run async on next tick
        return true;
    }

    /**
     * Send sync record message on main Bukkit Thread.
     *
     * @param pvpPlayer        the pvp player that is receiving the record data
     * @param recordPlayerName the record player name
     * @param recordPlayerData the record player data (can be null for not found)
     * @param eloRank          the elo rank
     */
    private void sendSyncRecordMessage(PvPPlayer pvpPlayer, String recordPlayerName, PlayerData recordPlayerData,
                                       int eloRank) {
        new BukkitRunnable() { // Run sendRecordMessage on main bukkit thread
            @Override
            public void run() {
                CommandListener.this.sendRecordMessage(pvpPlayer, recordPlayerName, recordPlayerData, eloRank);
            }
        }.runTask(pvpPlugin);
    }

    /**
     * Send record message.
     *
     * @param pvpPlayer        the pvp player that is receiving the record data
     * @param recordPlayerName the record player name
     * @param recordPlayerData the record player data (can be null for not found)
     * @param eloRank          the elo rank
     */
    private void sendRecordMessage(PvPPlayer pvpPlayer, String recordPlayerName, PlayerData recordPlayerData,
                                   int eloRank) {
        Player player = pvpPlayer.getPlayer();
        PlayerManager playerManager = pvpPlugin.getPlayerManager();

        if (recordPlayerData == null || recordPlayerName == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player record could not be found.");
        } else {
            String prefix = playerManager.getPlayerGroupPrefix(recordPlayerName);
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.translateAlternateColorCodes('&', prefix) +
                    recordPlayerName + ChatColor.GOLD + "'s Stats" + ChatColor.DARK_GRAY + ":");
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Rank" + ChatColor.DARK_GRAY + ": " +
                    ChatColor.AQUA + playerManager.getPlayerGroupName(recordPlayerName));
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Ranked 1v1 ELO and ELO Rank" +
                    ChatColor.DARK_GRAY + ": " + ChatColor.AQUA + recordPlayerData.getELO() + ChatColor.DARK_GRAY +
                    " - #" + ChatColor.AQUA + eloRank);
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Ranked 1v1 Wins" + ChatColor.DARK_GRAY +
                    "/" + ChatColor.GOLD + "Losses" + ChatColor.DARK_GRAY + ": " + ChatColor.AQUA +
                    recordPlayerData.getRanked1v1Wins() + ChatColor.DARK_GRAY + "/" + ChatColor.AQUA +
                    recordPlayerData.getRanked1v1Losses());
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Ranked 1v1 Kills" +
                    ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + "Deaths" + ChatColor.DARK_GRAY + ": " +
                    ChatColor.AQUA + recordPlayerData.getRanked1v1Kills() +
                    ChatColor.DARK_GRAY + "/" + ChatColor.AQUA + recordPlayerData.getRanked1v1Deaths());
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Unranked FFA Kills" +
                    ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + "Deaths" + ChatColor.DARK_GRAY + ": " +
                    ChatColor.AQUA + recordPlayerData.getUnrankedFFAKills() +
                    ChatColor.DARK_GRAY + "/" + ChatColor.AQUA + recordPlayerData.getUnrankedFFADeaths());
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Team PvP Wins" + ChatColor.DARK_GRAY +
                    ": " + ChatColor.AQUA + recordPlayerData.getTeamPvPWins());
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Team PvP Losses" + ChatColor.DARK_GRAY +
                    ": " + ChatColor.AQUA + recordPlayerData.getTeamPvPLosses());
        }
    }

    /**
     * Handle save command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleSaveCommand(PvPPlayer pvpPlayer, String[] args) {
        return false;
    }

    /**
     * Handle scoreboard command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleScoreboardCommand(PvPPlayer pvpPlayer, String[] args) {
        // Execute ordered query to DB async
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerManager playerManager = pvpPlugin.getPlayerManager();
                PlayerDataManager playerDataManager = playerManager.getPlayerDataManager();
                LinkedHashMap<String, Integer> topELORankingsMap = null;
                try {
                    topELORankingsMap = playerDataManager.getTopELORankings(10);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                final LinkedHashMap<String, Integer> topELORankingsMapReference = topELORankingsMap;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player player = pvpPlayer.getPlayer();

                        if (topELORankingsMapReference == null) {
                            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Could not get " +
                                    "ELO Scoreboard. Check console for errors.");
                        } else {
                            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD +
                                    "Top Players of Siege" + ChatColor.DARK_GRAY + ":");

                            int rankIndex = 1;
                            for (String name : topELORankingsMapReference.keySet()) {
                                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + rankIndex + ". " +
                                        ChatColor.AQUA + topELORankingsMapReference.get(name) +
                                        playerManager.getPlayerGroupPrefix(name) + " " + name);
                                rankIndex++;
                            }
                        }
                    }
                }.runTask(pvpPlugin);
            }
        }.runTaskAsynchronously(pvpPlugin);
        return true;
    }

    /**
     * Handle accept command.
     *
     * @param pvpPlayer the pvp player
     * @return if the command was successful
     */
    public boolean handleAcceptCommand(PvPPlayer pvpPlayer) {
        return false;
    }

    /**
     * Handle leave command boolean.
     *
     * @param pvpPlayer the pvp player
     * @return the boolean
     */
    public boolean handleLeaveCommand(PvPPlayer pvpPlayer) {
        Arena playerCurrentArena = pvpPlayer.getPlayerArenaManager().getCurrentArena();
        if (playerCurrentArena != null) {
            playerCurrentArena.leave(pvpPlayer);
        } else {
            pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're not currently " +
                    "in an arena.");
        }
        return true;
    }

    /**
     * Handle ffa command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleFFACommand(PvPPlayer pvpPlayer, String[] args) {
        Player player = pvpPlayer.getPlayer();
        ArenaManager arenaManager = pvpPlugin.getArenaManager();
        FFAArena currentFFAArena = pvpPlugin.getArenaManager().getFFAArena();

        if (args.length == 0) { // Join FFA Arena
            if (currentFFAArena == null) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "FFA Arena does not exist!");
            } else {
                currentFFAArena.join(pvpPlayer);
            }
        } else {
            String ffaCommand = args[0];

            if (ffaCommand.equalsIgnoreCase("setInv")) {

                if (currentFFAArena == null) {
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "FFA Arena " +
                            "doesn't exist! Set the spawn before setting the inventory for it!");
                } else {
                    currentFFAArena.setInventory(player.getInventory().getContents());
                    currentFFAArena.setArmorInventory(player.getInventory().getArmorContents());
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully " +
                            "set the Inventory for the " + ChatColor.WHITE + currentFFAArena.getName() +
                            ChatColor.GREEN + " arena.");
                }

            } else if (ffaCommand.equalsIgnoreCase("setSpawn")) {

                if (currentFFAArena == null) { // Create the Arena
                    currentFFAArena = new FFAArena(arenaManager, "FFA Arena");
                    arenaManager.setFFAArena(currentFFAArena);
                }

                currentFFAArena.setSpawnLocation(LocationUtil.centerBlockLocation(player.getLocation()));

                // Now save arenas async
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            arenaManager.getArenaDataManager().saveArenas();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        LOGGER.info("Saved arenas.");
                    }
                }.runTaskAsynchronously(pvpPlugin);

                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully " +
                        "set the spawn for FFA Arena.");

            } else if (ffaCommand.equalsIgnoreCase("setLeave")) {

                if (currentFFAArena == null) {
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "FFA Arena " +
                            "doesn't exist! Set the spawn before setting the leave location for it!");
                } else {
                    currentFFAArena.setLeaveLocation(LocationUtil.centerBlockLocation(player.getLocation()));
                }
            }
        }
        return true; // We use our own custom usage messages so don't make Bukkit output them
    }

    /**
     * Handle arena add command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleArenaAddCommand(PvPPlayer pvpPlayer, String[] args) {
        // TODO make sure info that \n should be used for multiple lines

        /*
        ItemStack playerItemStack = player.getInventory().getItemInHand();

                    if (args.length < 5) {
                        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Incorrect number of " +
                                "arguments! Usage: " + ChatColor.WHITE + "/ffa setSpawn <name> <premium?> " +
                                "<built by> <description>");
                        return true;
                    }
                    if (playerItemStack == null || playerItemStack.getType() == Material.AIR) {
                        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must be holding " +
                                "an Item to resemble this Arena in the GUI!");
                        return true;
                    }

                    currentFFAArena = new FFAArena("FFA Arena");
                    currentFFAArena.setSpawnLocation(LocationUtil.centerBlockLocation(player.getLocation()));
         */
        return false;
    }
}
