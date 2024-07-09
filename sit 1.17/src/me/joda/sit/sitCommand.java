package me.joda.sit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class sitCommand implements Listener, CommandExecutor {

	private Main main;
	
	public sitCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().contains("sit"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().contains("on"))
				{
					Player p = (Player) sender;
					p.getInventory().clear();
					main.on = true;
					sender.sendMessage("Go sit on any mob. LITERALLY ANY MOB");
				}
				if (args[0].toLowerCase().contains("off"))
				{
					main.on = false;
					sender.sendMessage("No more sitting!");
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void sitOnMob(PlayerInteractEntityEvent e)
	{
		if (main.on) 
		{
			if (e.getRightClicked().getPassenger() != null)
			{
				List<Entity> passengers = e.getRightClicked().getPassengers();
				passengers.get(passengers.size()).setPassenger(e.getPlayer());
			}
			else e.getRightClicked().setPassenger(e.getPlayer());
		}
	}

}