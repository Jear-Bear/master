package me.joda.timer;

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
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("timer");
	String baseMessage = ChatColor.WHITE + "    /timer ";
	TextComponent message;
	double speed;
	String[][] commands = new String[][]{
		{ChatColor.YELLOW + "start", ChatColor.GREEN + "Start " + ChatColor.YELLOW.toString() + "the timer (" + ChatColor.AQUA.toString() + "/money on" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "1234", ChatColor.YELLOW + "Set the timer's starting time in minutes (" + ChatColor.AQUA.toString() + "e.g. " + ChatColor.GREEN.toString() + "120" + ChatColor.YELLOW + ChatColor.YELLOW.toString() + " for the timer"},
		{ChatColor.YELLOW + "end", ChatColor.RED + "Ends " + ChatColor.YELLOW.toString() + "the timer and shows a " + ChatColor.GOLD.toString() + ChatColor.UNDERLINE.toString() + "pretty title card!"},
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
			if (cmd.getName().equalsIgnoreCase("timer"))
			{
				if (args.length > 0)
				{
					if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("start"))
					{
						Bukkit.getScheduler().cancelTasks(plugin);
						main.on = true;
						main.timerRunning = true;
						main.runner = (Player) sender;
						if (main.startingMoney == -1)
						{
							main.moneyTimer.moneyTimer(60, main, plugin);
						}
						else main.moneyTimer.moneyTimer(main.startingMoney, main, plugin);
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
					else if (isNumeric(args[0]))
					{
						main.startingMoney = Double.parseDouble(args[0]);
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

}
