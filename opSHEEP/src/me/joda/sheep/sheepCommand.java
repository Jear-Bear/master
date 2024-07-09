package me.joda.sheep;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

public class sheepCommand implements Listener, CommandExecutor {

	private Main main;
	
	public sheepCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().contains("sheep"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().contains("on"))
				{
					main.makeList();
					main.pluginOn = true;
					sender.sendMessage("Plugin enabled!");
				}
				if (args[0].toLowerCase().contains("off"))
				{
					main.pluginOn = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void shearEvent(PlayerShearEntityEvent e)
	{
		if (e.getEntity().getType() == EntityType.SHEEP)
		{
			Random rand = new Random();
			ItemStack drop = main.OP.get(rand.nextInt(main.OP.size()));
			e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), drop);
		}
	}
	
	@EventHandler
	public void shearEvent(EntityDeathEvent e)
	{
		if (e.getEntity().getType() == EntityType.SHEEP)
		{
			e.setDroppedExp(250);
		}
	}

}
