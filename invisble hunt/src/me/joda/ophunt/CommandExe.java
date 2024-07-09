package me.joda.ophunt;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandExe implements CommandExecutor, Listener
{
    public main main;
    public CommandExe(main main) {
        this.main = main;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
    	final Player player = (Player)sender;
        if (cmd.toString().toLowerCase().contains("lag")) {
            if(main.target != null)
            {
            	main.start(player);
            }
        }
        return false;
    }
}