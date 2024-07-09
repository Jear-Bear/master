package me.joda.jump;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public class jumpCommand implements Listener, CommandExecutor {

	private Main main;
	public jumpCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().equals("jump"))
		{
			if (args != null)
			{
				if (args[0].toString().toLowerCase().contains("on"))
				{
					for (Player p : Bukkit.getOnlinePlayers())
					{
						p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(64);
						p.setHealth(64.0);
					}
					main.setPluginStatus(true);
					sender.sendMessage("Plugin enabled!");
				}
				else if (args[0].toString().toLowerCase().contains("off"))
				{
					for (Player p : Bukkit.getOnlinePlayers()) p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
					main.setPluginStatus(false);
					sender.sendMessage("Plugin disabled!");
				}
				else sender.sendMessage("Invalid command!");
			}
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if (main.getPluginStatus())
		{
			Location from = e.getFrom();
		    Location to = e.getTo();
		    Player player = e.getPlayer();
		    if (from.getBlockY() < to.getBlockY() && !player.isSwimming() && !player.isFlying() && !player.isOnGround())
		    {
		    	if ((int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() <= 1)
		    	{
		    		main.jumpedTooMuch = true;
		    		player.setHealth(0);
		    		return;
		    	}
		    	int health = (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() / 2;
		    	player.damage(health);
		    	player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		    	int dispHealth = health / 2;
		    	for (Player p : Bukkit.getOnlinePlayers())
		    	{
		    		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
		    	}
		    	if (dispHealth > 1) Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + player.getDisplayName() + ChatColor.WHITE + " now has " + ChatColor.RED.toString() + ChatColor.BOLD + dispHealth + ChatColor.WHITE + " hearts");
		    	else if (dispHealth == 1) Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + player.getDisplayName() + ChatColor.WHITE + " now has " + ChatColor.RED.toString() + ChatColor.BOLD + "1" + ChatColor.WHITE + " heart");
		    	else if (dispHealth == 0) Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + player.getDisplayName() + ChatColor.WHITE + " now has " + ChatColor.RED.toString() + ChatColor.BOLD + "HALF A HEART");
		    }
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			for(Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1f, 1f);
			Player p = (Player) e.getEntity();
			if (main.jumpedTooMuch) e.setDeathMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + p.getDisplayName() + ChatColor.WHITE + " jumped too much");
			main.jumpedTooMuch = false;
		}
		
	}

}
