package me.joda.egg;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class eggCommand implements CommandExecutor, Listener {

	private Main main;
	
	public eggCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().equals("egg"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().equals("on"))
				{
					main.on = true;
					sender.sendMessage("egg sock moment");
				}
				else if (args[0].toLowerCase().equals("off"))
				{
					main.on = false;
					sender.sendMessage("no more eggg soc :(");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void craft(PlayerMoveEvent e)
	{
		if (e.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_BOOTS))
		{
			ItemMeta meta = e.getPlayer().getItemInHand().getItemMeta();
			meta.setDisplayName("egg socks");
			e.getPlayer().getItemInHand().setItemMeta(meta);
		}
	}

}
