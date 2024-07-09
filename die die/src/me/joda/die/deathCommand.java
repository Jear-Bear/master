package me.joda.die;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class deathCommand implements Listener, CommandExecutor {

	private Main main;
	
	public deathCommand(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("death"))
		{
			if (args != null)
			{
				if (args[0].toString().toLowerCase().contains("on"))
				{
					sender.sendMessage("Plugin enabled!");
					main.on = true;
				}
				if (args[0].toString().toLowerCase().contains("off"))
				{
					sender.sendMessage("Plugin disabled!");
					main.on = false;
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void playerDeath(PlayerDeathEvent e)
	{
		if (main.on)
		{
			if (e.getEntity() instanceof Player)
			{
				Player joda = (Player) e.getEntity();
				joda.setHealth(0.0);
			}
		}
	}

}
