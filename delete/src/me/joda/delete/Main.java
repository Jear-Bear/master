package me.joda.delete;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	HashMap <Player, Integer> blocks;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new chunkCommand(this), this);
		getCommand("chunk").setExecutor(new chunkCommand(this));
	}
}
