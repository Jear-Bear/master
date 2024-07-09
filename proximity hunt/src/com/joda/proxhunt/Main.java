package com.joda.proxhunt;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener
{
	static ArrayList<Player> hunters = new ArrayList<>();
	static ArrayList<Player> runners = new ArrayList<>();
	static ArrayList<Location> portals = new ArrayList<>();
	ItemStack[] hunterInv = new ItemStack[] {new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.COMPASS)};
    ItemStack[] netherite = new ItemStack[]{new ItemStack(Material.NETHERITE_BOOTS), new ItemStack(Material.NETHERITE_LEGGINGS), new ItemStack(Material.NETHERITE_CHESTPLATE), new ItemStack(Material.NETHERITE_HELMET)};
	ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
    ItemMeta m = sword.getItemMeta();
    
	@Override
	public void onEnable()
	{
		m.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
	    sword.setItemMeta(m);
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		getCommand("prox").setExecutor(new ProxCommand());
	}
	
	@EventHandler
    public void runnersTrack(PlayerMoveEvent e)
    {
    	double distance = 0.0;
    	Player t = e.getPlayer();
    	int slot = -1;
    	boolean hasLode = false;
    	boolean playerInNether = false;
    	if (hunters != null && runners != null)
    	{
    		if (runners.contains(e.getPlayer()))
    		{
    			if (hunters != null)
                {
                	for (Player h : hunters)
                	{
                		distance = h.getLocation().distance(t.getLocation());
                		for (Player targ : runners)
                		{
                			if (h.getWorld().getEnvironment() != targ.getWorld().getEnvironment()) {
                				if (portals.isEmpty() == false)
                        		{
                        			for (Location p : portals)
                            		{
                            			if (h.getLocation().distance(t.getLocation()) > h.getLocation().distance(p)) (h).setCompassTarget(p);
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
    	if (hunters != null)
    	{
    		if (hunters.contains(e.getPlayer()))
    		{
    			if (e.isBedSpawn() || e.isAnchorSpawn())
    			{
    				e.getPlayer().getInventory().setArmorContents(netherite);
    				e.getPlayer().getInventory().addItem(new ItemStack(sword));
    				e.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));
    				return;
    			}
    			else
    			{
    				e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
    				e.getPlayer().getInventory().setArmorContents(netherite);
    				e.getPlayer().getInventory().addItem(new ItemStack(sword));
    				e.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));	
    				return;
    			}
    		}
    	}
    }
    
    @EventHandler
    public void runnersDeath(PlayerDeathEvent e)
    {
    	Player p = e.getEntity();
    	Bukkit.broadcastMessage("PLAYER DIED");
    	if (runners != null)
    	{
    		if (runners.contains(p))
    		{
    			Bukkit.broadcastMessage("RUNNER DIED");
    			runners.remove(p);
    			e.setDeathMessage("");
    			p.setGameMode(GameMode.SPECTATOR);
    			Bukkit.broadcastMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.WHITE + " has " + ChatColor.RED + "died!");
    			Bukkit.getScheduler().runTaskLater((Plugin) Bukkit.getServer(), new Runnable() {

					@Override
					public void run() {
						Bukkit.broadcastMessage("There are " + ChatColor.GREEN + runners.size() + ChatColor.WHITE + "Remaining!");
					}
    				
    			}, 60L);
    		}
    	}
    	if (hunters != null)
    	{
    		if (hunters.contains(p))
    		{
    			Bukkit.broadcastMessage("HUNTER DIED");
    			e.getDrops().clear();
    		}
    	}
    }
}