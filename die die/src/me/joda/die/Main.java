package me.joda.die;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new deathCommand(this), this);
		getCommand("death").setExecutor(new deathCommand(this));
	}
}
