package me.joda.talentShow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class talentCommand implements CommandExecutor, Listener {

	public Main main;
	
	public talentCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player && sender.isOp())
		{
			Location stage = new Location(((Player) sender).getWorld(), 69, 41, -66);
			stage.setYaw(-90f);
			stage.setPitch(0f);
			if (cmd.getName().equalsIgnoreCase("perform"))
			{
				if (args != null)
				{
					if (!main.placedBlocks.isEmpty()) main.placedBlocks.clear();
					for (String name : args)
					{
						String player = name;
						for (Player p : Bukkit.getOnlinePlayers())
						{
							if (p.getDisplayName().equalsIgnoreCase(player))
							{
								p.teleport(stage);
								p.setGameMode(GameMode.CREATIVE);
								main.performers.add(p);
							}
						}
					}
					Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() 
			        {
			            @Override
			            public void run() {
			            	String perform = "Performer";
			            	if (main.performers.size() > 1) perform += "s";
			            	Bukkit.broadcastMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + perform + ":");
			            	for (Player p : Bukkit.getOnlinePlayers())
			            	{
			            		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
			            	}
			            }
			        }, 20L);
					Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() 
			        {
			            @Override
			            public void run() {
			            	for (Player perf : main.performers)
			            	{
			            		Bukkit.broadcastMessage(ChatColor.GOLD + perf.getDisplayName());
			            	}
			            	for (Player p : Bukkit.getOnlinePlayers())
			            	{
			            		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
			            	}
			            }
			        }, 40L);
				}
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "You don't have permission to do this!");
		}
		return false;
	}
	
	@EventHandler
	public void place(BlockPlaceEvent e)
	{
		for (Player performer : main.performers)
		{
			if (performer != null)
			{
				if (e.getPlayer() == performer)
				{
					main.placedBlocks.add(e.getBlock());
				}
			}
		}
	}
	
	@EventHandler
	public void place(BlockBreakEvent e)
	{
		for (Player performer : main.performers)
		{
			if (performer != null)
			{
				if (e.getPlayer() == performer)
				{
					if (main.placedBlocks.contains(e.getBlock()))
					{
						main.placedBlocks.remove(e.getBlock());	
					}
					else
					{
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "Sorry, but you can't do that!");
					}
				}
			}
		}
	}
}
