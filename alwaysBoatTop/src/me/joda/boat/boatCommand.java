package me.joda.boat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.entity.EntityDismountEvent;

public class boatCommand implements Listener, CommandExecutor {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("betterBoatDismount");
	
	public boatCommand(Main main) {
	}



	@EventHandler
	public void playerPlaceBoat(EntityDismountEvent e)
	{
		if (e.getDismounted().getName().equalsIgnoreCase("boat") || e.getDismounted().getName().equalsIgnoreCase("boat with chest"))
		{
			if (e.getEntity().getLocation().distance(e.getDismounted().getLocation()) == 0.0) 
			{
				e.setCancelled(true);
				e.getEntity().teleport(new Location(e.getEntity().getWorld(), e.getDismounted().getLocation().getX(), e.getDismounted().getLocation().getY() + 1.5, e.getDismounted().getLocation().getZ(), e.getEntity().getLocation().getYaw(), e.getEntity().getLocation().getPitch()));
				Location l = e.getDismounted().getLocation();
				Boat boat = (Boat) e.getEntity().getWorld().spawnEntity(l, e.getDismounted().getType());
				boat.setBoatType(((Boat) e.getDismounted()).getBoatType());
				e.getDismounted().remove();
				
			}
		}
	}



	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
