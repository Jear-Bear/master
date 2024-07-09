package me.joda.shrink;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new shrinkCommand(this), this);
		getCommand("shrink").setExecutor(new shrinkCommand(this));
	}
}
