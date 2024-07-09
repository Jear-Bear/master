package me.jodabeats.kaonashi;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;


import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener
{
	public boolean inGame = false;
	public boolean death = false;
	Inventory shared;
	public FileConfiguration config;
    public void onEnable() {
    	if (inGame == true) inGame = false;
    	if (inGame == false) inGame = true;
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("kaonashi").setExecutor((CommandExecutor)new CommandExe(this));
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
    
    public void start(Player p)
    {
    	if (inGame == true)
    	{
    		Bukkit.broadcastMessage("it has started!");
        	if (shared != null) shared.clear();
        	if (death)
        	{
        		for (Player player : Bukkit.getOnlinePlayers())
        		{
        			player.setHealth(0.0);
        		}
        		death = false;
        	}
    	}
    	else Bukkit.broadcastMessage("turned off!");
    }
    
    @EventHandler
    public void targetDeath(PlayerDeathEvent e)
    {
    	e.setDeathMessage("");
    	death = true;
    }
    
    @EventHandler
    public void drop(PlayerDropItemEvent event)
    {
    	if(inGame && shared != null)
    	{
    		for(int i = 0; i < shared.getSize(); i++) {
    		    ItemStack is = shared.getItem(i);
    		      
    		    if(is != null) {
    		        if(is.getType() == event.getItemDrop().getItemStack().getType()) {
    		        	shared.getItem(i).setAmount(is.getAmount() - 1);
    		        	return;
    		         }
    		    }
    		}
    		for(Player play : this.getServer().getOnlinePlayers()) {
            	play.getInventory().setContents(shared.getContents());
            	play.updateInventory();
            } 
    	}
    }
    
    @EventHandler
    public void place(BlockPlaceEvent event)
    {
    	if(inGame && shared != null)
    	{
    		for(int i = 0; i < shared.getSize(); i++) {
    		    ItemStack is = shared.getItem(i);
    		    if(is != null) {
    		        if(is.getType() == event.getBlock().getType()) {
    		        	shared.getItem(i).setAmount(is.getAmount() - 1);
    		        	return;
    		         }
    		    }
    		}
    		for(Player play : this.getServer().getOnlinePlayers()) {
            	play.getInventory().setContents(shared.getContents());
            	play.updateInventory();
            } 
    	}
    }
    
    @EventHandler
	public void pickup(EntityPickupItemEvent e)
	{
    	if(inGame && shared != null)
    	{
    		shared.addItem(e.getItem().getItemStack());
    		for(Player play : this.getServer().getOnlinePlayers()) {
            	play.getInventory().setContents(shared.getContents());
            	play.updateInventory();
            } 
    	}
	}
    
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
    	if (e.getEntity() instanceof Player)
    	{
        	for (Player p : Bukkit.getOnlinePlayers())	
        	{
        		if (p != (Player) e.getEntity()) p.damage(e.getDamage());
        	}
    	}
    }
    
    @EventHandler
    public void damageFromMob(EntityDamageByEntityEvent e)
    {
    	if (!(e.getDamager() instanceof  Player) && e.getEntity() instanceof Player)
    	{
    		for (Player p : Bukkit.getOnlinePlayers())	
        	{
        		if (p != (Player) e.getEntity()) p.damage(e.getDamage());
        	}
    	}
    }
}