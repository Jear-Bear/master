package me.joda.timer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	boolean timerRunning = true;
	Player p;
	ChatColor color = ChatColor.GREEN;
	double speed = .1;
	String currency = "$";
	Player runner;
	boolean isStill = false;
	double startingMoney = -1.00;
	moneyCommand moneyCommand = new moneyCommand(this);
	moneyTimerCommand moneyTimer = new moneyTimerCommand(this);
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new moneyCommand(this), this);
		getServer().getPluginManager().registerEvents(new moneyTimerCommand(this), this);
		getCommand("timer").setExecutor(new moneyCommand(this));
		getCommand("timer").setTabCompleter(new OnTabComplete());
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
