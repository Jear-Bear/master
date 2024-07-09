package me.joda.kb;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class kbCommand implements CommandExecutor, Listener {

	private Main main;
	
	public kbCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String labbel, String[] args) {
		if (cmd.getName().toLowerCase().equals("kb"))
		{
			if (args != null)
			{
				if (args[0].toString().toLowerCase().contains("on"))
				{
					for (Player p : Bukkit.getOnlinePlayers())
					{
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
					}
					main.on = true;
					sender.sendMessage("Plugin enabled!");
				}
				else if (args[0].toString().toLowerCase().contains("off"))
				{
					for (Player p : Bukkit.getOnlinePlayers())
					{
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, .5f);
					}
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
				else sender.sendMessage("Invalid command!");
			}
		}
		return false;
	}
	
	@EventHandler
	public void Launch(EntityDamageByEntityEvent e)
	{
		if (main.on)
		{
			if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && e.getDamager() != e.getEntity())
			{
				
				Player damager = (Player) e.getDamager();
				Entity victim = e.getEntity();
				// Assume you have these
				Location dam = damager.getEyeLocation();
				Location targ = victim.getLocation();

				// If you want direction, just subtract the two!
				Vector direction = dam.toVector().subtract(targ.toVector());
				direction.multiply(5);
				
				damager.setVelocity(direction);
			}
		}
	}
	
	@EventHandler
	public void look(PlayerMoveEvent e)
	{
		if (main.on) 
		{
			rayCast(e.getPlayer(), 64);
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
		if (target instanceof Mob)
		{
			// Assume you have these
			Location dam = p.getEyeLocation();
			Location targ = target.getLocation();

			// If you want direction, just subtract the two!
			Vector direction = dam.toVector().subtract(targ.toVector());
			direction.multiply(5);
			
			p.setVelocity(direction);
		}
	}
}
