package me.joda.itemShuffle;

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



public class shuffleCommand implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("itemShuffle");
	String baseMessage = ChatColor.WHITE + "    /shuffle ";
	TextComponent message;
	double speed;
	String[][] commands = new String[][]{
		{ChatColor.YELLOW + "start", ChatColor.GREEN + "Start " + ChatColor.YELLOW.toString() + "the game (" + ChatColor.AQUA.toString() + "/shuffle on" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "stop", ChatColor.RED + "Stop " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "and reset" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " the game (" + ChatColor.AQUA.toString() + "/shuffle off" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "help", ChatColor.YELLOW + "Pulls up this " + ChatColor.GOLD.toString() + ChatColor.UNDERLINE.toString() + "super helpful" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " list of commands you're looking at now"}
		};
	
	public shuffleCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("shuffle"))
			{
				if (args.length > 0)
				{
					if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("stop"))
					{
						main.on = false;
						Bukkit.getScheduler().cancelTasks(plugin);
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("start"))
					{
						Bukkit.getScheduler().cancelTasks(plugin);
						main.on = true;
						main.moneyTimer.moneyTimer(360, main, plugin);
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
				}
			}
		}
		return false;
	}

}
