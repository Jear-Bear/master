package me.joda.cursed;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CursedCommand implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		event.setJoinMessage("");
		Player p = event.getPlayer();
		p.getLocation().getWorld().setTime(1000);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setLevel(0);
		p.setExp(0);
	}
	
	@EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e)
	{
		Player player = e.getPlayer();
		ItemStack potion = e.getItem();
		if (potion.getType() == Material.POTION) {
			PotionMeta meta = (PotionMeta) potion.getItemMeta();
			if (meta.getDisplayName().equals("Steroids"))
			{
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*120, 1000000000));
			}
		}
    }    
	
	/*
	@EventHandler
    public void tptest(PlayerChangedWorldEvent event){
        if(event.getPlayer().getLocation().getWorld().getName().contains("end")){
            Player p = event.getPlayer();
            ItemStack potion = new ItemStack(Material.POTION);
			PotionMeta meta = (PotionMeta) potion.getItemMeta();
			meta.setColor(Color.WHITE);
			meta.setDisplayName("Steroids");
			potion.setItemMeta(meta);
            p.getInventory().addItem(potion);
        }
    }
	*/
	
	@EventHandler
	public void mobDeath(EntityDeathEvent event)
	{
		Entity ent = event.getEntity();
		if (ent instanceof Pig)
		{
			event.getDrops().clear();
			event.setDroppedExp(0);
			ent.getLocation().getWorld().dropItem(ent.getLocation(), new ItemStack(Material.ENDER_PEARL));
		}
		
		else if (ent instanceof Piglin)
		{
			event.getDrops().clear();
			event.setDroppedExp(0);
			ent.getLocation().getWorld().dropItem(ent.getLocation(), new ItemStack(Material.BLAZE_ROD));
			ent.getLocation().getWorld().dropItem(ent.getLocation(), new ItemStack(Material.COOKED_PORKCHOP));
		}
		
		else if (ent instanceof PiglinBrute)
		{
			event.getDrops().clear();
			event.setDroppedExp(0);
			ent.getLocation().getWorld().dropItem(ent.getLocation(), new ItemStack(Material.BLAZE_ROD, 3));
			ent.getLocation().getWorld().dropItem(ent.getLocation(), new ItemStack(Material.COOKED_PORKCHOP));
		}
		
		else if (ent instanceof IronGolem)
		{
			event.getDrops().clear();
			event.setDroppedExp(0);
			ent.getLocation().getWorld().dropItem(ent.getLocation(), new ItemStack(Material.IRON_INGOT, 4));
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Block b = event.getBlock();
		
		if (b.getType() == Material.GRASS)
		{
			event.setDropItems(false);
            b.setType(Material.AIR);
            event.setDropItems(true);
			b.getLocation().getWorld().dropItem(b.getLocation(), new ItemStack(Material.BREAD));
		}
		
		else if (b.getType() == Material.NETHER_BRICKS)
		{
			event.setDropItems(false);
            b.setType(Material.AIR);
            event.setDropItems(true);
			b.getLocation().getWorld().dropItem(b.getLocation(), new ItemStack(Material.BLAZE_ROD));
			b.getLocation().getWorld().dropItem(b.getLocation(), new ItemStack(Material.ENDER_PEARL));
		}
		
		else if (b.getType() == Material.OBSIDIAN)
		{
			event.setDropItems(false);
            b.setType(Material.AIR);
            event.setDropItems(true);
			b.getLocation().getWorld().dropItem(b.getLocation(), new ItemStack(Material.OBSIDIAN, 2));
		}
		
		
	}
}
