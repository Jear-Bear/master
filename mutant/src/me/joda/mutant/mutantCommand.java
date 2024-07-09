package me.joda.mutant;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class mutantCommand implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("warden");
	int task;
	int secondCount = 0;
	int min = 0;
	int sec = 0;
	CommandSender send;
	public mutantCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("warden"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
						Bukkit.getScheduler().cancelTask(task);
				        return false;
					}
					else if (Integer.parseInt(args[0]) != 0)
					{
						main.minutes = Integer.parseInt(args[0]);
					    main.on = true;
					    timer(main.minutes * 60);
					    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					        @Override
					        public void run() {
					        	for (int i = 0; i <= 9; i++)
					        	{
					        		Bukkit.dispatchCommand(sender, "summon minecraft:pig" /*"summon caves_and_cliffs_mod:warden"*/);
					        	}
					        }
					    }, main.minutes*60*20);
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Please enter how many minutes you want between spawns!" + ChatColor.GRAY.toString() + " (/warden 3)");
					}
				}
			}
		}
		return false;
	}
	
	public void timer(int seconds)
	{
		int total = seconds*20;
		min = 0;
		sec = 0;
		secondCount = 0;
		for (int i = 0; i < total; i+=20)
        {	
    		Bukkit.getServer().getScheduler().runTaskLater(main, (Runnable)new Runnable() {
				@Override
                public void run() {
					secondCount += 1;
    				int total = seconds - secondCount;
    				min = total / 60;
    				sec = total % 60;
    				String secon = String.valueOf(sec);
    				if (secon.length() == 1) secon = "0" + secon;
    				String color = "";
    				if ((((double)(main.minutes*60 - secondCount) / (double)(main.minutes * 60)) * 100) > 80) color = ChatColor.GREEN + ChatColor.BOLD.toString();
    				else if ((((double)(main.minutes*60 - secondCount) / (double)(main.minutes * 60)) * 100) > 60) color = ChatColor.YELLOW + ChatColor.BOLD.toString();
    				else if ((((double)(main.minutes*60 - secondCount) / (double)(main.minutes * 60)) * 100) > 40) color = ChatColor.GOLD + ChatColor.BOLD.toString();
    				else if ((((double)(main.minutes*60 - secondCount) / (double)(main.minutes * 60)) * 100) > 20) color = ChatColor.RED + ChatColor.BOLD.toString();
    				else color = ChatColor.DARK_RED + ChatColor.BOLD.toString();
					String message = color + min + ":" + secon;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
					} 
    			}
    		}, i);
        }
	}

}
