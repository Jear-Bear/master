package me.joda.judge;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class judgeCommand implements CommandExecutor {

	public Main main;
	
	public judgeCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player && sender.isOp())
		{
			Location judge = new Location(((Player) sender).getWorld(), 101, 37, -67);
			judge.setYaw(90f);
			judge.setPitch(0f);
			if (cmd.getName().equalsIgnoreCase("judge"))
			{
				if (sender instanceof Player)
				{
					Player j = (Player) sender;
					j.teleport(judge);
				}
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "You don't have permission to do this!");
		}
		return false;
	}
}
