package me.joda.poop;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	int distance;
	Player p;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new poopCommand(this), this);
		getCommand("poop").setExecutor(new poopCommand(this));
	}
}
