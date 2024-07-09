package me.joda.glass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

public class glassCommand implements Listener, CommandExecutor {

	private Main main;
	public int time;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("glass");
	
	public glassCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("glass"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("on"))
					{
						main.on = true;
						sender.sendMessage(ChatColor.GREEN + "Plugin enabled");
					}
					else if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
					}
				}
			}
		}
		return false;
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void breakGlass(PlayerMoveEvent e)
	{
		Location from = e.getFrom();
		Location to = e.getTo();
		Block block = e.getPlayer().getTargetBlock(null, 200);
		Location player = e.getPlayer().getLocation();
		if (block.getType().toString().toLowerCase().contains("glass") || block.getType().toString().toLowerCase().contains("fence"))
		{
			Location blockLocation = block.getLocation();
			if (e.getPlayer().getFacing().toString() == "WEST" || e.getPlayer().getFacing().toString() == "NORTH")
			{
				player.subtract(1, 0, 1);
			}
			if ((from.distance(blockLocation) > to.distance(blockLocation)))
			{
				int yLevel = block.getY();
				int playerYLevel = e.getPlayer().getLocation().getBlockY();
				if (yLevel == playerYLevel)
				{
					if (player.distance(block.getLocation()) < 2.0)
					{
						if (block.getType().toString().toLowerCase().contains("glass") || block.getType().toString().toLowerCase().contains("fence"))
						{
							blockLocation.getBlock().setType(Material.AIR);
							block.setType(Material.AIR);
							if (block.getType().toString().toLowerCase().contains("glass")) e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
							if (block.getType().toString().toLowerCase().contains("fence")) e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_STONE_BREAK, 1f, 1f);
							e.getPlayer().getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5,0.5,0.5), 1, 1, 0.1, 0.1, 0.1, block);
							for (Entity entity : e.getPlayer().getNearbyEntities(20, 20, 20))
							{
								if (entity instanceof Player)
								{
									Player p = (Player) entity;
									p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
								}
							}
						}
					}
				}
				else if (yLevel == playerYLevel + 1)
				{
					if (player.distance(block.getLocation()) < 2.0)
					{
						if (block.getType().toString().toLowerCase().contains("glass") || block.getType().toString().toLowerCase().contains("fence"))
						{
							blockLocation.getBlock().setType(Material.AIR);
							block.setType(Material.AIR);
							if (block.getType().toString().toLowerCase().contains("glass")) e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
							if (block.getType().toString().toLowerCase().contains("fence")) e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_STONE_BREAK, 1f, 1f);
							e.getPlayer().getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5,0.5,0.5), 1, 1, 0.1, 0.1, 0.1, block);
							for (Entity entity : e.getPlayer().getNearbyEntities(20, 20, 20))
							{
								if (entity instanceof Player)
								{
									Player p = (Player) entity;
									p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
								}
							}
						}
					}
				}
				else
				{
					return;
				}
			}
		}
	}
}
