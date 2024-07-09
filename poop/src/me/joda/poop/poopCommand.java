package me.joda.poop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;

public class poopCommand implements Listener, CommandExecutor {

	private Main main;
	boolean clickTable = false;
	boolean poop = false;
	Location craftLoc;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("dookie");
	public poopCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("poop"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					if (args[0].equalsIgnoreCase("on"))
					{
						main.on = true;
						sender.sendMessage(ChatColor.RED + "Plugin enabled");
				        return false;
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void shift(PlayerToggleSneakEvent e)
	{
		if (main.on)
		{
			Player p = e.getPlayer();
			if (e.isSneaking())
			{
				ItemStack dookie = new ItemStack(Material.BROWN_DYE);
				ItemMeta itemStackMeta = dookie.getItemMeta();
				itemStackMeta.setDisplayName("Dookie");
				dookie.setItemMeta(itemStackMeta);
				Location l = p.getLocation();
				l.getWorld().dropItemNaturally(l, dookie);
			}
		}
	}
	
	@EventHandler
	public void pickupPoop(PlayerPickupItemEvent e)
	{
		ItemStack dookie = new ItemStack(Material.BROWN_DYE);
		ItemMeta itemStackMeta = dookie.getItemMeta();
		itemStackMeta.setDisplayName("Dookie");
		dookie.setItemMeta(itemStackMeta);
		if (e.getItem().getItemStack().getType() == Material.BROWN_DYE)
		{	
			this.poop = true;
			e.getPlayer().setHealth(0.0);
		}
	}
	
	@EventHandler
	public void deathByDookie(PlayerDeathEvent e)
	{
		if (poop && e.getEntity() instanceof Player)
		{
			e.setDeathMessage(ChatColor.WHITE + ((Player) e.getEntity()).getName() + " held their own dookie");
			this.poop = false;
		}
	}
	

}
