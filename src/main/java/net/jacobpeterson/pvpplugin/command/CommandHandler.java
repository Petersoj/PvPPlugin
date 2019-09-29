package net.jacobpeterson.pvpplugin.command;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.arenas.ffa.FFAArena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.itemstack.Ranked1v1ArenaItemStack;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.GameManager;
import net.jacobpeterson.pvpplugin.game.game.ranked1v1.Ranked1v1Game;
import net.jacobpeterson.pvpplugin.game.game.team2v2.Team2v2;
import net.jacobpeterson.pvpplugin.game.game.team2v2.Team2v2Game;
import net.jacobpeterson.pvpplugin.player.PlayerManager;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.data.PlayerData;
import net.jacobpeterson.pvpplugin.player.data.PlayerDataManager;
import net.jacobpeterson.pvpplugin.player.game.PlayerGameManager;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import net.jacobpeterson.pvpplugin.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class CommandHandler implements CommandExecutor {

    private final Logger LOGGER;
    private PvPPlugin pvpPlugin;

    /**
     * Instantiates a new Command listener.
     *
     * @param pvpPlugin the pvp plugin
     */
    public CommandHandler(PvPPlugin pvpPlugin) {
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

            // Allows for quoted sequences of args to be one arg
            args = ChatUtil.getArgsQuoted(args);

            String commandName = command.getName();
            switch (commandName) {
                case "setlobby":
                    return handleSetLobbyCommand(pvpPlayer);
                case "lobby":
                    return this.handleLobbyCommand(pvpPlayer);
                case "record":
                    return this.handleRecordCommand(pvpPlayer, args);
                case "save":
                    return this.handleSaveCommand(pvpPlayer);
                case "scoreboard":
                    return this.handleScoreboardCommand(pvpPlayer);
                case "duel":
                    return this.handleDuelCommand(pvpPlayer, args);
                case "accept":
                    return this.handleAcceptCommand(pvpPlayer, args);
                case "leave":
                    return this.handleLeaveCommand(pvpPlayer, args);

                case "ffa":
                    return this.handleFFACommand(pvpPlayer, args);

                case "1v1":
                    return this.handle1v1Command(pvpPlayer, args);

                case "2v2":
                    return this.handle2v2Command(pvpPlayer, args);

                default:
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Unhandled PvPPlugin command!");
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
                            CommandHandler.this.sendSyncRecordMessage(pvpPlayer,
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

                        CommandHandler.this.sendSyncRecordMessage(pvpPlayer,
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
                    CommandHandler.this.sendSyncRecordMessage(pvpPlayer,
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
                CommandHandler.this.sendRecordMessage(pvpPlayer, recordPlayerName, recordPlayerData, eloRank);
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
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatUtil.translateAnyColorCodes(prefix) +
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
     * @return if the command was successful
     */
    public boolean handleSaveCommand(PvPPlayer pvpPlayer) {
        Player player = pvpPlayer.getPlayer();
        Game currentGame = pvpPlayer.getPlayerGameManager().getCurrentGame();

        if (currentGame == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're not currently in a game! " +
                    "Join a game to save your inventory.");
            return true;
        }

        // Save the player inventory for the respective arena
        pvpPlayer.getPlayerInventoryManager().saveArenaPersistedInventory(currentGame.getArena());

        if (pvpPlayer.isPremium()) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Saved your " +
                    ChatColor.AQUA + "hotbar" + ChatColor.GOLD + " setup forever!");
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Thanks for supporting " +
                    "our server by donating.");
        } else {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Saved your " +
                    ChatColor.AQUA + "hotbar" + ChatColor.GOLD + " setup until you logout.");
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "However, premium members get to " +
                    "keep it.");
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Navigate through our premium" +
                    " packages using " + ChatColor.AQUA + "/buy" + ChatColor.GOLD + "!");
        }
        return true;
    }

    /**
     * Handle scoreboard command.
     *
     * @param pvpPlayer the pvp player
     * @return if the command was successful
     */
    public boolean handleScoreboardCommand(PvPPlayer pvpPlayer) {
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
                                        ChatColor.AQUA + topELORankingsMapReference.get(name) + name);
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
     * Handle duel command boolean.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleDuelCommand(PvPPlayer pvpPlayer, String[] args) {
        Player player = pvpPlayer.getPlayer();
        GameManager gameManager = pvpPlugin.getGameManager();
        PlayerGameManager playerGameManager = pvpPlayer.getPlayerGameManager();
        Game playerCurrentGame = playerGameManager.getCurrentGame();

        if (args.length == 0) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must specify a player to duel! " +
                    "Usage: " + ChatColor.WHITE + "/duel <player>" + ChatColor.RED + " or " + ChatColor.WHITE +
                    "/duel team <team leader>");
            return true;
        }

        // Check if player cannot invite at all
        if (playerCurrentGame != null) { // Game might be in progress or queue
            if (playerCurrentGame.isInProgress()) { // Game is in progress
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're already in a game for " +
                        ChatColor.WHITE + playerGameManager.getCurrentGame().getName());
                return true;
            } else if (gameManager.isInQueue(playerCurrentGame)) { // Game not in progress, but is in queue
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're already in a game queue " +
                        "for " + ChatColor.WHITE + playerGameManager.getCurrentGame().getName());
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Type \"/leave queue\" to " +
                        "leave the current game queue.");
                return true;
            }
        }

        if (args.length == 1) { // args[1] might be 'team' or '<playername>'
            if (args[0].equalsIgnoreCase("team")) { // Check if second arg is 'team'
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must specify the team leader " +
                        "of the team you want to duel!");
                return true;
            }

            // /duel command for 1v1
            String duelPlayerName = args[0];
            PvPPlayer duelPlayer = pvpPlugin.getPlayerManager().getPvPPlayer(
                    Bukkit.getPlayer(duelPlayerName));

            if (duelPlayer == null) { // Check if player is online
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Could not find player " +
                        ChatColor.WHITE + duelPlayerName + ChatColor.RED + "!");
                return true;
            }

            // Check if duel player cannot be invited
            Game duelPlayerCurrentGame = duelPlayer.getPlayerGameManager().getCurrentGame();
            if (duelPlayerCurrentGame != null && duelPlayerCurrentGame.isInProgress()) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                        duelPlayer.getPrefixedName() + ChatColor.GOLD + " is currently in a game!");
                return true;
            }

            // Check if current player is part of another type of game
            if (playerCurrentGame != null && !(playerCurrentGame instanceof Ranked1v1Game)) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must leave the " +
                        "queue of any other game before attempting to 1v1 duel another player!");
                return true;
            }

            Ranked1v1Game playerCurrentRanked1v1Game = (Ranked1v1Game) playerCurrentGame;

            // Check if player cannot invite other players (game is already full)
            if (playerCurrentRanked1v1Game != null &&
                    playerCurrentRanked1v1Game.getDueler() != null &&
                    playerCurrentRanked1v1Game.getAcceptor() != null) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You cannot invite any more players!");
                return true;
            }

            // Player can successfully send duel invite

            if (playerCurrentRanked1v1Game == null) {
                playerCurrentRanked1v1Game = new Ranked1v1Game(gameManager, null);
            }
            playerCurrentRanked1v1Game.setDueler(pvpPlayer); // Player1 should always be the inviter
            playerCurrentRanked1v1Game.setDuel(true);
            playerCurrentRanked1v1Game.getInvitedPlayers().add(duelPlayer);

            playerGameManager.setCurrentGame(playerCurrentRanked1v1Game);

            // Send invited message
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "A duel invite has been sent " +
                    "to " + duelPlayer.getPrefixedName() + ChatColor.GREEN + ".");

            // Send invite message
            duelPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You have " +
                    "been challenged by " + pvpPlayer.getPrefixedName() + ChatColor.GOLD + "!");
            duelPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Type " +
                    ChatColor.AQUA + "/accept " + player.getName() + ChatColor.GOLD + " to play!");
            return true;
        } else if (args.length == 2) {
            // /duel command for team 2v2

            String duelTeam2v2LeaderName = args[1];
            PvPPlayer duelTeam2v2LeaderPlayer = pvpPlugin.getPlayerManager().getPvPPlayer(
                    Bukkit.getPlayer(duelTeam2v2LeaderName));
            Team2v2Game duelTeam2v2Game;
            Team2v2 duelTeam2v2;

            if (duelTeam2v2LeaderPlayer == null) { // Check if team leader is online
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Could not find player " +
                        ChatColor.WHITE + duelTeam2v2LeaderName);
                return true;
            }

            Game duelTeamLeaderCurrentGame = duelTeam2v2LeaderPlayer.getPlayerGameManager().getCurrentGame();

            // Check if duel team cannot be invited
            if (duelTeamLeaderCurrentGame.isInProgress()) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                        duelTeam2v2LeaderPlayer.getPrefixedName() + ChatColor.GOLD + " is currently" +
                        " in a game!");
                return true;

                // Check if duelTeamLeaderCurrentGame is not Team2v2Game
            } else if (!(duelTeamLeaderCurrentGame instanceof Team2v2Game)) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                        duelTeam2v2LeaderPlayer.getPrefixedName() + ChatColor.GOLD + " does not have a " +
                        "team to duel!");
                return true;
            } else { // duelTeamLeaderCurrentGame is a Team2v2Game
                duelTeam2v2Game = (Team2v2Game) duelTeamLeaderCurrentGame;
                duelTeam2v2 = duelTeam2v2Game.getPlayerTeam(duelTeam2v2LeaderPlayer);

                if (duelTeam2v2.getTeamLeader() == null ||
                        duelTeam2v2.getTeamMember() == null) { // Check if duel team is not full
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                            duelTeam2v2LeaderPlayer.getPrefixedName() + ChatColor.GOLD +
                            " must have a full 2v2 Team before sending a duel invite!");
                    return true;
                } else if (duelTeam2v2Game.getRedTeam2v2() != null &&
                        duelTeam2v2Game.getBlueTeam2v2() != null) { // Check if one team is not available
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                            duelTeam2v2LeaderPlayer.getPrefixedName() + ChatColor.GOLD +
                            " is already part of a game that has two teams!");
                    return true;

                    // Check if input team leader is not the leader
                } else if (!duelTeam2v2Game.getPlayerTeam(duelTeam2v2LeaderPlayer)
                        .getTeamLeader().equals(duelTeam2v2LeaderPlayer)) {
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                            duelTeam2v2LeaderPlayer.getPrefixedName() + ChatColor.GOLD + " is not the " +
                            "team leader of their team!");
                    return true;
                }
            }

            // Check if inviter cannot invite
            if (!(playerCurrentGame instanceof Team2v2Game)) { // Check if player doesn't have a Team2v2Game
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must create a 2v2 Team " +
                        "before sending a duel invite!");
                return true;
            } else { // Player is part of a Team2v2Game
                Team2v2Game playerCurrentTeam2v2Game = (Team2v2Game) playerCurrentGame;
                Team2v2 playerCurrentTeam2v2 = playerCurrentTeam2v2Game.getPlayerTeam(pvpPlayer);

                if (playerCurrentTeam2v2 == null) { // Check if player team is created
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must create a 2v2 Team " +
                            "before sending a duel invite!");
                    return true;
                } else if (playerCurrentTeam2v2.getTeamLeader() == null ||
                        playerCurrentTeam2v2.getTeamMember() == null) { // Check if player team is not full
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must have a full 2v2 Team " +
                            "before sending a duel invite!");
                    return true;
                } else if (playerCurrentTeam2v2Game.getRedTeam2v2() != null &&
                        playerCurrentTeam2v2Game.getBlueTeam2v2() != null) { // Check if one team is not available
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You are already part of a " +
                            "game that has two teams!");
                    return true;
                } else { // Player has valid team and can send invite

                    // Add the team leader of the duel team to invitedPlayers
                    playerCurrentTeam2v2Game.getInvitedPlayers().add(duelTeam2v2LeaderPlayer);

                    // Send the invite confirmation message
                    String teamDuelInviteMessage = ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "A team duel " +
                            "invite has been sent to " + duelTeam2v2LeaderPlayer.getPrefixedName() +
                            ChatColor.GREEN + ".";
                    player.sendMessage(teamDuelInviteMessage);
                    playerCurrentTeam2v2.getOtherTeamMember(pvpPlayer).getPlayer().sendMessage(teamDuelInviteMessage);

                    // Send the invite to duel team members
                    String teamChallengeMessage = ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You're team has " +
                            "been challenged by " + pvpPlayer.getPrefixedName() + ChatColor.GOLD + "!";
                    String acceptTeamChallengeMessage = ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Type " +
                            ChatColor.AQUA + "/accept team " + player.getName() + ChatColor.GOLD + " to play!";
                    duelTeam2v2.getTeamLeader().getPlayer().sendMessage(teamChallengeMessage);
                    duelTeam2v2.getTeamLeader().getPlayer().sendMessage(acceptTeamChallengeMessage);
                    duelTeam2v2.getTeamMember().getPlayer().sendMessage(teamChallengeMessage);
                    return true;
                }
            }
        } else {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Too many arguments for this command!");
            return true;
        }
    }

    /**
     * Handle accept command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleAcceptCommand(PvPPlayer pvpPlayer, String[] args) {
        Player player = pvpPlayer.getPlayer();
        GameManager gameManager = pvpPlugin.getGameManager();
        PlayerGameManager playerGameManager = pvpPlayer.getPlayerGameManager();
        Game playerCurrentGame = playerGameManager.getCurrentGame();

        if (args.length == 0) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must specify a player name! " +
                    "Usage: " + ChatColor.WHITE + "/accept <player>" + ChatColor.RED + " or " + ChatColor.WHITE +
                    "/accept team <team leader>");
            return true;
        } else if (args.length == 1) { // args[1] might be 'team' or '<playername>'
            if (args[0].equalsIgnoreCase("team")) { // Check if second arg is 'team'
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must specify the name of the " +
                        "team leader you want to accept the invite from!");
                return true;
            }

            // /accept command for 1v1
            String acceptPlayerName = args[0];
            PvPPlayer acceptPlayer = pvpPlugin.getPlayerManager().getPvPPlayer(
                    Bukkit.getPlayer(acceptPlayerName));

            if (acceptPlayer == null) { // Check if player is online
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Could not find player " +
                        ChatColor.WHITE + acceptPlayerName + ChatColor.RED + "!");
                return true;
            }

            Game acceptPlayerCurrentGame = acceptPlayer.getPlayerGameManager().getCurrentGame();
            // Check if accept player cannot be accepted
            if (acceptPlayerCurrentGame != null && acceptPlayerCurrentGame.isInProgress()) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                        acceptPlayer.getPrefixedName() + ChatColor.GOLD + " is currently in a game!");
                return true;
            }
            // Check if accept player's game is still ranked1v1
            if (!(acceptPlayerCurrentGame instanceof Ranked1v1Game)) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                        acceptPlayer.getPrefixedName() + ChatColor.GOLD + " is currently in a different queue!");
                return true;
            }
            // Check if accept player's game is still open to join
            Ranked1v1Game acceptPlayerCurrentRanked1v1Game = (Ranked1v1Game) acceptPlayerCurrentGame;
            if (acceptPlayerCurrentRanked1v1Game.getAcceptor() != null) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Player " +
                        acceptPlayer.getPrefixedName() + ChatColor.GOLD + " already accepted another player!");
                return true;
            }
            // Check if accept player is part of another type of game
            if (playerCurrentGame != null && !(playerCurrentGame instanceof Ranked1v1Game)) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must leave the " +
                        "queue of any other game before attempting to accept another player's 1v1 duel!");
                return true;
            }

            // Check if player cannot invite other players
            Ranked1v1Game playerCurrentRanked1v1Game = (Ranked1v1Game) playerCurrentGame;
            if (playerCurrentRanked1v1Game != null &&
                    playerCurrentRanked1v1Game.getDueler() != null &&
                    playerCurrentRanked1v1Game.getAcceptor() != null) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You cannot accept any more invites!");
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Use " + ChatColor.WHITE +
                        "/leave queue" + ChatColor.RED + " to leave your current game.");
                return true;
            }

            // Accept the invite
            acceptPlayerCurrentRanked1v1Game.setAcceptor(pvpPlayer);
            playerGameManager.setCurrentGame(acceptPlayerCurrentRanked1v1Game);

            // Send info messages
            acceptPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + pvpPlayer.getPrefixedName() +
                    ChatColor.GREEN + " has accepted your duel invite!");
            acceptPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "Now select an arena" +
                    "to join in Ranked 1v1 Menu.");
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "You have successfully" +
                    " accepted the invite!");
            return true;
        } else if (args.length == 2) {

            // /accept command for 2v2

            // TODO

            // Check if they're the team leader to accept the invite and on invited players
            // remove from invited players

            // Set both playerCurrentGames = to the same game
            return true;
        } else {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Too many arguments for this command!");
            return true;
        }
    }

    /**
     * Handle leave command boolean.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleLeaveCommand(PvPPlayer pvpPlayer, String[] args) {
        GameManager gameManager = pvpPlugin.getGameManager();
        PlayerGameManager playerGameManager = pvpPlayer.getPlayerGameManager();
        Game playerCurrentGame = playerGameManager.getCurrentGame();

        if (args.length == 0) {
            if (playerCurrentGame != null && playerCurrentGame.isInProgress()) {
                playerCurrentGame.leave(pvpPlayer); // Info messages of leaving should be taken care of in here
            } else {
                pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're not " +
                        "currently in a game.");
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("queue")) { // /leave queue
            if (playerCurrentGame == null) {
                pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You're not " +
                        "currently in a queue.");
                return true;
            } else if (playerCurrentGame.isInProgress()) {
                pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Your game is in " +
                        "progress!");
                pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Type " +
                        ChatColor.WHITE + "/leave" + ChatColor.RED + " to leave your current game.");
                return true;
            } else { // Game is not in progress
                gameManager.handlePlayerLeaveQueue(pvpPlayer);
                return true;
            }
        } else {
            pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Unknown leave command.");
            return true;
        }
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
        GameManager gameManager = pvpPlugin.getGameManager();

        FFAArena currentFFAArena = pvpPlugin.getArenaManager().getFFAArena();

        if (args.length == 0) { // Join FFA Arena
            if (currentFFAArena == null) {
                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "FFA Arena does not exist!");
            } else {
                gameManager.getFFAGame().join(pvpPlayer);
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
                            "set the Inventory for the " + currentFFAArena.getFormattedName() +
                            ChatColor.GREEN + " arena.");
                }

            } else if (ffaCommand.equalsIgnoreCase("setSpawn")) {

                if (currentFFAArena == null) { // Create the Arena
                    currentFFAArena = new FFAArena(arenaManager, "FFA Arena", ChatColor.GOLD + "FFA Arena");
                    arenaManager.setFFAArena(currentFFAArena);

                    gameManager.updateArenaReferences(); // Creates the FFAGame instance
                }

                currentFFAArena.setSpawnLocation(LocationUtil.centerBlockLocation(player.getLocation()));

                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully " +
                        "set the spawn location for FFA Arena.");

            } else if (ffaCommand.equalsIgnoreCase("setLeave")) {

                if (currentFFAArena == null) {
                    player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "FFA Arena " +
                            "doesn't exist! Set the spawn before setting the leave location for it!");
                } else {
                    currentFFAArena.setLeaveLocation(LocationUtil.centerBlockLocation(player.getLocation()));
                }

                player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully " +
                        "set the leave location for FFA Arena.");
            }

            // Now save arenas async
            pvpPlugin.getArenaManager().getArenaDataManager().saveArenasAsync();
        }
        return true; // We use our own custom usage messages so don't make Bukkit output them
    }

    /**
     * Handle 1v1 command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handle1v1Command(PvPPlayer pvpPlayer, String[] args) {
        if (args.length == 0) { // /1v1 command
            pvpPlayer.getPlayer().openInventory(pvpPlayer.getPlayerGUIManager().getRanked1v1Menu().getInventory());
        } else {
            switch (args[0].toLowerCase()) {
                case "arena":
                    if (args.length < 2) {
                        pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Incorrect " +
                                "number of arguments.");
                        return false;
                    }

                    switch (args[1].toLowerCase()) {
                        case "add":
                            return this.handle1v1ArenaAddCommand(pvpPlayer, args);
                        case "remove":
                            return this.handle1v1ArenaRemoveCommand(pvpPlayer, args);
                        case "setspawn1":
                            return this.handle1v1ArenaSetSpawnCommand(pvpPlayer, args, 1);
                        case "setspawn2":
                            return this.handle1v1ArenaSetSpawnCommand(pvpPlayer, args, 2);
                        case "setfinish":
                            return this.handle1v1ArenaSetFinishCommand(pvpPlayer, args);
                        default:
                            pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED +
                                    "Unknown arena command.");
                            return true;
                    }
                case "setinv":
                    return this.handle1v1ArenaSetInvCommand(pvpPlayer, args);
                case "enable":
                    return this.handle1v1ArenaEnableDisableCommand(pvpPlayer, args, false);
                case "disable":
                    return this.handle1v1ArenaEnableDisableCommand(pvpPlayer, args, true);
                default:
                    pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED +
                            "Unknown 1v1 command.");
                    return true;
            }
        }
        return false;
    }

    /**
     * Handle 1v1 arena add command boolean.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handle1v1ArenaAddCommand(PvPPlayer pvpPlayer, String[] args) {
        Player player = pvpPlayer.getPlayer();
        ArenaManager arenaManager = pvpPlugin.getArenaManager();
        PlayerDataManager playerDataManager = pvpPlugin.getPlayerManager().getPlayerDataManager();

        if (args.length != 5) { // Check if proper args
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Incorrect number of " +
                    "arguments! Usage: " + ChatColor.WHITE + "/1v1 arena add <name> " +
                    "<built by> <description>");
            return true;
        }

        Material arenaMaterial;
        ItemStack playerHandItemStack = player.getInventory().getItemInHand();
        // Check if player is holding item
        if (playerHandItemStack == null || playerHandItemStack.getType() == Material.AIR) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must be holding " +
                    "an Item to resemble this Arena in the GUI!");
            return true;
        } else {
            arenaMaterial = playerHandItemStack.getType();
        }


        // Parse arena information from args
        String arena1v1FormattedName = args[2];
        String arena1v1NameIdentifier = ChatUtil.stripAnyColorCodes(arena1v1FormattedName);
        String builtByName = ChatUtil.translateAnyColorCodes(args[3]);
        String description = args[4];

        // Check if arena already exists
        if (arenaManager.getArenaByNameIdentifier(arena1v1NameIdentifier) != null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Arena already exists!");
            return true;
        }

        // Create the arena
        Ranked1v1Arena ranked1v1Arena = new Ranked1v1Arena(arenaManager, arena1v1NameIdentifier, arena1v1FormattedName);
        ranked1v1Arena.setBuiltByName(builtByName);
        ranked1v1Arena.setDescription(description);
        ranked1v1Arena.setArenaItemStack(new Ranked1v1ArenaItemStack(ranked1v1Arena, arenaMaterial));

        // Add the arena
        arenaManager.getRanked1v1Arenas().add(ranked1v1Arena);

        // Update the GameManager arena references
        pvpPlugin.getGameManager().updateArenaReferences();

        // Save the arenas async
        pvpPlugin.getArenaManager().getArenaDataManager().saveArenasAsync();

        // Loop through all online Players, update their inventories that display arenas and their player data
        for (PvPPlayer aPvPPlayer : pvpPlugin.getPlayerManager().getPvPPlayers()) {
            playerDataManager.updatePlayerDataArenas(aPvPPlayer.getPlayerData());
            aPvPPlayer.getPlayerGUIManager().updateArenaItemStackInventories(pvpPlugin.getGameManager());
        }

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully added the 1v1 Arena.");
        return true;
    }

    /**
     * Handle 1v1 arena remove command boolean.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handle1v1ArenaRemoveCommand(PvPPlayer pvpPlayer, String[] args) {
        Player player = pvpPlayer.getPlayer();
        ArenaManager arenaManager = pvpPlugin.getArenaManager();
        PlayerDataManager playerDataManager = pvpPlugin.getPlayerManager().getPlayerDataManager();

        if (args.length != 3) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Incorrect number of " +
                    "arguments! Usage: " + ChatColor.WHITE + "/1v1 arena remove <name>");
            return true;
        }

        String arena1v1NameIdentifier = args[2];

        // Get the arena
        Arena arena = arenaManager.getArenaByNameIdentifier(arena1v1NameIdentifier);
        if (arena == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena does not exist!");
            return true;
        } else if (!(arena instanceof Ranked1v1Arena)) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena is not a 1v1 arena!");
            return true;
        }

        // Remove the arena
        arenaManager.getRanked1v1Arenas().remove((Ranked1v1Arena) arena);

        // Update the GameManager arena references
        pvpPlugin.getGameManager().updateArenaReferences();

        pvpPlugin.getArenaManager().getArenaDataManager().saveArenasAsync();

        // Loop through all online Players, update their inventories that display arenas and their player data
        for (PvPPlayer aPvPPlayer : pvpPlugin.getPlayerManager().getPvPPlayers()) {
            playerDataManager.updatePlayerDataArenas(aPvPPlayer.getPlayerData());
            aPvPPlayer.getPlayerGUIManager().updateArenaItemStackInventories(pvpPlugin.getGameManager());
        }

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully removed the 1v1 Arena.");
        return true;
    }

    /**
     * Handle 1v1 arena set spawn command boolean.
     *
     * @param pvpPlayer   the pvp player
     * @param args        the args
     * @param spawnNumber the spawn number (1 or 2)
     * @return if the command was successful
     */
    public boolean handle1v1ArenaSetSpawnCommand(PvPPlayer pvpPlayer, String[] args, int spawnNumber) {
        Player player = pvpPlayer.getPlayer();
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        String arena1v1NameIdentifier = args[2];

        // Get the arena
        Arena arena = arenaManager.getArenaByNameIdentifier(arena1v1NameIdentifier);
        if (arena == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena does not exist!");
            return true;
        } else if (!(arena instanceof Ranked1v1Arena)) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena is not a 1v1 arena!");
            return true;
        }

        // Set the arena spawn location 1 or 2
        Ranked1v1Arena ranked1v1Arena = (Ranked1v1Arena) arena;
        Location spawnLocation = LocationUtil.centerBlockLocation(player.getLocation());
        if (spawnNumber == 1) { // Set spawn 1
            ranked1v1Arena.setSpawnLocation1(spawnLocation);
        } else if (spawnNumber == 2) { // Set spawn 2
            ranked1v1Arena.setSpawnLocation2(spawnLocation);
        } else {
            throw new IllegalArgumentException("Spawn number must be 1 or 2!");
        }

        pvpPlugin.getArenaManager().getArenaDataManager().saveArenasAsync();

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully set the 1v1 Arena spawn " +
                spawnNumber + ".");
        return true;
    }

    /**
     * Handle 1v1 arena finish command boolean.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handle1v1ArenaSetFinishCommand(PvPPlayer pvpPlayer, String[] args) {
        Player player = pvpPlayer.getPlayer();
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        String arena1v1NameIdentifier = args[2];

        // Get the arena
        Arena arena = arenaManager.getArenaByNameIdentifier(arena1v1NameIdentifier);
        if (arena == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena does not exist!");
            return true;
        } else if (!(arena instanceof Ranked1v1Arena)) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena is not a 1v1 arena!");
            return true;
        }

        // Set the arena finish location
        Ranked1v1Arena ranked1v1Arena = (Ranked1v1Arena) arena;
        ranked1v1Arena.setFinishLocation(LocationUtil.centerBlockLocation(player.getLocation()));

        pvpPlugin.getArenaManager().getArenaDataManager().saveArenasAsync();

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully set the 1v1 Arena finish " +
                "location.");
        return true;
    }

    /**
     * Handle 1v1 arena set inv command boolean.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handle1v1ArenaSetInvCommand(PvPPlayer pvpPlayer, String[] args) {
        Player player = pvpPlayer.getPlayer();
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        String arena1v1NameIdentifier = args[1];

        // Get the arena
        Arena arena = arenaManager.getArenaByNameIdentifier(arena1v1NameIdentifier);
        if (arena == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena does not exist!");
            return true;
        } else if (!(arena instanceof Ranked1v1Arena)) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena is not a 1v1 arena!");
            return true;
        }

        // Set arena inventory
        Ranked1v1Arena ranked1v1Arena = (Ranked1v1Arena) arena;
        ranked1v1Arena.setInventory(player.getInventory().getContents());
        ranked1v1Arena.setArmorInventory(player.getInventory().getArmorContents());

        pvpPlugin.getArenaManager().getArenaDataManager().saveArenasAsync();

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully set the 1v1 Arena Inventory.");
        return true;
    }

    /**
     * Handle 1v1 arena enable/disable command boolean.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handle1v1ArenaEnableDisableCommand(PvPPlayer pvpPlayer, String[] args, boolean arenaDisabled) {
        Player player = pvpPlayer.getPlayer();
        PlayerDataManager playerDataManager = pvpPlugin.getPlayerManager().getPlayerDataManager();
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        String arena1v1NameIdentifier = args[1];

        // Get the arena
        Arena arena = arenaManager.getArenaByNameIdentifier(arena1v1NameIdentifier);
        if (arena == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena does not exist!");
            return true;
        } else if (!(arena instanceof Ranked1v1Arena)) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena is not a 1v1 arena!");
            return true;
        }

        // Set arena disabled boolean
        Ranked1v1Arena ranked1v1Arena = (Ranked1v1Arena) arena;
        ranked1v1Arena.setDisabled(arenaDisabled);

        pvpPlugin.getArenaManager().getArenaDataManager().saveArenasAsync();

        // Loop through all online Players, update their inventories that display arenas and their player data
        for (PvPPlayer aPvPPlayer : pvpPlugin.getPlayerManager().getPvPPlayers()) {
            playerDataManager.updatePlayerDataArenas(aPvPPlayer.getPlayerData());
            aPvPPlayer.getPlayerGUIManager().updateArenaItemStackInventories(pvpPlugin.getGameManager());
        }

        player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GREEN + "Successfully set the 1v1 Arena to be " +
                (arenaDisabled ? "disabled" : "enabled") + ".");
        return true;
    }

    /**
     * Handle 2v2 command.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handle2v2Command(PvPPlayer pvpPlayer, String[] args) {
        return false;
    }
}
