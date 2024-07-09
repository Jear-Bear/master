package me.joda.angryPigs;

import java.util.Collection;
import java.util.List;

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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Piglin;

public class pigsCommand implements CommandExecutor, Listener {

	private Main main;
	
	public pigsCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("pigs"))
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
		return false;
	}
	
	@EventHandler
	public void angryPigs(PlayerMoveEvent e)
	{
		if (main.on) 
		{
			Player player = e.getPlayer();
			for (Entity entity : near(player, 50))
			{
				if (entity.getType() == EntityType.PIGLIN_BRUTE || entity.getType() == EntityType.PIGLIN || entity.getType() == EntityType.ZOMBIFIED_PIGLIN)
				{
					if (entity.getType() == EntityType.ZOMBIFIED_PIGLIN)
					{
						PigZombie zPiglin = (PigZombie) entity;
						zPiglin.setAngry(true);
						Mob mob = (Mob) zPiglin;
						if (mob.getTarget() == null) mob.setTarget((LivingEntity) e.getPlayer());
					}
				}
			}
		}
	}
	
	public List<Entity> near(Player p, double radius) {
		return p.getNearbyEntities(radius, radius, radius);
	}
	
	@EventHandler
	public void breakSpawner(BlockBreakEvent e)
	{
		if (e.getBlock().getType().toString().toLowerCase().contains("spawner"))
		{
			e.setDropItems(false);
			e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.BLAZE_ROD, 5));
			e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.ENDER_PEARL, 4));
		}
	}

}
