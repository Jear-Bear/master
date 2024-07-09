package me.joda.cursed;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(new CursedCommand(), this);
	}
	
	public void ondisable()
	{
		//shutdown logic
	}
}

