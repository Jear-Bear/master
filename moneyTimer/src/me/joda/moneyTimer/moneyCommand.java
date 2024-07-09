package me.joda.moneyTimer;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;



public class moneyCommand implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("moneyTimer");
	String baseMessage = ChatColor.WHITE + "    /money ";
	TextComponent message;
	double speed;
	String[][] commands = new String[][]{
		{ChatColor.YELLOW + "start", ChatColor.GREEN + "Start " + ChatColor.YELLOW.toString() + "the timer (" + ChatColor.AQUA.toString() + "/money on" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "stop", ChatColor.RED + "Stop " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "and reset" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " the timer (" + ChatColor.AQUA.toString() + "/money off" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "1234", ChatColor.YELLOW + "Set the timer's starting amount (" + ChatColor.AQUA.toString() + "e.g. " + ChatColor.GREEN.toString() + "$1234.27 " + ChatColor.YELLOW + "or" + ChatColor.RED + " -$3468.00"+ ChatColor.YELLOW.toString() + ") for the timer"},
		{ChatColor.YELLOW + "speed 10", ChatColor.YELLOW + "Set the increase/decrease" + ChatColor.AQUA.toString() + " speed " + ChatColor.YELLOW.toString()+ "of the timer (" + ChatColor.GREEN.toString() + "+$ " + ChatColor.YELLOW.toString() + "or " + ChatColor.RED.toString() + "-$ " + ChatColor.YELLOW.toString() + "per second)"},
		{ChatColor.YELLOW + "pause", ChatColor.YELLOW + "Pause the timer"},
		{ChatColor.YELLOW + "unpause", ChatColor.YELLOW + "Unpause the timer"},
		{ChatColor.YELLOW + "end", ChatColor.RED + "Ends " + ChatColor.YELLOW.toString() + "the timer and shows a " + ChatColor.GOLD.toString() + ChatColor.UNDERLINE.toString() + "pretty title card!"},
		{ChatColor.YELLOW + "currency R$", ChatColor.YELLOW + "Change the " + ChatColor.GREEN.toString() + "currency"},
		{ChatColor.YELLOW + "help", ChatColor.YELLOW + "Pulls up this " + ChatColor.GOLD.toString() + ChatColor.UNDERLINE.toString() + "super helpful" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " list of commands you're looking at now"}
		};
	
	public moneyCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("money"))
			{
				if (args.length > 0)
				{
					if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("stop"))
					{
						main.on = false;
						Bukkit.getScheduler().cancelTasks(plugin);
						sender.sendMessage(ChatColor.RED + "Timer stopped!");
				        return false;
					}
					if (args[0].equalsIgnoreCase("pause"))
					{
						speed = main.speed;
						main.speed = 0;
						sender.sendMessage(ChatColor.YELLOW + "Timer paused!");
				        return false;
					}
					if (args[0].equalsIgnoreCase("unpause"))
					{
						sender.sendMessage(ChatColor.YELLOW + "Timer unpaused!");
						main.speed = speed;
				        return false;
					}
					else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("start"))
					{
						Bukkit.getScheduler().cancelTasks(plugin);
						main.on = true;
						main.timerRunning = true;
						if (main.speed == 0)
						{
							main.speed = -.1;
						}
						if (main.startingMoney == -1)
						{
							
							main.moneyTimer.moneyTimer(1000, main, plugin);
						}
						else main.moneyTimer.moneyTimer(main.startingMoney, main, plugin);
					}
					else if(args[0].equalsIgnoreCase("speed"))
					{
						if (args.length > 1)
						{
							if (args[1] != null)
							{
								if (isDouble(args[1]))
								{

									main.speed = Double.parseDouble(args[1]);
									main.moneySpeed.setSpeed(main.speed, main.p);
								}
							}
						}
					}
					else if (args[0].equalsIgnoreCase("help"))
					{
						sender.sendMessage(ChatColor.AQUA + "Usage (hover for info, click to run):");
						for(int i = 0; i < commands.length; i++)
						{
							message = new TextComponent("");
							message.setText(baseMessage + commands[i][0]);
							message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(commands[i][1])));
							message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/money " + ChatColor.stripColor(commands[i][0])));
						    sender.spigot().sendMessage(message);
						}
					}
					else if (args[0].equalsIgnoreCase("end"))
					{
						main.timerRunning = false;
						main.on = false;
					}
					else if (args[0].equalsIgnoreCase("currency"))
					{
						if (args.length > 1)
						{
							if (args[1] != null)
							{
								main.currency = args[1];
							}
						}
					}
					else if (isDouble(args[0]))
					{
						main.startingMoney = Double.parseDouble(args[0]);
					}
				}
			}
		}
		return false;
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
