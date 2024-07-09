package me.joda.hearts;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import java.util.Objects;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	int distance = 100;
	Player p;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new heartCommand(this), this);
		getCommand("boom").setExecutor(new heartCommand(this));
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent e)
	{
		
	}
}
