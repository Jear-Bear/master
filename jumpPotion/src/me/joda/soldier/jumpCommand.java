package me.joda.soldier;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.netty.util.internal.ThreadLocalRandom;

public class jumpCommand implements Listener, CommandExecutor {

	private Main main;
	public jumpCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("jump"))
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
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if (main.on)
		{
			Location from = e.getFrom();
		    Location to = e.getTo();
		    Player player = e.getPlayer();
		    if (from.getBlockY() < to.getBlockY() && !player.isSwimming() && !player.isFlying() && !player.isOnGround())
		    {
		    	randomPotion(e.getPlayer());
		    }
		}
	}
	
	public void randomPotion(Player p)
	{
		int rnd = ThreadLocalRandom.current().nextInt(PotionEffectType.values().length);
		p.addPotionEffect(new PotionEffect(PotionEffectType.values()[rnd], 1200, 10000));
	}

}
