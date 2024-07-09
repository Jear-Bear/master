package me.joda.cursed;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;

public class cursedCommand implements Listener, CommandExecutor {

	private Main main;
	int a;
	BossBar bar;
	private List<String> lore;
	
	public cursedCommand(Main main) {
		this.main = main;
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().contains("cursed"))
		{
			if (args != null)
			{
				if (args[0].equals("on"))
				{
					Player p = (Player) sender;
					p.setFoodLevel(8);
					p.setHealth(20.0);
					main.on = true;
					p.getInventory().clear();
					sender.sendMessage("Plugin enabled!");
				}
				else if (args[0].equals("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void look(PlayerMoveEvent e)
	{
		if (main.on)
		{
			if (getEntityInLineOfSightBlockIterator(e.getPlayer(), 5) instanceof Chicken)
			{
				Entity entity = getEntityInLineOfSightBlockIterator(e.getPlayer(), 5);
				e.getPlayer().getWorld().playSound(entity.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
				Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
		            @Override
		            public void run() {
		            	e.getPlayer().getWorld().createExplosion(entity.getLocation(), 3.0f);
		            }
		        }, 30L);
			}
		}
	}
	
	@EventHandler
	public void farm(BlockBreakEvent e)
	{
		if (main.on) 
		{
			if (e.getBlock().getType().toString().toLowerCase().contains("wheat"))
			{
				e.setDropItems(false);
				e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.NETHERITE_INGOT));
			}
			if (e.getBlock().getType().toString().toLowerCase().contains("log"))
			{
				e.setDropItems(false);
				e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.WOODEN_PICKAXE));
			}
			if (e.getBlock().getType().toString().toLowerCase().contains("stone"))
			{
				e.setDropItems(false);
				e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.STONE_PICKAXE));
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void cow(PlayerInteractEntityEvent e)
	{
		if (main.on) 
		{
			if (e.getRightClicked() instanceof Cow && e.getPlayer().getItemInHand().getType().equals(Material.BUCKET))
			{
				e.getPlayer().setItemInHand(new ItemStack(Material.WATER_BUCKET));
				e.getPlayer().getInventory().remove(new ItemStack(Material.MILK_BUCKET));
			}
		}
	}
	
	public Entity getEntityInLineOfSightBlockIterator(Player p, int range)
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
	
			if (b.getType().isSolid()) 
			{ 
				break;
			} 
			
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
						return target;
					}
				}
			}
		}
		return target;
	}
}
