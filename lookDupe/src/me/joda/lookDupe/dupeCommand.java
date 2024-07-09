package me.joda.lookDupe;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

public class dupeCommand implements CommandExecutor, Listener {

	public Main main;
	
	public dupeCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("look"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("on"))
					{
						main.on = true;
						Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Plugin enabled!");
						for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
					}
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						Bukkit.broadcastMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Plugin disabled!");
						for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, .5f);
						
					}
				}
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Sorry, but only players can use this command!");
		}
		return false;
	}
	
	@EventHandler
	public void lookAtItem(PlayerMoveEvent e)
	{
		if (main.on) {
			rayCast(e.getPlayer(), 10);
		}
	}
	
	public void rayCast(Player p, int range)
	{
		List<Entity> targetList = p.getNearbyEntities(range, range, range);
		BlockIterator bi = new BlockIterator(p, range);
		Entity target = null;
		while(bi.hasNext())
		{
			Block b = bi.next();
			int bx = b.getX();
			int by = b.getY();
			int bz = b.getZ();
			if (b.getType().isSolid()) break;
			else
			{
				for(Entity e : targetList) 
				{
					Location l = e.getLocation();
					double ex = l.getX();
					double ey = l.getY();
					double ez = l.getZ();
					if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) 
					{
						target = e;
					}
				}
			}
		}
		if (target.getType() == EntityType.DROPPED_ITEM)
		{
			ItemStack stack = ((Item) target).getItemStack();
			int amount = stack.getAmount() * 2;
			int stacks = (amount - (amount%64)) / 64;
			int remainder = amount % 64;
			if (stacks <= 1) 
			{
				ItemStack newStack = new ItemStack(stack.getType(), amount);
				target.getWorld().dropItemNaturally(target.getLocation(), newStack);
				target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, .5f, 1f);
			}
			else if (stacks > 1)
			{
				ItemStack newStack = new ItemStack(stack.getType(), remainder);
				target.getWorld().dropItemNaturally(target.getLocation(), newStack);
				target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, .5f, 1f);
				for (int i = 0; i < stacks; i++)
				{
					newStack = new ItemStack(stack.getType(), 64);
					target.getWorld().dropItemNaturally(target.getLocation(), newStack);
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, .5f, 1f);
				}
			}
		}
	}

}
