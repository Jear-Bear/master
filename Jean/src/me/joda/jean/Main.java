package me.joda.jean;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	ArrayList<Player> target = new ArrayList<>();
	ArrayList<Player> hunter = new ArrayList<>();
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new jeanCommand(this), this);
		getCommand("jean").setExecutor(new jeanCommand(this));
	}
}
