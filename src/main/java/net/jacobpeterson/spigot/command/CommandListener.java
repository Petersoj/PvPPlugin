package net.jacobpeterson.spigot.command;

import net.jacobpeterson.spigot.PvPPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {

    private final CommandHandler commandHandler;

    /**
     * Instantiates a new Command listener.
     *
     * @param pvpPlugin the pvp plugin
     */
    public CommandListener(PvPPlugin pvpPlugin) {
        this.commandHandler = new CommandHandler(pvpPlugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

        } else {
            return false;
        }
    }

    /**
     * Gets command handler.
     *
     * @return the command handler
     */
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
