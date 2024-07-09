package me.joda.delete;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class chunkCommand implements Listener, CommandExecutor {

	private Main main;
	int y;
	public chunkCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.toString().toLowerCase().contains("chunk"))
		{
			if (args != null)
			{
				if (args[0].contains("on"))
				{
					main.on = true;
					sender.sendMessage("Plugin enabled!");
				}
				if (args[0].contains("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disbled!");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void chunkLeave(PlayerMoveEvent e)
	{
		
		Location from = e.getFrom();
	    Location to = e.getTo();
	    y = from.getChunk().getChunkSnapshot().getHighestBlockYAt(e.getFrom().getChunk().getX(), e.getFrom().getChunk().getZ());
	    int b = y;
	    if (from.getChunk() != to.getChunk())
	    {
	    	for (int a = 0; a <= 20*b; a+= 20)
            {
            		Bukkit.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
    	                @Override
    	                public void run() {
    	                	y -= 1;
    	                	for (int x = 0; x <= 15; x++)
    	        	    	{
	        	    			for (int z = 0; z <= 15; z++)
	        	    			{
	        	    				from.getChunk().getBlock(x, y, z).setType(Material.AIR);
	        	    			}
    	        	    	}
    	                }
    	            }, a);
            	}
            	
            }
	    	
	    }

}
