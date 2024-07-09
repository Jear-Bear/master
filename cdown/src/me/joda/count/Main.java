package me.joda.count;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public FileConfiguration config;
	int value = 5;
	boolean pvp;
	
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("count").setExecutor((CommandExecutor)new countCommand(this));
        getServer().getPluginManager().registerEvents(new countCommand(this), this);
        getServer().getPluginManager().registerEvents(new tracking(this), this);
        getServer().getPluginManager().registerEvents(new countCommand(this), this);
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
}