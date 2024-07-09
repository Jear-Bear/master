package me.joda.rename;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class renameCommand implements Listener, CommandExecutor {

	
	public renameCommand(Main main) {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rename"))
		{
			if (args != null)
			{
				if (sender instanceof Player)
				{
					((Player) sender).setPlayerListName(args[0]);
					((Player) sender).setDisplayName(args[0]);
					((Player) sender).setCustomName(args[0]);
					((Player) sender).setCustomNameVisible(true);
				}
			}
		}
		return false;
	}

}
