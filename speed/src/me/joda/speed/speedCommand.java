package me.joda.speed;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class speedCommand implements Listener, CommandExecutor {

	private Main main;
	
	public speedCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("speed"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().contains("on"))
				{
					for (Player p : Bukkit.getOnlinePlayers())
					{
						p.setWalkSpeed(.2f);
					}
					main.on = true;
					sender.sendMessage("Plugin enabled!");
				}
				if (args[0].toLowerCase().contains("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void breakTrack(BlockBreakEvent e)
	{
		e.getPlayer().setWalkSpeed(e.getPlayer().getWalkSpeed() + .001f);
	}

}
