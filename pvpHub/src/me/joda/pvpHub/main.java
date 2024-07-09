package me.joda.pvpHub;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class main extends JavaPlugin implements Listener
{      
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
	}
	
    @EventHandler
    public void triggerpvp(PlayerMoveEvent e)
    {
    		if (e.getPlayer().getFoodLevel() < 20)
    		{
    			e.getPlayer().setFoodLevel(20);
    		}
    		if(e.getPlayer().getLocation().getY() < 0.0)
    		{
    			Location spawn = e.getPlayer().getWorld().getSpawnLocation();
    			spawn.setPitch(0f);
    			spawn.setYaw(90f);
    			Vector still = new Vector(0, 0, 0);
    			e.getPlayer().setVelocity(still);
    			e.getPlayer().teleport(spawn);
    		}
    }
    
    @EventHandler
    public void portal(PlayerPortalEvent e)
    {
    	e.setCancelled(true);
    }
    
    @EventHandler
    public void breakBlock(BlockBreakEvent e)
    {
    	if ((e.getBlock().getType() == Material.BARRIER && !e.getPlayer().isOp()) || e.getPlayer().getGameMode() != GameMode.CREATIVE)
    	{
    		e.setCancelled(true);
    		e.getPlayer().sendMessage(ChatColor.RED + "Sorry, but you can't do that!");
    	}
    }
    
    @EventHandler
    public void join(PlayerJoinEvent e)
    {
		if (!e.getPlayer().hasPlayedBefore()) 
    	{
    		e.setJoinMessage("");
    		for (Player p : Bukkit.getOnlinePlayers())
    		{
    			p.sendMessage("Welcome " + e.getPlayer().getDisplayName() + " to the talent show!");
    		}
    	}
    	else 
    	{
    		e.setJoinMessage("");
    		for (Player p : Bukkit.getOnlinePlayers())
    		{
    			p.sendMessage(e.getPlayer().getDisplayName() + " has joined!");
    		}
    	}
		Location spawn = new Location(e.getPlayer().getWorld(), 216, 40, -67);
		spawn.setYaw(90f);
		spawn.setPitch(0f);
    	e.getPlayer().teleport(spawn);
    	e.getPlayer().getInventory().clear(); 
    }
    
    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
		e.setQuitMessage("");
    }
    
    @EventHandler
    public void noDrop(PlayerDropItemEvent e)
    {
    	e.setCancelled(true);
    }
    
    @EventHandler
    public void damage(EntityDamageEvent e)
    {
		if (e.getEntity().getLocation().getBlock().getBiome() != Biome.BASALT_DELTAS)
		{
			e.setCancelled(true);
		}
    }
    
    @EventHandler
    public void pvp(EntityDamageByEntityEvent e)
    {
		if (e.getEntity().getLocation().getBlock().getBiome() != Biome.BASALT_DELTAS)
    	{
    		e.setCancelled(true);
    	}
    }
}