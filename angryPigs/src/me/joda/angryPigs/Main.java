package me.joda.angryPigs;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public FileConfiguration config;
	boolean on = false;
	Player runner;
	
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("pigs").setExecutor((CommandExecutor)new pigsCommand(this));
        getServer().getPluginManager().registerEvents(new pigsCommand(this), this);
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
}