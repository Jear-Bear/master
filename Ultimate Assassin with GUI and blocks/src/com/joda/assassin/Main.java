package com.joda.assassin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener
{
	int x, z;
	int runnerPoints = 0;
	int assassinPoints = 0;
	boolean inGame = false;
	Player runner;
	ArrayList<Player> players = new ArrayList<>();
	ArrayList<Player> assassins = new ArrayList<>();
	ArrayList<Location> powerBlocks = new ArrayList<>();
	Location spawn;
	countdown cDown = new countdown();
	assassinCommand assassin = new assassinCommand(this);
	
	@Override
	public void onEnable()
	{
		assassin.makeInvs();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new assassinCommand(this), this);
		getServer().getPluginManager().registerEvents(new logDrop(this), this);
		getCommand("start").setExecutor(new assassinCommand(this));
	}

	public void findSpawn(Player p)
	{
		x = getRand();
		z = getRand();
		spawn = p.getWorld().getHighestBlockAt(x, z).getLocation();
		if (spawn.getBlock().getBiome().toString().toLowerCase().contains("ocean"))
		{
			while (spawn.getBlock().getBiome().toString().toLowerCase().contains("ocean"))
			{
				x = getRand();
				z = getRand();
				spawn = p.getWorld().getHighestBlockAt(x, z).getLocation();
			}
		}
	}
	
	public void countdownTitle(int i)
	{
		cDown.countDownTitle(i, (Plugin) this);
	}
	
	public void nextRound(Player player)
	{
		assassin.startRound(player);
	}
	
	public int getRand() 
	{
        if (Math.random() > 0.5) return (int)(Math.random() * 8000.0);
        return (int)(Math.random() * -8000.0);
    }
}
