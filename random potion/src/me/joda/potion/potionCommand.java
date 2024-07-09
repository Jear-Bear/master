package me.joda.potion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.netty.util.internal.ThreadLocalRandom;

public class potionCommand implements Listener, CommandExecutor {

	private Main main;
	public potionCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("random"))
		{
			if (args == null)
			{
				sender.sendMessage("Enter on/off after the command!");
				return false;
			}
			else if (args != null) 
			{
				if (args[0].toString().toLowerCase().contains("off"))
				{
					main.setPluginStatus(false);
					sender.sendMessage("Plugin disabled!");
				}
				if (args[0].toString().toLowerCase().contains("on")) 
				{
					main.setPluginStatus(true);
					sender.sendMessage("Plugin enabled!");
				}
			}
		}
		return false;
	}
	
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(!(e.getEntity() instanceof Player))
        {     
            LivingEntity ent = e.getEntity();
            Object mcPlayer = ((LivingEntity) ent).getKiller();
            if(mcPlayer == null) return;
            else
            {
            	if (main.getPluginStatus()) randomPotion((Player) mcPlayer);
            	else if (!main.getPluginStatus()) return;
            }
        }
    }
	
	public void randomPotion(Player p)
	{
		/*
		int rnd = ThreadLocalRandom.current().nextInt(PotionEffectType.values().length);
		p.addPotionEffect(new PotionEffect(PotionEffectType.values()[rnd], 1200, 1));
		*/
		p.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1200, 0));
	}

}
