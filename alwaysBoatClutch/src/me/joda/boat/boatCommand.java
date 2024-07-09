package me.joda.boat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.plugin.Plugin;

public class boatCommand implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("betterBoatClutch");
	
	public boatCommand(Main main) {
		this.main = main;
	}
	
	
	
	@EventHandler
	public void playerPlaceBoat(EntityPlaceEvent e)
	{
		if (e.getEntity().getName().equalsIgnoreCase("boat"))
		{
			main.PlayerClutching.put(e.getPlayer(), true);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

	            @Override
	            public void run(){
	                main.PlayerClutching.remove(e.getPlayer());
	            }
	        }, 8L);
	    }
		else if (e.getEntity().getName().equalsIgnoreCase("boat with chest"))
		{
			main.PlayerClutching.put(e.getPlayer(), true);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

	            @Override
	            public void run(){
	                main.PlayerClutching.remove(e.getPlayer());
	            }
	        }, 15L);
	    }
	}



	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@EventHandler
    public void onFallDamage(EntityDamageEvent e)
	{
        if(e.getEntity() instanceof Player)
        {
            if (e.getCause() == DamageCause.FALL)
            {
            	if (main.PlayerClutching.containsKey((Player) e.getEntity()))
            	{
            		if (main.PlayerClutching.get((Player) e.getEntity()))
            		{
            			e.setCancelled(true);
            		}
            	}
            }
        }
    }

}
