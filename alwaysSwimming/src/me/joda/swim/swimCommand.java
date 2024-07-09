package me.joda.swim;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class swimCommand implements Listener {
	public boolean enabled = false;
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if (cmd.getName().toLowerCase().equals("swim"))
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 100));
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 100));
			}
		}
		return false;
	}
	
	@EventHandler
	public void noDrown(EntityDamageEvent e)
	{
		if (e.getCause() == DamageCause.DROWNING) e.setCancelled(true); 
	}
}
