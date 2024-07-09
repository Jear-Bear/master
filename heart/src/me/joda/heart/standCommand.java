package me.joda.heart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class standCommand implements Listener, CommandExecutor {

	private Main main;
	Location chestLoc;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("heart");
	public standCommand(Main main) {
		this.main = main;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("heart"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
						((Player) sender).resetMaxHealth();
				        return false;
					}
					else if (isInt(args[0]) || isDouble(args[0]))
					{
						main.on = true;
						if (isInt(args[0])) main.health = (double) Integer.parseInt(args[0]);
						else main.health = Double.parseDouble(args[0]);
						((Player) sender).setMaxHealth(main.health);
					}
					else if (!isInt(args[0]))
					{
						sender.sendMessage(ChatColor.RED + "Enter what health you wantt! (default is 20) \n" + ChatColor.GOLD.toString() + ChatColor.BOLD + "Usage: " + ChatColor.GRAY + " (/table 9)");
					}
				}
			}
		}
		return false;
	}
	
	
	public static boolean isInt(String str) {
	  	try {
	      	@SuppressWarnings("unused")
	    	int x = Integer.parseInt(str);
	      	return true;
		} catch (NumberFormatException e) {
	    	return false;
		}
	}
	
	public static boolean isDouble(String str) {
	  	try {
	      	@SuppressWarnings("unused")
	    	double x = Double.parseDouble(str);
	      	return true;
		} catch (NumberFormatException e) {
	    	return false;
		}
	}

}
