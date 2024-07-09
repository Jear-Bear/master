package me.joda.jumpInventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.util.Vector;

public class invCommand implements Listener, CommandExecutor {

	private Main main;
	public Vector launch = new Vector(0, 500, 0);
	public invCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("inv"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().equals("on"))
				{
					main.on = true;
					sender.sendMessage("Plugin enabled!");
				}
				if (args[0].toLowerCase().equals("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void invOpen(InventoryClickEvent e)
	{
		if (main.on)
		{
			e.getWhoClicked().setVelocity(launch);
		}
	}
}
