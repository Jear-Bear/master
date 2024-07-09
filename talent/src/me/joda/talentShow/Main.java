package me.joda.talentShow;

import java.util.ArrayList;

import org.bukkit.block.Block;
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
	public ArrayList<Block> placedBlocks = new ArrayList<>();
	public ArrayList<Player> performers = new ArrayList<>();
	
    public void onEnable() {
    	getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("perform").setExecutor((CommandExecutor)new talentCommand(this));
        getServer().getPluginManager().registerEvents(new talentCommand(this), this);
        this.getCommand("fin").setExecutor((CommandExecutor)new audienceCommand(this));
        getServer().getPluginManager().registerEvents(new audienceCommand(this), this);
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
}