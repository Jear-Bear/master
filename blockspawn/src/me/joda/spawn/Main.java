package me.joda.spawn;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = true;
	boolean won;
	ArrayList<Player> hiders = new ArrayList<>();
	ArrayList<Player> seeker = new ArrayList<>();
	ArrayList<Player> inGamePlayers = new ArrayList<>();
	int x, z;
	boolean readyOrNot = false;
	boolean death = false;
    
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new breakCommand(this), this);
		getCommand("break").setExecutor(new breakCommand(this));
	}
	
	public int getRand() 
	{
        if (Math.random() > 0.5) return (int)(Math.random() * 8000.0);
        return (int)(Math.random() * -8000.0);
    }
}