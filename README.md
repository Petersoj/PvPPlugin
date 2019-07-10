# A PVP Plugin for Spo Jell

The final Jar is located in the build/libs/ folder.

Informational Screenshots: https://imgur.com/a/DJ6aXN4

#GENERAL COMMANDS

<displayname> = GroupManager prefix
Features:
Menus for all gamemodes
ELO
Game Rank (the leaderboard rank)

Global commands to these plugins:
/setlobby -> Sets the lobby (when joining the lobby)
/lobby -> Teleports to lobby, 0 seconds cooldown
/record or /records -> See personal stats
/record <player -> See a players stats
/save -> Saves the in-game hotbar while playing 1v1/2v2
/scoreboard -> Top 10 players (most elo)
/accept <player> -> Accepts a duel challenge/team invite

Messages:
Scoreboard (top 10):
&6Viewing the top 10 players
&61. &b<elo> <displayname>
&62. &b<elo> <displayname>
and so so on to the 10 player

Records/Stats (personal and other player):
<displayname>&6's Stats&8:
&6Rank&8: &b%vault_rank%
&6Ranked 1v1 ELO&8/&6Game Rank&8: &b1v1_elo>&8- #&b<game_rank>
&6Ranked 1v1 Wins&8/&6Losses&8: &b<1v1_wins>&8/&b<1v1_losses>
&6Unranked FFA Kills&8/&6Deaths&8: &b<ffa_kills>&8/&b<ffa_deaths>
&6Team PvP Victories&8: &b<teampvp_wins>
&6Team PvP Defeats&8: &b<teampvp_losses>

No permission message: &cYou dont have permission to use this command!
No permission to see other player stats: &cYou need to be premium to see others stats!
Player not found: &cPlayer not found.

Permission:
game.setlobby -> Allowed to set the server lobby
game.lobby -> Allowed to use /lobby command
game.record -> Can see personal stats
game.record.player -> Allowed to see other players stats
game.save -> Can save inventory while playing 1v1/2v2 until you logout
game.save.premium -> When having this permission your inventory will be saved the whole time
game.scoreboard -> Allowed to see top 10 players









#UNRANKED FFA

<displayname> = GroupManager prefix
Features:
Menus for all gamemodes
ELO
Game Rank (the leaderboard rank)

Commands:
/ffa -> Joins the ffa arena
/leave -> Leaves the ffa arena
/ffa setInv -> Sets the join inventory for ffa arena
/ffa setSpawn -> Sets the spawn point when joining ffa arena
/ffa setLeave -> Sets the leave point when leaving ffa arena

Messages:
Join FFA:
&6You succesfully joined the FFA arena.
&6Type &b/leave &6to get back to the lobby.

Leaves FFA:
&6You successfully left the FFA arena.

Kill: &6You have killed <displayname>&6!
Death: <displayname> &6had &b<health> &6health left when they killed you.

Permissions:
ffa.join -> Can join the ffa arena
ffa.leave -> Can leave the ffa arena
ffa.setinv -> Can set ffa inventory
ffa.setspawn -> Can set join point
ffa.setleave -> Can set leave point









#RANKED 1V1

<displayname> = GroupManager prefix
Features:
Menus for all gamemodes
ELO
Game Rank (the leaderboard position/rank)


Commands:
/1v1 -> Opens the 1v1 menu (Screenshot: )
/challenge <player> or /duel <player> -> Sends a 1v1 duel to a player
/1v1 arena add <arena> -> Creates a 1v1 arena
/1v1 arena remove <arena> -> Removes a 2v2 arena
/1v1 arena setSpectate <arena> -> Sets the spectate position
/1v1 arena setSpawn1 <arena> -> Sets the first spawn when joining the arena
/1v1 arena setSpawn2 <arena> -> Sets the second spawn when joining the arena
/1v1 arena setFinish <arena> -> Sets the position where the losser and winner teleports after the game
/1v1 setInv <arena> -> Sets the inventory + armor for the arena
/1v1 enable/disable <arena> -> Enabled or disabled a arena. Enabled = Can play Disabled = Cannot play

Messages:
Challenge:
&6A challenge has been sent to <displayname>&6.

Challenged (1v1):
&6You have been challenged by <displayname>&6!
&6Type &b/accept <player> &6to play!

Random Queue (this text shows up if you are in the random queue in 1v1): &6You are in position &b<position> &6of the queue.
In Queue against player (this will show up if you have been challenged by a player and accepted): &6You and <displayname> &6are in position &b<position> &6of the queue.

Player win broadcast: <displayname> &6just won against <displayname>&6!

You won game: &6You won the game and gained &b15 &6points.
You lost game: &6You lost the game and lost &b10 &6points.

Game teleprot: If you can do this, the countdown will be in the exp bar, so when it's 3 seconds until you teleport to the arena it says 3 then 2 then 1
Game starting: Same here, but now the counter starts at 5 seconds then 4 and so on...

You won round: &6You have won the round!
You lost round:
&6You have lost the round!
<displayname> &6had &b<health> &6health left when they killed you.

Game overview (shows up after every round while playing): <displayname &f<rounds won> &7: &f<rounds won> <displayname>

Save hotbar (default rank):
&6Saved your &bhotbar &6setup until you logout.
&6However, premium members get to keep it.
&6Navigate throught our premium packages using &b/buy&6!

Save hotbar (premium rank):
&6Saved your &bhotbar &6setup forever!
&6Thanks for supporting our server by donating.

Permissions:
1v1.menu -> This allows the group to open the 1v1 menus.
1v1.challenge -> Can challenge a player
1v1.accept -> Can accept a challenge from a player
1v1.rjoin -> Allowed to join a random map
1v1.join.* -> Allowed to choose what map to join
1v1.first -> Jumps before default rank in the queue
1v1.arena* -> Allowed to use all "arena create" commands (create,delete, setspawn, finish, inventory,enable/disable)










#TEAM PVP

<displayname> = GroupManager prefix
Features:
Menus for all gamemodes
ELO
Game Rank (the leaderboard rank)

Commands:
/2v2 -> Opens the 2v2 menu
/2v2 create -> Creates your team and you are the leader of it
/challenge team <team leader> or /duel team <team leader> -> Challenges a team to a 2v2 game
/accept team <team leader invitation> -> Accepts the 2v2 invitation
/2v2 arena add <arena> -> Creates a 2v2 arena
/2v2 arena remove <arena> -> Removes a 2v2 arena
/2v2 arena setSpectate <arena> -> Sets the spectate position
/2v2 arena setTeamRed1 <arena> -> Player 1/2 in team red will be teleported to this spawn when the game has started
/2v2 arena setTeamBlue1 <arena> -> Player 1/2 in team blue will be teleported to this spawn when the game has started
/2v2 arena setFinish <arena> -> Both teams will be teleported to this position when the match has ended
/2v2 setInv <team> <arena> -> Sets the inventory + armor for the team and arena.
/2v2 enable/disable <arena> -> Enabled or disabled a arena. Enabled = Can play Disabled = Cannot play

Messages:
Invited to team:
&6You have been invited to join &6Team <displayname>&6!
&6Type &b/accept team <player>&6 to join the team!

Team won broadcast: <teamcolor> <team leader+displayname> &6just won against <teamcolor> <team leader+displayname>&6!
Example; &cTeam &2Notch &6just won against &9Team &2Herobrine&6!

Game teleprot: If you can do this, the countdown will be in the exp bar, so when it's 3 seconds until you teleport to the arena it says 3 then 2 then 1
Game starting: Same here, but now the counter starts at 5 seconds then 4 and so on...

Your team won round: &6Your team have won the round!
You lost round:
&6Your team have lost the round!
Die message: <displayname> &6had &b<health> &6health left when they killed you.

Your team won game: &6Your team won the game and you gained &b15 &6points.
Your team lost game: &6Your team lost the game and you lost &b10 &6points.

Game overview (shows up after every round while playing):
&6Team <team leader> &f<rounds won> &7: &f<rounds won> &6Team <team leader>

Save hotbar (default rank):
&6Saved your &bhotbar &6setup until you logout.
&6However, premium members get to keep it.
&6Navigate throught our premium packages using &b/buy&6!

Save hotbar (premium rank):
&6Saved your &bhotbar &6setup forever!
&6Thanks for supporting our server by donating.

Player wants to join your team (message):
<displayname> &&requested to join your team!

Permissions:
2v2.menu -> Allowed to open all 2v2 menus
2v2.teamcreate -> Allowed to create 2v2 team
2v2.challenge.team -> Allowed to challenge a team
2v2.jointeam -> Can join a crafted team
2v2.accept.team -> Can accept a team challenge
2v2.rjoin -> Can join a random map when creating a 2v2 team
2v2.join.* -> Can join ALL maps in 2v2
2v2.arena.* -> Allowed to use all "arena create" commands (create,delete, setspawn, finish, inventory,enable/disable)

