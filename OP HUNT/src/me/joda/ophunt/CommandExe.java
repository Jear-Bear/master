package me.joda.ophunt;

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
    	final Player player = (Player)sender;
        if (label.equalsIgnoreCase("hunt")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be used by the player.");
                return false;
            }
            else this.plugin.start(player);
        }
        return true;
    }
}