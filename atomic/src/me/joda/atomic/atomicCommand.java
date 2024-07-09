package me.joda.atomic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class atomicCommand implements CommandExecutor, Listener {

	private Main main;
	
	public atomicCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("atomic"))
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
	public void onPlayerDamage(EntityDamageEvent e) 
	{
		if (main.on) {
			if (e.getEntity() instanceof Player)
			{
				Player p = (Player) e.getEntity();
				p.getWorld().createExplosion(p.getLocation(), 50, true, true, p);
				p.setHealth(0.0);
			}
		}
	}

}
