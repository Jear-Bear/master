package me.joda.cursed;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on;
	ArrayList<Player> hiders = new ArrayList<>();
	ArrayList<Player> seeker = new ArrayList<>();
	int x, z;
	Location spawn;
	double wbSize = 500.0;
	boolean readyOrNot = false;
    
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new cursedCommand(this), this);
		getCommand("cursed").setExecutor(new cursedCommand(this));
	}
	
}