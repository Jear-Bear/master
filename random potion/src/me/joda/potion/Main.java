package me.joda.potion;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener
{
	public HashMap<Player, Integer> roundsWon;
	boolean pluginOn;
	ArrayList<Player> players = new ArrayList<>();
	Location spawn;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new potionCommand(this), this);
		getCommand("random").setExecutor(new potionCommand(this));
	}
	
	public boolean getPluginStatus()
	{
		return pluginOn;
	}
	
	public void setPluginStatus(boolean b)
	{
		pluginOn = b;
	}
}

