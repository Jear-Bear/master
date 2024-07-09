package me.joda.shift;

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
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;

public class shiftCommand implements Listener, CommandExecutor {

	private Main main;
	boolean clickTable = false;
	Location craftLoc;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("shift");
	public shiftCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("shift"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					else if (isInt(args[0]))
					{
						main.on = true;
						main.distance = Integer.parseInt(args[0]);
					}
					else if (!isInt(args[0]))
					{
						sender.sendMessage(ChatColor.RED + "Enter max distance!\n" + ChatColor.GOLD.toString() + ChatColor.BOLD + "Usage: " + ChatColor.GRAY + " (/shift 9)");
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void shift(PlayerToggleSneakEvent e)
	{
		Player p = e.getPlayer();
		if (e.isSneaking())
		{
			RayTraceResult rBlock = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), (double) main.distance, FluidCollisionMode.NEVER);
			RayTraceResult rEntity = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getEyeLocation().getDirection(), (double) main.distance, (entity) -> entity != p);
			if ((rEntity != null) && (rBlock != null))
			{
				Entity ent = rEntity.getHitEntity();
				Block b = rBlock.getHitBlock();
				if (ent.getType() == EntityType.DROPPED_ITEM)
				{
					if (p.getLocation().distance(ent.getLocation()) < p.getLocation().distance(b.getLocation()))
					{
						ItemStack i = ((Item) ent).getItemStack();
						p.getInventory().addItem(i);
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
						ent.remove();
						return;
					}
					else if (p.getLocation().distance(ent.getLocation()) > p.getLocation().distance(b.getLocation()))
					{
						p.getInventory().addItem(new ItemStack(b.getType(), 1));
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
						ent.remove();
						return;
					}
				}
				else if (p.getLocation().distance(ent.getLocation()) == p.getLocation().distance(b.getLocation()))
				{
					ItemStack i = ((Item) ent).getItemStack();
					p.getInventory().addItem(i);
					p.getInventory().addItem(new ItemStack(b.getType(), 1));
					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
					ent.remove();
					return;	
				}
			}
			else if (rEntity != null)
			{
				ItemStack i = ((Item) rEntity.getHitEntity()).getItemStack();
				p.getInventory().addItem(i);
				p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
				rEntity.getHitEntity().remove();
				return;
			}
			else
			{
				p.getInventory().addItem(new ItemStack(rBlock.getHitBlock().getType(), 1));
				p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
				rBlock.getHitBlock().setType(Material.AIR);
				return;
			}
		}
	}
	
	public static boolean isInt(String str) {
	  	try {
	      	@SuppressWarnings("unused")
	    	int x = Integer.parseInt(str);
	      	return true;
		} catch (NumberFormatException e) {
	    	return false;
		}
	}

}
