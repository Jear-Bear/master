package me.joda.moneyTimer;

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
	boolean timerRunning = true;
	Player p;
	ChatColor color = ChatColor.GREEN;
	double speed = 0;
	String currency = "$";
	double startingMoney = -1.00;
	moneyCommand moneyCommand = new moneyCommand(this);
	moneySpeedCommand moneySpeed = new moneySpeedCommand(this);
	moneyTimerCommand moneyTimer = new moneyTimerCommand(this);
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new moneyCommand(this), this);
		getServer().getPluginManager().registerEvents(new moneyTimerCommand(this), this);
		getCommand("money").setExecutor(new moneyCommand(this));
		getCommand("money").setTabCompleter(new OnTabComplete());
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
