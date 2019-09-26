package net.jacobpeterson.pvpplugin.command;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.arenas.ffa.FFAArena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.itemstack.Ranked1v1ArenaItemStack;
import net.jacobpeterson.pvpplugin.game.Game;
import net.jacobpeterson.pvpplugin.game.GameManager;
import net.jacobpeterson.pvpplugin.player.PlayerManager;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.data.PlayerData;
import net.jacobpeterson.pvpplugin.player.data.PlayerDataManager;
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

import java.io.IOException;
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
                case "challenge":
                    return this.handleChallengeCommand(pvpPlayer, args);
                case "accept":
                    return this.handleAcceptCommand(pvpPlayer);
                case "leave":
                    return this.handleLeaveCommand(pvpPlayer);

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
     * @return if the command was successful
     */
    public boolean handleSaveCommand(PvPPlayer pvpPlayer) {
        return false;
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
     * Handle accept command boolean.
     *
     * @param pvpPlayer the pvp player
     * @param args      the args
     * @return if the command was successful
     */
    public boolean handleChallengeCommand(PvPPlayer pvpPlayer, String[] args) {
        return false;
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
     * @return if the command was successful
     */
    public boolean handleLeaveCommand(PvPPlayer pvpPlayer) {
        Game playerCurrentGame = pvpPlayer.getPlayerGameManager().getCurrentGame();
        if (playerCurrentGame != null) {
            playerCurrentGame.leave(pvpPlayer);
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
                            "set the Inventory for the " + ChatColor.WHITE + currentFFAArena.getName() +
                            ChatColor.GREEN + " arena.");
                }

            } else if (ffaCommand.equalsIgnoreCase("setSpawn")) {

                if (currentFFAArena == null) { // Create the Arena
                    currentFFAArena = new FFAArena(arenaManager, "FFA Arena");
                    arenaManager.setFFAArena(currentFFAArena);

                    gameManager.updateArenaReferences(arenaManager); // Creates the FFAGame instance
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
            this.saveArenasAsync();
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

                    switch (args[1]) {
                        case "add":
                            return this.handle1v1ArenaAddCommand(pvpPlayer, args);
                        case "remove":
                            return this.handle1v1ArenaRemoveCommand(pvpPlayer, args);
                        case "setSpawn1":
                            return this.handle1v1ArenaSetSpawnCommand(pvpPlayer, args, 1);
                        case "setSpawn2":
                            return this.handle1v1ArenaSetSpawnCommand(pvpPlayer, args, 2);
                        case "setFinish":
                            return this.handle1v1ArenaSetFinishCommand(pvpPlayer, args);
                        default:
                            pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED +
                                    "Unknown arena command.");
                            return true;
                    }
                case "setInv":
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

        if (args.length < 5) { // Check if <5 and not =5 because description is more than 1 word
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Incorrect number of " +
                    "arguments! Usage: " + ChatColor.WHITE + "/1v1 arena add <name> <premium?> " +
                    "<built by> <description>");
            return true;
        }

        Material arenaMaterial;
        ItemStack playerHandItemStack = player.getInventory().getItemInHand();
        if (playerHandItemStack == null || playerHandItemStack.getType() == Material.AIR) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "You must be holding " +
                    "an Item to resemble this Arena in the GUI!");
            return true;
        } else {
            arenaMaterial = playerHandItemStack.getType();
        }


        // Parse arena information from args
        String arena1v1Name = args[2];
        boolean isPremium = Boolean.parseBoolean(args[3]);
        String builtByName = args[4];
        StringBuilder descriptionStringBuilder = new StringBuilder();
        for (int descriptionIndex = 5; descriptionIndex < args.length; descriptionIndex++) {
            descriptionStringBuilder.append(args[descriptionIndex]).append(" ");
        }

        // Check if arena already exists
        if (arenaManager.getArenaByName(arena1v1Name) != null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "Arena already exists!");
        }

        // Create the arena
        Ranked1v1Arena ranked1v1Arena = new Ranked1v1Arena(arenaManager, arena1v1Name);
        ranked1v1Arena.setPremium(isPremium);
        ranked1v1Arena.setBuiltByName(builtByName);
        ranked1v1Arena.setDescription(descriptionStringBuilder.toString());
        ranked1v1Arena.setArenaItemStack(new Ranked1v1ArenaItemStack(ranked1v1Arena, arenaMaterial));

        // Add the arena
        arenaManager.getRanked1v1Arenas().add(ranked1v1Arena);

        // Save the arenas async
        this.saveArenasAsync();

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

        String arena1v1Name = args[2];

        // Get the arena
        Arena arena = arenaManager.getArenaByName(arena1v1Name);
        if (arena == null) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena does not exist!");
            return true;
        } else if (!(arena instanceof Ranked1v1Arena)) {
            player.sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.RED + "That arena is not a 1v1 arena!");
            return true;
        }

        // Remove the arena
        arenaManager.getRanked1v1Arenas().remove((Ranked1v1Arena) arena);

        this.saveArenasAsync();

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
        String arena1v1Name = args[2];
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        // Get the arena
        Arena arena = arenaManager.getArenaByName(arena1v1Name);
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

        this.saveArenasAsync();

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
        String arena1v1Name = args[2];
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        // Get the arena
        Arena arena = arenaManager.getArenaByName(arena1v1Name);
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

        this.saveArenasAsync();

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
        String arena1v1Name = args[1];
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        // Get the arena
        Arena arena = arenaManager.getArenaByName(arena1v1Name);
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

        this.saveArenasAsync();

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
        String arena1v1Name = args[1];
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        // Get the arena
        Arena arena = arenaManager.getArenaByName(arena1v1Name);
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

        this.saveArenasAsync();

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

    /**
     * Save the arenas async.
     */
    private void saveArenasAsync() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    pvpPlugin.getArenaManager().getArenaDataManager().saveArenas();
                    LOGGER.info("Saved arenas.");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }.runTaskAsynchronously(pvpPlugin);
    }
}
