package me.joda.hearts;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.plugin.Plugin;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import java.util.Objects;
import java.util.Random;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	int distance = 100;
	Player p;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new heartCommand(this), this);
		getCommand("hearts").setExecutor(new heartCommand(this));
		this.saveDefaultConfig();
	}
	
	public void task() {
        Calendar c = Calendar.getInstance();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (c.get(Calendar.HOUR_OF_DAY) == 24 && c.get(Calendar.MINUTE) == 00 && c.get(Calendar.SECOND) == 00) {
                	setAllPlayerHealth();
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent e)
	{	
		//if player hasn't joined before, add them to configuration file, and assign a random health
		if (!isPlayerInConfig(e.getPlayer()))
		{
			double health = randomHeart();
			addPlayer(e.getPlayer(), health);
			e.getPlayer().setMaxHealth(health);
		}
		
		//if they have joined before
		else if (e.getPlayer().getMaxHealth() != getHealth(e.getPlayer()))
		{
			//but don't have a stored health value, make one and set the player's health
			if (getHealth(e.getPlayer()) == 0.0) 
			{
				double health = randomHeart();
				setHealth(e.getPlayer(), health);
				e.getPlayer().setHealth(health);
			}
			
			//but their health doesn't match, set it
			else e.getPlayer().setMaxHealth(getHealth(e.getPlayer()));
		}
	}
	
	//add player to configuration file
	public void addPlayer(Player player, double health) {   
		this.getConfig().set("UUID", player.getUniqueId());
		this.getConfig().set("UUID.Hearts", health);
    }
	
	//check if the player is listed in the configuration file
	public boolean isPlayerInConfig(Player player) {
		  if (this.getConfig().contains(player.getUniqueId().toString()))
		  {
			  return true;
		  }
		  else return false;
		}
	
	//return random value between 1-10
	public double randomHeart()
	{
		//generate random number between 0.01-20.0
		Random rand = new Random();
		int health = (rand.nextInt(10) + 1) * 2;
		return health;
	}
	
	//get the player health stored in the configuration file
	public double getHealth(Player p)
	{
		//iterate through each player in configuration file
		for(String key : this.getConfig().getKeys(false)) {
			//check if player matches the argument
			if (key == p.getUniqueId().toString())
			{
				//return the player health
				return this.getConfig().getDouble(key + ".Hearts");
			}
		}
		return 0.0;
	}
	
	//set the player health in the configuration file
	public void setHealth(Player p, double health)
	{
		//iterate through each player in configuration file
		for(String key : this.getConfig().getKeys(false)) {
			//check if player matches the argument
			if (key == p.getUniqueId().toString())
			{
				//set the player health
				this.getConfig().set(key + ".Hearts", health);
			}
		}
	}
	
	//goes through configuration file and assigns EVERY player with a random health
	public void setAllPlayerHealth()
	{
		for(String key : this.getConfig().getKeys(false)) {
			double health = randomHeart();
			this.getConfig().set(key + ".Hearts", health);
		}
		for (Player p : Bukkit.getServer().getOnlinePlayers())
		{
			p.setMaxHealth(getHealth(p));
		}
	}
}
