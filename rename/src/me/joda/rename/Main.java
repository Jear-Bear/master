package me.joda.rename;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new renameCommand(this), this);
		getCommand("rename").setExecutor(new renameCommand(this));
	}
}

