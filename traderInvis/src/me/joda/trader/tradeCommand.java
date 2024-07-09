package me.joda.trader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class tradeCommand implements Listener, CommandExecutor {

	private Main main;
	boolean clickTable = false;
	boolean poop = false;
	Location craftLoc;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("mobBoom");
	public tradeCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("trade"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off") && main.p.isOp())
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					if (args[0].equalsIgnoreCase("on") && main.p.isOp())
					{
						main.on = true;
						sender.sendMessage(ChatColor.GREEN + "Plugin enabled");
				        return false;
					}
					if (!main.p.isOp())
					{
						sender.sendMessage(ChatColor.RED + "Sorry, but you can't use this command!");
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void killTrader(EntityDeathEvent e)
	{
		ItemStack itemStack = new ItemStack(Material.POTION);
		PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
		potionMeta.setDisplayName("Potion of Invisibility");
		potionMeta.setColor(Color.GRAY);
		potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 60 * 3, 0), true);
		itemStack.setItemMeta(potionMeta);
		
		if (e.getEntityType() == EntityType.WANDERING_TRADER)
		{
			e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), itemStack);
		}
	}
	

}
