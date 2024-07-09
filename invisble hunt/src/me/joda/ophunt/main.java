package me.joda.ophunt;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.boss.BossBar;


import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class main extends JavaPlugin implements Listener
{
	public main plugin;
	public boolean inGame = false;
    public FileConfiguration config;
    BossBar bar;
    int lvl = 1;
    Location portal = null;
    Location hunterSpawn;
    public ArrayList<Player> target = new ArrayList<>();
    public ArrayList<Player> hunter = new ArrayList<>();
    public ArrayList<Location> portals = new ArrayList<>();
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        getServer().getPluginManager().registerEvents(new CommandExe(this), this);
        this.getCommand("lag").setExecutor((CommandExecutor)new CommandExe(this));
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
    
    public void start(Player p)
    {
    	hunterSpawn = p.getLocation();
    	if (target != null) target.clear();
    	if (hunter != null) hunter.clear();
    	if (portals != null) portals.clear();
    	for (Player play : Bukkit.getServer().getOnlinePlayers())
    	{
    		if (play != p) hunter.add(play);
    		if (play == p) target.add(play);
    	}
		for (Player hunt : hunter)
    	{
			hunt.getInventory().clear();
    		hunt.setHealth(20.0);
    		hunt.setFoodLevel(20);
    		hunt.getInventory().addItem(new ItemStack(Material.COMPASS));
    	}
    	for (Player targ : target)
    	{
    		targ.getInventory().clear();
    		targ.setHealth(20.0);
    		targ.setFoodLevel(20);
    		targ.getInventory().clear();
    	}
    }
    
    @EventHandler
    public void targetTrack(PlayerMoveEvent e)
    {
    	if (target.contains(e.getPlayer()))
    	{
    		Vector play = e.getPlayer().getVelocity();
    		if (play.getY() < 0)
    		{
    			play.setY(play.getY() * -1);
    		}
    		if(e.getFrom().getX() == e.getTo().getX()) {
    	        if(e.getFrom().getY() == e.getTo().getY()) {
    	        if(e.getFrom().getZ() == e.getTo().getZ()) {
        			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, false, false));
    	                }
    	            }
    	        }
    		else
    		{
    			for (PotionEffect potion : e.getPlayer().getActivePotionEffects())
    			{
    				if (potion.getType().toString().toLowerCase().contains("invis"))
    				{
    					e.getPlayer().removePotionEffect(potion.getType());
    				}
    				
    			}
    		}
    	}
    	double distance = 0.0;
    	Player t = e.getPlayer();
    	int slot = -1;
    	boolean hasLode = false;
    	boolean playerInNether = false;
    	if (hunter!= null && target != null)
    	{
    		if (target.contains(e.getPlayer()))
    		{
    			if (hunter != null)
                {
                	for (Player h : hunter)
                	{
                		distance = h.getLocation().distance(t.getLocation());
                		for (Player targ : target)
                		{
                			if (h.getWorld().getEnvironment() != targ.getWorld().getEnvironment()) {
                				if (portals.isEmpty() == false)
                        		{
                        			for (Location p : portals)
                            		{
                            			if (h.getLocation().distance(t.getLocation()) > h.getLocation().distance(p)) h.setCompassTarget(p);
                            			else h.setCompassTarget(t.getLocation());
                            		}
                        		}
                	            break;
                	        }
                			if (h.getWorld().getEnvironment() == World.Environment.NETHER && targ.getWorld().getEnvironment() == World.Environment.NETHER) 
    						{
                				for (ItemStack item : h.getInventory())
                				{
                					slot += 1;
                					if (item != null)
                					{
                						if (item.getType() == Material.COMPASS && (((CompassMeta) item.getItemMeta()).isLodestoneTracked() || ((CompassMeta) item.getItemMeta()).hasLodestone()))
                    					{
                							Bukkit.broadcastMessage("has lode");
                    						hasLode = true;
                    						break;
                    					}
                    					if (slot == 36)
                    					{
                    						hasLode = false;
                    						break;
                    					}
                					}
                				}
    						    ItemStack comp = new ItemStack(Material.COMPASS);
    						    CompassMeta compassMeta = (CompassMeta) comp.getItemMeta();
    					        compassMeta.setLodestoneTracked(false);
    					        compassMeta.setLodestone(targ.getLocation());
    					        comp.setItemMeta(compassMeta);
    						    if (hasLode == true) h.getInventory().setItem(slot, comp);
    						    if (hasLode == false) 
    						    {
    						    	h.getInventory().addItem(comp);
    						    }
    						}
                			if (distance > h.getLocation().distance(targ.getLocation()) && playerInNether == false)
                			{
                				distance = h.getLocation().distance(targ.getLocation());
                				t = targ;
                			}
                		}
                		h.setCompassTarget(t.getLocation());
                	}
                } 
    		} 
    	}
    }
    
    @EventHandler
    public void portal(PlayerPortalEvent e)
    {
    	portals.add(e.getPlayer().getLocation());
    }
    
    @EventHandler
    public void hunterRespawn(PlayerRespawnEvent e)
    {
    	if (hunter != null)
    	{
    		if (hunter.contains(e.getPlayer()))
    		{
    			if (e.isBedSpawn() || e.isAnchorSpawn())
    			{
    				e.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));
    				return;
    			}
    			else
    			{
    				e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
    				e.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));	
    				return;
    			}
    		}
    	}
    }
    
    @EventHandler
    public void targetDeath(PlayerDeathEvent e)
    {
    	Player p = e.getEntity();
    	if (target != null)
    	{
    		if (target.contains(p))
    		{
    			for (Player play : hunter)
    			{
    				play.sendTitle(org.bukkit.ChatColor.RED + "Hunters" + org.bukkit.ChatColor.WHITE + " win!", "", 10, 60, 10);
    			}
    		}
    	}
    }
}