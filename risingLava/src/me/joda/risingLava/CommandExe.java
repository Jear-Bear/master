package me.joda.risingLava;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandExe implements CommandExecutor
{
    public Main plugin;
    
    public CommandExe(final Main plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
    	final Player player = (Player)sender;
        if (label.equalsIgnoreCase("rise")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be used by the player.");
                return false;
            }
            else this.plugin.nextGame(player);
        }
        return true;
    }
}