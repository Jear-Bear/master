package me.joda.sumo;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener
{
	public HashMap<Player, Integer> roundsWon;
	int score1 = 0;
	int score2 = 0;
	int x;
	int z;
	ArrayList<Player> players = new ArrayList<>();
	Location spawn;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new sumoCommand(this), this);
		getCommand("sumo").setExecutor(new sumoCommand(this));
	}
}

