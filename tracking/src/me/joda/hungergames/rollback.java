package me.joda.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class rollback implements Listener, CommandExecutor {

	private Main main;
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("hungergames");
	
	public rollback(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rollback"))
		{
			if (main.placed != null)
			{
				for (Location l : main.placed)
				{
					l.getBlock().setType(Material.AIR);
				}
				main.placed.clear();
			}
			if (main.brokeType != null && main.brokeLoc != null)
			{
				for (Location l : main.brokeLoc)
				{
					l.getBlock().setType(main.brokeType.get(main.brokeLoc.indexOf(l)));
				}
				main.brokeType.clear();
				main.brokeLoc.clear();
			}
		}
		return false;
	}
	
	@EventHandler
	void track(BlockPlaceEvent e)
	{
		main.placed.add(e.getBlock().getLocation());
	}
	
	@EventHandler
	void track(BlockBreakEvent e)
	{
		main.brokeType.add(e.getBlock().getType());
		main.brokeLoc.add(e.getBlock().getLocation());
	}

}
