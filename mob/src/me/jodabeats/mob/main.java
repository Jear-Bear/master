package me.jodabeats.mob;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.entity.Firework;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class main extends JavaPlugin implements Listener
{
	public boolean inGame = false;
	public boolean death = false;
	Inventory shared;
	Entity target;
	public FileConfiguration config;
	public ArrayList<Player> players = new ArrayList<>();
    public void onEnable() {
    	if (inGame == true) inGame = false;
    	if (inGame == false) inGame = true;
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("mob").setExecutor((CommandExecutor)new CommandExe(this));
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
    
    public void start(Player p)
    {
    	if (inGame == true)
    	{	
    		if (players != null) players.clear();
    		Bukkit.broadcastMessage("it has started!");
        	if (shared != null) shared.clear();
        	for (Player player : Bukkit.getOnlinePlayers())
    		{
    			players.add(player);
    		}
        	if (death)
        	{
        		for (Player player : Bukkit.getOnlinePlayers())
        		{
        			player.setHealth(0.0);
        		}
        		death = false;
        	}
    	}
    	else Bukkit.broadcastMessage("turned off!");
    }
    
    @EventHandler
    public <T> void look(PlayerMoveEvent e)
    {
    	Player p = e.getPlayer();
    	Block a = p.getTargetBlock(null, 256);
    	Location l = a.getLocation();
    	Entity lookAt = getNearestEntityInSight(p, 50);
    	if (lookAt != null)
    	{
    		if (lookAt instanceof LivingEntity)
    		{
    			if (l.distance(lookAt.getLocation()) < 2.0)
        		{
        			Vector v;
        			if (lookAt instanceof Player)
        			{
        				v = new Vector(lookAt.getVelocity().getX(), 40, lookAt.getVelocity().getZ());
        			}
        			else  v = new Vector(lookAt.getVelocity().getX(), 4, lookAt.getVelocity().getZ());
        			lookAt.getWorld().playSound(lookAt.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.0f);
        			lookAt.getWorld().playEffect(lookAt.getLocation(), Effect.SMOKE, 40);
        			 lookAt.setVelocity(v);
        		}
    		}	
    	}
    }
    
    public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( (Set<Material>) null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (int i = 0;i<sightBlock.size();i++)
            sight.add(sightBlock.get(i).getLocation());
        for (int i = 0;i<sight.size();i++) {
            for (int k = 0;k<entities.size();k++) {
                if (Math.abs(entities.get(k).getLocation().getX()-sight.get(i).getX())<1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY()-sight.get(i).getY())<1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ()-sight.get(i).getZ())<1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null; //Return null/nothing if no entity was found
    }
    
    @EventHandler
    public void damageFromMob(EntityDamageEvent e)
    {
    	if (e.getCause() == DamageCause.BLOCK_EXPLOSION || e.getCause() == DamageCause.ENTITY_EXPLOSION)
    	{
    		e.setCancelled(true);
    		e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY(), e.getEntity().getLocation().getZ(), 2f);
    	}
    }
}