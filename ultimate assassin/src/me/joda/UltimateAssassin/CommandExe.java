package me.joda.UltimateAssassin;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandExe implements CommandExecutor
{
    public main plugin;
    
    public CommandExe(final main plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (label.equalsIgnoreCase("assassin")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("[Ultimate Assassin ] This command can only be used by the player.");
                return true;
            }
            final Player player = (Player)sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("nextGame")) {
                    this.plugin.nextGame(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("help")) {
                    this.plugin.helpMenu(player);
                    return true;
                }
            }
            if (args.length == 3 && args[0].equalsIgnoreCase("start")) {
                this.plugin.startGame(args[1], args[2], player);
                return true;
            }
            this.plugin.helpMenu(player);
        }
        return false;
    }
}