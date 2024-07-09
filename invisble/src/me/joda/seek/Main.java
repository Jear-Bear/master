package me.joda.seek;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = true;
	boolean won;
	ArrayList<Player> hiders = new ArrayList<>();
	ArrayList<Player> seeker = new ArrayList<>();
	ArrayList<Player> inGamePlayers = new ArrayList<>();
	int x, z;
	boolean readyOrNot = false;
	boolean death = false;
    
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new seekCommand(this), this);
		getCommand("seek").setExecutor(new seekCommand(this));
	}
	
	public int getRand() 
	{
        if (Math.random() > 0.5) return (int)(Math.random() * 8000.0);
        return (int)(Math.random() * -8000.0);
    }
}