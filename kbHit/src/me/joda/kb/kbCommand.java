package me.joda.kb;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
		Bukkit.broadcastMessage("SHEESH");
		if (main.on)
		{
			if (e.getDamager() instanceof Player)
			{
				
				Player damager = (Player) e.getDamager();
				Entity victim = e.getEntity();
				// Assume you have these
				Location dam = damager.getEyeLocation();
				Location targ = victim.getLocation();

				// If you want direction, just subtract the two!
				Vector direction = dam.toVector().subtract(targ.toVector());
				
				damager.setVelocity(direction.normalize().multiply(2));
			}
		}
	}
}
