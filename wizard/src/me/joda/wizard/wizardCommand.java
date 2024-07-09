package me.joda.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class wizardCommand implements CommandExecutor, Listener {
	public boolean enabled = false;
	private Main main;
	ItemStack stick = new ItemStack(Material.STICK);
	ItemMeta stickMeta = stick.getItemMeta();
	Set<Material> mats = new HashSet<Material>();
	ArrayList<ItemStack> wands = new ArrayList<>();
	
	public wizardCommand(Main main)
	{
		mats.addAll(Arrays.asList(new Material[] {Material.AIR, Material.WATER, Material.CAVE_AIR}));
		this.main = main;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if (cmd.getName().toLowerCase().equals("magic"))
		{
			Bukkit.broadcastMessage("You're a wizard Harry");
			for (Player p : Bukkit.getOnlinePlayers())
			{
				giveWands(p);
			}
		}
		return false;
	}
	
	@EventHandler
	public void stick(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.BLUE.toString() + ChatColor.BOLD + "Explodus"))
		{
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Location l = e.getPlayer().getTargetBlock((Set<Material>) null, 100).getLocation();
				e.getPlayer().getWorld().strikeLightningEffect(l);
				Random r = new Random();
				float random = 2f + r.nextFloat() * (12.5f - 2f);
				e.getPlayer().getWorld().createExplosion(l, random);
				
			}
		}
		if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA.toString() + ChatColor.BOLD + "Squirtus"))
		{
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Location l = e.getPlayer().getTargetBlock((Set<Material>) null, 100).getLocation();
				l = l.add(0, 1, 0);
				l.getBlock().setType(Material.WATER);
			}
		}
		if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD.toString() + ChatColor.BOLD + "Burnus"))
		{
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				
				Projectile fireball = player.launchProjectile(Fireball.class);
				fireball.setVelocity(fireball.getVelocity().multiply(5));
			}
		}
		if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Dragonballus"))
		{
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				DragonFireball fireball = player.launchProjectile(DragonFireball.class);
				fireball.setVelocity(fireball.getVelocity().multiply(5));
			}
		}
		if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Broomstick"))
		{
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Vector v = player.getLocation().getDirection();
				v.multiply(2);
				player.setVelocity(v);
			}
		}
		if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED.toString() + ChatColor.BOLD + "Switchus fishus"))
		{
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Block b = player.getTargetBlock(mats, 100);
				for (Entity ent : player.getNearbyEntities(100.0, 100.0, 100.0))
				{
					if (b.getLocation().distance(ent.getLocation()) <= 4)
					{
						if (ent.getCustomName() != null)
						{
							for (Player p : Bukkit.getOnlinePlayers())
							{
								if (ent.getCustomName().equals(p.getDisplayName()))
								{
									p.setGameMode(GameMode.SURVIVAL);
									p.teleport(ent.getLocation());
									ent.teleport(ent.getLocation().subtract(0, 200, 0));
									return;
								}
							}
						}
						if (!(ent.getType() == EntityType.SALMON) && !(ent.getType() == EntityType.COD))
						{
							Location l = ent.getLocation();
							if (!(ent instanceof Player)) ent.teleport(ent.getLocation().subtract(0, 200, 0));
							if (ent.getCustomName() != null) return;
							if (ent instanceof Player)
							{
								Player play = (Player) ent;
								if (play.getGameMode() == GameMode.SURVIVAL)
								{
									Entity salmon = player.getWorld().spawnEntity(l, EntityType.SALMON);
									Player p = (Player) ent;
									p.setGameMode(GameMode.SPECTATOR);
									p.setSpectatorTarget(salmon);
									salmon.setCustomName(p.getDisplayName());
									salmon.setCustomNameVisible(true);
								}
							}
							else if (!(ent instanceof Player))
							{
								player.getWorld().spawnEntity(l, EntityType.SALMON);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler   
    public void hitFireball(ProjectileHitEvent e) 
	{
        EntityType fball = e.getEntityType();
        if( fball != null && fball.equals(EntityType.FIREBALL)) 
        {
            Fireball f = (Fireball) e.getEntity();
            Location location = f.getLocation();
            f.getWorld().createExplosion(location, 3.25f);        
        }
    }
	
	@EventHandler   
    public void fishDeath(EntityDeathEvent e) 
	{
        if (e.getEntity().getCustomName() != null)
        {
        	for (Player p : Bukkit.getOnlinePlayers())
        	{
        		if (e.getEntity().getCustomName().equals(p.getDisplayName()))
				{
        			p.getInventory().clear();
        			p.setHealth(0.0);
        			main.fished = true;
				}
        	}
        }
    }
	
	@EventHandler   
    public void playerDeath(PlayerDeathEvent e) 
	{
        if (main.fished && e.getEntity() instanceof Player)
        {
        	Player p = (Player) e.getEntity();
        	e.setDeathMessage(ChatColor.AQUA + p.getDisplayName() + " was turned into a fish");
        	main.fished = false;
        }
    }
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e)
	{
		e.getPlayer().setGameMode(GameMode.SURVIVAL);
		giveWands(e.getPlayer());
	}
	
	@EventHandler
    public void onPlayerAttack(EntityDamageEvent event)
    {
    	if(event.getEntity() instanceof Player)
    	{
    		if (event.getCause() == DamageCause.BLOCK_EXPLOSION)
    		{
    			event.setCancelled(true);
    		}
    	}
    }
	
	public void giveWands(Player p)
	{
		stickMeta.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD + "Explodus");
		stickMeta.addEnchant(Enchantment.KNOCKBACK, 4, true);
		stick.setItemMeta(stickMeta);
		p.getInventory().addItem(stick);
		
		stickMeta.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Squirtus");
		stickMeta.addEnchant(Enchantment.KNOCKBACK, 4, true);
		stick.setItemMeta(stickMeta);
		p.getInventory().addItem(stick);
		
		stickMeta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Burnus");
		stickMeta.addEnchant(Enchantment.KNOCKBACK, 4, true);
		stick.setItemMeta(stickMeta);
		p.getInventory().addItem(stick);
		
		stickMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Dragonballus");
		stickMeta.addEnchant(Enchantment.KNOCKBACK, 4, true);
		stick.setItemMeta(stickMeta);
		p.getInventory().addItem(stick);
		
		stickMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Broomstick");
		stickMeta.addEnchant(Enchantment.KNOCKBACK, 4, true);
		stick.setItemMeta(stickMeta);
		p.getInventory().addItem(stick);
		
		stickMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Switchus fishus");
		stickMeta.addEnchant(Enchantment.KNOCKBACK, 4, true);
		stick.setItemMeta(stickMeta);
		p.getInventory().addItem(stick);
	}
}
