package me.joda.pluginName;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class moneySpeedCommand extends moneyCommand implements Listener, CommandExecutor 
{
	private Main main;
	
	public moneySpeedCommand(Main main) {
		super(main);
		this.main = main;
	}
	
	public void setSpeed(double speed, Player p)
	{
		if (speed == 0)
		{
			p.sendMessage(ChatColor.RED + "Please enter a number other than zero!");
		}
		main.speed = speed;
	}

	

}
