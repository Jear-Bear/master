package com.joda.toggle;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(new ToggleListener(), this);
	}
}
