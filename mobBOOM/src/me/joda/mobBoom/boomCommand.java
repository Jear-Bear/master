package me.joda.mobBoom;

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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;

public class boomCommand implements Listener, CommandExecutor {

	private Main main;
	boolean clickTable = false;
	boolean poop = false;
	Location craftLoc;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("mobBoom");
	public boomCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("boom"))
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
	public void shift(PlayerMoveEvent e)
	{
		if (main.on)
		{
			Player p = e.getPlayer();
			RayTraceResult rBlock = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), (double) main.distance, FluidCollisionMode.SOURCE_ONLY);
			RayTraceResult rEntity = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getEyeLocation().getDirection(), (double) main.distance, (entity) -> entity != p);
			if ((rEntity != null) || (rBlock != null))
			{
				Entity ent = rEntity.getHitEntity();
				Block b = rBlock.getHitBlock();
				if (rEntity != null)
				{
					ent.getWorld().createExplosion(ent.getLocation(), 100f);
				}
				if (rBlock != null)
				{
					b.getWorld().createExplosion(ent.getLocation(), 100f);
				}
			}
		}
	}
	

}
