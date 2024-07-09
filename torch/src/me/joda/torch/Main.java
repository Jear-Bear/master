package me.joda.torch;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	int distance = 100;
	Player p;
	boolean name = false;
	String playerName = "";
	boolean ask = false;
	boolean askTake = false;
	boolean fun = false;
	Player messager;
	Location torchLoc;
	boolean accept = false;
	boolean drop = false;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new torchCommand(this), this);
		getCommand("torch").setExecutor(new torchCommand(this));
	}
}
