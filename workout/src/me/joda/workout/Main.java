package me.joda.workout;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	boolean workout;
	boolean armsOn;
	boolean legsOn;
	boolean backOn;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new workoutCommand(this), this);
		getCommand("workout").setExecutor(new workoutCommand(this));
	}
}

