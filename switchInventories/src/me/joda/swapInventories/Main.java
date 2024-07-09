package me.joda.swapInventories;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	Player p;
	ArrayList<Player> players = new ArrayList<>();
	swapCommand swapCommand = new swapCommand(this);
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new swapCommand(this), this);
		getCommand("swap").setExecutor(new swapCommand(this));
		getCommand("swap").setTabCompleter(new OnTabComplete());
	}
	
	private Main instance;
	public Main getInstance(){
	    return instance;
	}
	
	@EventHandler
    public void on (PlayerCommandSendEvent e)
    {
        e.getCommands().removeIf(command -> command.contains(":"));
    }
}
