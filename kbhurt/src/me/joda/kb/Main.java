package me.joda.kb;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public FileConfiguration config;
	public boolean on = false;
	HashMap<Player, Integer> Damage = new HashMap<Player, Integer>();
	HashMap<Player, Double> Distance = new HashMap<Player, Double>();
	int time = 0;
	Location start;
	Location finish;
	
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("kb").setExecutor((CommandExecutor)new kbCommand(this));
        getServer().getPluginManager().registerEvents(new kbCommand(this), this);
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
}