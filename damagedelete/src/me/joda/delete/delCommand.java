package me.joda.delete;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class delCommand implements Listener, CommandExecutor {

	private Main main;
	
	public delCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("delete"))
		{
			if (args != null)
			{
				if (args[0].equalsIgnoreCase("on"))
				{
					main.on = true;
					sender.sendMessage("Plugin enabled!");
				}
				else if (args[0].equalsIgnoreCase("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}

	@EventHandler
	public void damage(EntityDamageEvent e)
	{
		if (main.on == true)
		{
			if (e.getEntity() instanceof Player)
			{
				Player p = (Player) e.getEntity();
				if (p.getInventory().isEmpty())
				{
					return;
				}
				int slot = (int) (Math.random() * (40));
				ItemStack itemStack = p.getInventory().getItem(slot);
				if (itemStack == null)
				{
					while (itemStack == null)
					{
						slot = (int) (Math.random() * (40));
						itemStack = p.getInventory().getItem(slot);
					}
				}
				if (itemStack != null)
				{
					ItemMeta meta = itemStack.getItemMeta();
					for (Player player : Bukkit.getOnlinePlayers())
					{
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
					}
					String name = WordUtils.capitalizeFully(itemStack.getType().toString().replace("_", " "));
					Bukkit.broadcastMessage(p.getDisplayName() + " has lost " + ChatColor.GOLD.toString() + ChatColor.BOLD + itemStack.getAmount() + " X " + name);
					p.getInventory().setItem(slot, new ItemStack(Material.AIR));
				}
			}
		}
	}
}
