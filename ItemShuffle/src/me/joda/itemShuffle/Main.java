package me.joda.itemShuffle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	boolean dragonIsAlive = true;
	Player p;
	ChatColor color = ChatColor.GREEN;
	shuffleCommand moneyCommand = new shuffleCommand(this);
	shuffleTimerCommand moneyTimer = new shuffleTimerCommand(this);
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new shuffleCommand(this), this);
		getServer().getPluginManager().registerEvents(new shuffleTimerCommand(this), this);
		getCommand("shuffle").setExecutor(new shuffleCommand(this));
		getCommand("shuffle").setTabCompleter(new OnTabComplete());
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
