package com.joda.health;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public static Plugin instance;
	@Override
	public void onEnable()
	{
		this.getConfig().options().copyDefaults();
		this.saveDefaultConfig();
		
		instance = this;
		getCommand("health").setExecutor(new HealCommand());
		getCommand("hunger").setExecutor(new HungerCommand());
		getCommand("day").setExecutor(new DayCommand());
		getCommand("night").setExecutor(new NightCommand());
		getCommand("sunny").setExecutor(new SunnyCommand());
		getCommand("rain").setExecutor(new RainCommand());
		getCommand("thunder").setExecutor(new ThunderCommand());
	}
}
