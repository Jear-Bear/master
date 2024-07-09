package me.joda.vixManhunt;

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
	double speed = 0;
	double startingMoney = -1.00;
	vixCommand vixCommand = new vixCommand(this);
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new vixCommand(this), this);
		getCommand("vix").setExecutor(new vixCommand(this));
		getCommand("vix").setTabCompleter(new OnTabComplete());
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
