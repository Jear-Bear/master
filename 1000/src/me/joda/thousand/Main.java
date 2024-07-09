package me.joda.thousand;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new thousandCommand(this), this);
		getCommand("1k").setExecutor(new thousandCommand(this));
	}
}
