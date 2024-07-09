package me.joda.soldier;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class soldierCommand implements Listener, CommandExecutor {

	private Main main;
	public soldierCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("super"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("on"))
					{
						main.on = true;
						ItemStack helmet = new ItemStack(Material.TURTLE_HELMET);
						ItemMeta helmetMeta = helmet.getItemMeta();
						helmetMeta.addEnchant(Enchantment.WATER_WORKER, 0, false);
						helmet.setItemMeta(helmetMeta);
						Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Plugin enabled!");
						for (Player p : Bukkit.getOnlinePlayers()) 
						{
							p.setWalkSpeed(.275f);
							p.getInventory().setHelmet(helmet);
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
						}
					}
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						Bukkit.broadcastMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Plugin disabled!");
						for (Player p : Bukkit.getOnlinePlayers()) 
						{
							p.setWalkSpeed(.2f);
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, .5f);
						}
						
					}
				}
			}
		}
		return false;
	}
	
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) 
	{
		if (e.getEntity() instanceof Player)
		{
			e.setDamage(e.getDamage() / 2);
		}
	}
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent e)
	{
	  if(e.getDamager() instanceof Player)
	    {
	      e.setDamage(e.getDamage() + 3.5);
	    }
	}
	
	@EventHandler
	public void look(PlayerMoveEvent e)
	{
		if (main.on) 
		{
			e.getPlayer().setFoodLevel(20);
			String tool = e.getPlayer().getItemInHand().getType().toString().toLowerCase();
			if (tool.contains("pick") || tool.contains("hoe") || (tool.contains("axe") && !tool.contains("pick")) || tool.contains("shovel"))
			{
				e.getPlayer().getItemInHand().addEnchantment(Enchantment.DIG_SPEED, 3);
			}
			
			if (e.getPlayer().getLocation().getBlock().getType() == Material.WATER)
			{
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, false), false);
			}
			else if (e.getPlayer().getLocation().getBlock().getType() != Material.WATER)
			{
				for (PotionEffect effect : e.getPlayer().getActivePotionEffects())
				{
					e.getPlayer().removePotionEffect(effect.getType());
				}
			}
		}
	}
	
	@EventHandler
	public void breathe(EntityPotionEffectEvent e)
	{
		if (e.getCause() == Cause.TURTLE_HELMET)
		{
			e.setCancelled(true);
			Player p = (Player) e.getEntity();
			p.removePotionEffect(PotionEffectType.WATER_BREATHING);
		}
	}

}
