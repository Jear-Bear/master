package me.joda.hearts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class heartCommand implements Listener, CommandExecutor {

	private Main main;
	boolean clickTable = false;
	boolean poop = false;
	Location craftLoc;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("mobBoom");
	public heartCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("heart"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off") && main.p.isOp())
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					if (args[0].equalsIgnoreCase("on") && main.p.isOp())
					{
						main.on = true;
						sender.sendMessage(ChatColor.GREEN + "Plugin enabled");
				        return false;
					}
					if (!main.p.isOp())
					{
						sender.sendMessage(ChatColor.RED + "Sorry, but you can't use this command!");
					}
				}
			}
		}
		return false;
	}
	

}
