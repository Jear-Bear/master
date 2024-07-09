package me.joda.hungergames;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public FileConfiguration config;
	public boolean on = false;
	public Player egg;
	public Player joda;
	public Player ben;
	CommandSender console;
	public ArrayList<Material> brokeType = new ArrayList<>();
	public ArrayList<Location> brokeLoc = new ArrayList<>();
	public ArrayList<Location> placed = new ArrayList<>();
	int value = 5;
	boolean pvp= true;
	public HashMap<Player, Location> respawn = new HashMap<Player, Location>();
	
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("count").setExecutor((CommandExecutor)new countCommand(this));
        this.getCommand("hungergames").setExecutor((CommandExecutor)new hungergamesCommand(this));
        this.getCommand("rollback").setExecutor((CommandExecutor)new rollback(this));
        getServer().getPluginManager().registerEvents(new countCommand(this), this);
        getServer().getPluginManager().registerEvents(new hungergamesCommand(this), this);
        getServer().getPluginManager().registerEvents(new tracking(this), this);
        getServer().getPluginManager().registerEvents(new rollback(this), this);
        getServer().getPluginManager().registerEvents(new deathSystem(this), this);
        
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
}