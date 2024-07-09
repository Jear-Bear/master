package me.joda.shrink;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import java.util.concurrent.ThreadLocalRandom;

public class shrinkCommand implements Listener, CommandExecutor {

	private Main main;
	public int time;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("shrink");
	int task;
	double size = 1.0;
	public shrinkCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("shrink"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("stop"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
						Bukkit.getScheduler().cancelTask(task);
				        return false;
					}
					else
					{
						size = 1.0;
						 time = 20 * Integer.parseInt(args[0]);
					        main.on = true;
					        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
					            @Override
					            public void run() {
					            	size -= .01;
					            	for (Player p : Bukkit.getOnlinePlayers()) 
					            	{
					            		CommandSender send = (CommandSender) p;
					            		Bukkit.dispatchCommand(send, "size " + size);
					            	}
					            }
					        }, 0L, time);
					}
				}
			}
		}
		return false;
	}
	
	

}
