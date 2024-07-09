package me.joda.soldier;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new jumpCommand(this), this);
		getCommand("jump").setExecutor(new jumpCommand(this));
	}
}
