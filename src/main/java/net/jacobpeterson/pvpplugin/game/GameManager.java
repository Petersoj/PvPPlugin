package net.jacobpeterson.pvpplugin.game;

import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.arenas.ffa.FFAArena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.Team2v2Arena;
import net.jacobpeterson.pvpplugin.game.event.GeneralGameEventHandlers;
import net.jacobpeterson.pvpplugin.game.game.ffa.FFAGame;
import net.jacobpeterson.pvpplugin.game.game.ranked1v1.Ranked1v1Game;
import net.jacobpeterson.pvpplugin.game.game.team2v2.Team2v2;
import net.jacobpeterson.pvpplugin.game.game.team2v2.Team2v2Game;
import net.jacobpeterson.pvpplugin.player.PvPPlayer;
import net.jacobpeterson.pvpplugin.player.game.PlayerGameManager;
import net.jacobpeterson.pvpplugin.util.ChatUtil;
import net.jacobpeterson.pvpplugin.util.Initializers;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class GameManager implements Initializers {

    private PvPPlugin pvpPlugin;
    private GeneralGameEventHandlers generalGameEventHandlers;
    private FFAGame ffaGame;
    private HashMap<Ranked1v1Arena, LinkedList<Ranked1v1Game>> ranked1v1GameQueueMap;
    private HashMap<Team2v2Arena, LinkedList<Team2v2Game>> team2v2GameQueueMap;

    /**
     * Instantiates a new GameManager which is used to manage game instances/queues.
     *
     * @param pvpPlugin the pvp plugin
     */
    public GameManager(PvPPlugin pvpPlugin) {
        this.pvpPlugin = pvpPlugin;
        this.generalGameEventHandlers = new GeneralGameEventHandlers(this);
        this.ranked1v1GameQueueMap = new HashMap<>();
        this.team2v2GameQueueMap = new HashMap<>();
    }

    @Override
    public void init() {
        this.updateArenaReferences();

        this.generalGameEventHandlers.init();
    }

    @Override
    public void deinit() {
        // Stop and deinit all game instances

        // Stop game instance for FFA
        if (ffaGame != null) {
            ffaGame.stop();
            ffaGame.deinit();
        }

        // Loop through all arenas' game queues for Ranked1v1
        for (LinkedList<Ranked1v1Game> ranked1v1GameQueue : ranked1v1GameQueueMap.values()) {
            for (Ranked1v1Game ranked1v1Game : ranked1v1GameQueue) { // Loop through all game instances for arena
                ranked1v1Game.stop();
                ranked1v1Game.deinit();
            }
        }

        // Loop through all arenas' game queues for Team2v2
        for (LinkedList<Team2v2Game> team2v2GameQueue : team2v2GameQueueMap.values()) {
            for (Team2v2Game team2v2Game : team2v2GameQueue) { // Loop through all game instances for arena
                team2v2Game.stop();
                team2v2Game.deinit();
            }
        }

        this.generalGameEventHandlers.deinit();
    }

    /**
     * Update arena references. Will basically check if an arena exists and there isn't a game instance or queue for
     * it. If there isn't, create a game instance or queue.
     * Should be called whenever there is an addition/removal of arenas.
     * Will also stop/deinit games as necessary.
     */
    @SuppressWarnings("SuspiciousMethodCalls") // Suppress this warning because we know that value exists in Map
    public void updateArenaReferences() {
        ArenaManager arenaManager = pvpPlugin.getArenaManager();

        //
        // Update FFA Arena game
        //
        FFAArena ffaArena = arenaManager.getFFAArena();
        if (ffaArena != null && ffaGame == null) { // Check if FFAGame needs to be created
            this.ffaGame = new FFAGame(this, ffaArena);
            this.ffaGame.init();
            this.ffaGame.start();
        } else if (ffaArena == null && ffaGame != null) { // Destroy FFAGame instance as the arena no longer exists
            this.ffaGame.stop();
            this.ffaGame.deinit();
            this.ffaGame = null;
        }

        //
        // Update game queue map for Ranked1v1Arenas
        //
        ArrayList<Ranked1v1Arena> realRanked1v1Arenas = arenaManager.getRanked1v1Arenas();
        // Loop through all current Arena references to check if removal/stopping is needed
        Iterator<Ranked1v1Arena> ranked1v1ArenaIterator = ranked1v1GameQueueMap.keySet().iterator();
        while (ranked1v1ArenaIterator.hasNext()) {
            Ranked1v1Arena ranked1v1Arena = ranked1v1ArenaIterator.next();

            // Check if Arena reference in game queue no longer exists in realRanked1v1Arenas
            if (!realRanked1v1Arenas.contains(ranked1v1Arena)) {
                LinkedList<Ranked1v1Game> ranked1v1GameQueue = ranked1v1GameQueueMap.get(ranked1v1Arena);

                // Destroy all Ranked1v1Game instances because the arena no longer exists
                for (Ranked1v1Game ranked1v1Game : ranked1v1GameQueue) {
                    ranked1v1Game.stop();
                    ranked1v1Game.deinit();
                }

                // Remove from game queue map
                ranked1v1ArenaIterator.remove();
            }
        }
        // Create game queue for arenas that don't exist in ranked1v1GameQueueMap
        for (Ranked1v1Arena realRanked1v1Arena : realRanked1v1Arenas) {
            if (!ranked1v1GameQueueMap.containsKey(realRanked1v1Arena)) {
                ranked1v1GameQueueMap.put(realRanked1v1Arena, new LinkedList<>());
            }
        }

        //
        // Update game queue map for team2v2GameQueueMap
        //
        ArrayList<Team2v2Arena> realTeam2v2Arenas = arenaManager.getTeam2v2Arenas();
        // Loop through all current Arena references to check if removal/stopping is needed
        Iterator<Team2v2Arena> team2v2ArenaIterator = team2v2GameQueueMap.keySet().iterator();
        while (ranked1v1ArenaIterator.hasNext()) {
            Team2v2Arena team2v2Arena = team2v2ArenaIterator.next();

            // Check if Arena reference in game queue no longer exists in realTeam2v2Arenas
            if (!realTeam2v2Arenas.contains(team2v2Arena)) {
                LinkedList<Team2v2Game> team2v2Games = team2v2GameQueueMap.get(team2v2Arena);

                // Destroy all Team2v2Game instances because the arena no longer exists
                for (Team2v2Game team2v2Game : team2v2Games) {
                    team2v2Game.stop();
                    team2v2Game.deinit();
                }

                // Remove from game queue map
                team2v2ArenaIterator.remove();
            }
        }
        // Create game queue for arenas that don't exist in team2v2GameQueueMap
        for (Team2v2Arena realTeam2v2Arena : realTeam2v2Arenas) {
            if (!team2v2GameQueueMap.containsKey(realTeam2v2Arena)) {
                team2v2GameQueueMap.put(realTeam2v2Arena, new LinkedList<>());
            }
        }
    }

    /**
     * Checks if a game is in a queue.
     *
     * @param game the game
     * @return the boolean
     */
    public boolean isInQueue(Game game) {
        if (game.getArena() == null) {
            return false;
        }
        if (game instanceof FFAGame) {
            return false;
        } else if (game instanceof Ranked1v1Game) {
            Queue<Ranked1v1Game> ranked1v1GameQueue = ranked1v1GameQueueMap.get((Ranked1v1Arena) game.getArena());
            if (ranked1v1GameQueue == null) {
                return false;
            } else {
                return ranked1v1GameQueue.contains(game);
            }
        } else if (game instanceof Team2v2Game) {
            Queue<Team2v2Game> team2v2GameQueue = team2v2GameQueueMap.get((Team2v2Arena) game.getArena());
            if (team2v2GameQueue == null) {
                return false;
            } else {
                return team2v2GameQueue.contains(game);
            }
        } else {
            return false;
        }
    }

    /**
     * Removes a game from its queue.
     *
     * @param game the game
     */
    public void removeFromQueue(Game game) {
        if (game.getArena() == null) {
            return;
        }
        if (game instanceof Ranked1v1Game) {
            Queue<Ranked1v1Game> ranked1v1GameQueue = ranked1v1GameQueueMap.get((Ranked1v1Arena) game.getArena());
            ranked1v1GameQueue.remove(game);
        } else if (game instanceof Team2v2Game) {
            Queue<Team2v2Game> team2v2GameQueue = team2v2GameQueueMap.get((Team2v2Arena) game.getArena());
            team2v2GameQueue.remove(game);
        }
    }

    /**
     * Handles a player leaving the queue (sends messages to affected players, dissolves games, etc.).
     *
     * @param pvpPlayer the pvp player
     */
    public void handlePlayerLeaveQueue(PvPPlayer pvpPlayer) {
        PlayerGameManager playerGameManager = pvpPlayer.getPlayerGameManager();
        Game playerCurrentGame = playerGameManager.getCurrentGame();

        if (this.isInQueue(playerCurrentGame)) { // Check if game is in arena queue
            // Remove this game from queue because all players have to be present in order for the game to be valid
            this.removeFromQueue(playerCurrentGame);
        }

        // Dissolve the game either due to a stale duel request or a player leaving a queue that they joined

        if (playerCurrentGame instanceof Ranked1v1Game) { // Check if the game was Ranked1v1Game
            Ranked1v1Game ranked1v1Game = (Ranked1v1Game) playerCurrentGame;
            if (pvpPlayer.equals(ranked1v1Game.getDueler())) { // Check if queue leaver is dueler
                PvPPlayer acceptorPvPPlayer = ranked1v1Game.getAcceptor();
                acceptorPvPPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                        ChatColor.RED + "Your opponent has left your game queue!");
                acceptorPvPPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                        ChatColor.GOLD + "Join another game!");
                // Cancel game
                acceptorPvPPlayer.getPlayerGameManager().setCurrentGame(null);
            } else if (pvpPlayer.equals(ranked1v1Game.getAcceptor())) { // Check if queue leaver is acceptor
                ranked1v1Game.getDueler().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                        ChatColor.RED + "Your opponent has left your game queue!");
                ranked1v1Game.getDueler().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                        ChatColor.GOLD + "Invite/join another or type " + ChatColor.WHITE + "/leave queue" +
                        ChatColor.GOLD + " to leave your Ranked 1v1 game.");
                // Allow dueler game instance to persist because another player could accept another invite, etc.
            }

            pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You " +
                    "have successfully left your game queue.");
        } else if (playerCurrentGame instanceof Team2v2Game) { // Check if the game was Team2v2Game
            Team2v2Game team2v2Game = (Team2v2Game) playerCurrentGame;
            Team2v2 redTeam = team2v2Game.getRedTeam2v2();
            Team2v2 blueTeam = team2v2Game.getBlueTeam2v2();

            if (redTeam != null) { // Check if red team is not null
                PvPPlayer redTeamLeader = redTeam.getTeamLeader();
                PvPPlayer redTeamMember = redTeam.getTeamMember();

                if (pvpPlayer.equals(redTeamLeader)) { // Check if leaver is team leader of blue team
                    // Send message to team member
                    if (redTeamMember != null) {
                        redTeamMember.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                ChatColor.RED + "Your team leader has left your game queue!");
                        redTeamMember.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                ChatColor.GOLD + "Join another game!");
                        redTeamMember.getPlayerGameManager().setCurrentGame(null);
                    }
                    // Send messages to other team
                    if (blueTeam != null) {
                        if (blueTeam.getTeamLeader() != null) {
                            blueTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.RED + "Your opponent team leader has left your game queue!");
                            blueTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.GOLD + "Invite another or type " + ChatColor.WHITE + "/leave queue" +
                                    ChatColor.GOLD + " to leave your team.");
                        }
                        if (blueTeam.getTeamMember() != null) {
                            blueTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.RED + "Your opponent team leader has left your game queue!");
                            blueTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.GOLD + "Have your team leader invite another player or type " +
                                    ChatColor.WHITE + "/leave queue" +
                                    ChatColor.GOLD + " to leave your team.");
                        }
                    }

                    // Set the redTeamLeader to null
                    redTeam.setTeamLeader(null);
                } else if (pvpPlayer.equals(redTeamMember)) { // Check if leaver is team member of red team
                    // Send message to team leader
                    if (redTeamLeader != null) {
                        redTeamLeader.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                ChatColor.RED + "Your team member has left your game queue!");
                        redTeamLeader.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                ChatColor.GOLD + "Invite/join another or type " + ChatColor.WHITE + "/leave queue" +
                                ChatColor.GOLD + " to leave your team.");
                    }
                    // Send messages to other team
                    if (blueTeam != null) {
                        if (blueTeam.getTeamLeader() != null) {
                            blueTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.RED + "Your opponent team leader has left your game queue!");
                            blueTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.GOLD + "Invite/join another or type " + ChatColor.WHITE +
                                    "/leave queue" + ChatColor.GOLD + " to leave your team.");
                        }
                        if (blueTeam.getTeamMember() != null) {
                            blueTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.RED + "Your opponent team leader has left your game queue!");
                            blueTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.GOLD + "Have your team leader invite another player or type " +
                                    ChatColor.WHITE + "/leave queue" +
                                    ChatColor.GOLD + " to leave your team.");
                        }
                    }

                    // Set the redTeamMember to null
                    redTeam.setTeamMember(null);
                }
            }
            if (blueTeam != null) { // Check if blue team is not null
                PvPPlayer blueTeamLeader = blueTeam.getTeamLeader();
                PvPPlayer blueTeamMember = blueTeam.getTeamMember();

                if (pvpPlayer.equals(blueTeamLeader)) { // Check if leaver is team leader of blue team
                    // Send message to team member
                    if (blueTeamMember != null) {
                        blueTeamMember.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                ChatColor.RED + "Your team leader has left your game queue!");
                        blueTeamMember.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                ChatColor.GOLD + "Join another game!");
                        blueTeamMember.getPlayerGameManager().setCurrentGame(null);
                    }
                    // Send messages to other team
                    if (redTeam != null) {
                        if (redTeam.getTeamLeader() != null) {
                            redTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.RED + "Your opponent team leader has left your game queue!");
                            redTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.GOLD + "Invite/join another or type " + ChatColor.WHITE +
                                    "/leave queue" + ChatColor.GOLD + " to leave your team.");
                        }
                        if (redTeam.getTeamMember() != null) {
                            redTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.RED + "Your opponent team leader has left your game queue!");
                            redTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.GOLD + "Have your team leader invite another player or type " +
                                    ChatColor.WHITE + "/leave queue" +
                                    ChatColor.GOLD + " to leave your team.");
                        }
                    }

                    // Set the blueTeamLeader to null
                    blueTeam.setTeamLeader(null);
                } else if (pvpPlayer.equals(blueTeamMember)) { // Check if leaver is team member of blue team
                    // Send message to team leader
                    if (blueTeamLeader != null) {
                        blueTeamLeader.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                ChatColor.RED + "Your team member has left your game queue!");
                        blueTeamLeader.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                ChatColor.GOLD + "Invite/join another or type " + ChatColor.WHITE + "/leave queue" +
                                ChatColor.GOLD + " to leave your team.");
                    }
                    // Send messages to other team
                    if (redTeam != null) {
                        if (redTeam.getTeamLeader() != null) {
                            redTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.RED + "Your opponent team leader has left your game queue!");
                            redTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.GOLD + "Invite/join another or type " + ChatColor.WHITE +
                                    "/leave queue" + ChatColor.GOLD + " to leave your team.");
                        }
                        if (redTeam.getTeamMember() != null) {
                            redTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.RED + "Your opponent team leader has left your game queue!");
                            redTeam.getTeamLeader().getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX +
                                    ChatColor.GOLD + "Have your team leader invite another player or type " +
                                    ChatColor.WHITE + "/leave queue" +
                                    ChatColor.GOLD + " to leave your team.");
                        }
                    }

                    // Set the blueTeamMember to null
                    blueTeam.setTeamMember(null);
                }
            }

            pvpPlayer.getPlayer().sendMessage(ChatUtil.SERVER_CHAT_PREFIX + ChatColor.GOLD + "You " +
                    "have successfully left the queue.");
        }

        // Set the player's current game to null
        playerGameManager.setCurrentGame(null);
    }

    /**
     * Gets pvp plugin.
     *
     * @return the pvp plugin
     */
    public PvPPlugin getPvPPlugin() {
        return pvpPlugin;
    }

    /**
     * Gets general game event handlers.
     *
     * @return the general game event handlers
     */
    public GeneralGameEventHandlers getGeneralGameEventHandlers() {
        return generalGameEventHandlers;
    }

    /**
     * Gets ffa game.
     *
     * @return the ffa game
     */
    public FFAGame getFFAGame() {
        return ffaGame;
    }

    /**
     * Gets ranked 1v1 game queue map.
     *
     * @return the ranked 1v1 game queue map
     */
    public HashMap<Ranked1v1Arena, LinkedList<Ranked1v1Game>> getRanked1v1GameQueueMap() {
        return ranked1v1GameQueueMap;
    }

    /**
     * Gets team 2v2 game queue map.
     *
     * @return the team 2v2 game queue map
     */
    public HashMap<Team2v2Arena, LinkedList<Team2v2Game>> getTeam2v2GameQueueMap() {
        return team2v2GameQueueMap;
    }
}
