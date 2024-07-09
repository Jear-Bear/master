package me.joda.ophunt;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.boss.BossBar;


import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class main extends JavaPlugin implements Listener
{
	public main plugin;
    private static final String Player = null;
	public boolean inGame = false;
    public FileConfiguration config;
    BossBar bar;
    int lvl = 1;
    Location portal = null;
    Location hunterSpawn;
    ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
    ItemStack[] hunterInv = new ItemStack[] {new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.COMPASS)};
    ItemStack[] netherite = new ItemStack[]{new ItemStack(Material.NETHERITE_BOOTS), new ItemStack(Material.NETHERITE_LEGGINGS), new ItemStack(Material.NETHERITE_CHESTPLATE), new ItemStack(Material.NETHERITE_HELMET)};
    public ArrayList<Player> target = new ArrayList<>();
    public ArrayList<Player> hunter = new ArrayList<>();
    public ArrayList<Location> portals = new ArrayList<>();
    public void onEnable() {
    	for (ItemStack i : netherite)
    	{
    		i.addEnchantment(Enchantment.DURABILITY, 3);
    	}
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("hunt").setExecutor((CommandExecutor)new CommandExe(this));
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
    
    public void start(Player p)
    {
    	ItemMeta m = sword.getItemMeta();
        m.setDisplayName("Glizzy");
        sword.setItemMeta(m);
    	lvl = 1;
    	hunterSpawn = p.getLocation();
    	if (target != null) target.clear();
    	if (hunter != null) hunter.clear();
    	if (portals != null) portals.clear();
    	for (Player play : Bukkit.getServer().getOnlinePlayers())
    	{
    		if (play != p) target.add(play);
    	}
    	hunter.add(p);
		for (Player hunt : hunter)
    	{
			hunt.getInventory().clear();
    		hunt.setHealth(20.0);
    		hunt.setFoodLevel(20);
    		hunt.getInventory().setArmorContents(netherite);
    		hunt.getInventory().addItem(sword);
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
    	double distance = 0.0;
    	Player t = e.getPlayer();
    	Location portal = null;
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
    public void targetDeath(PlayerDeathEvent e)
    {
    	Player p = e.getEntity();
    	if (target != null)
    	{
    		if (target.contains(p))
    		{
    			p.setGameMode(GameMode.SPECTATOR);
    		}
    	}
    	if (hunter != null)
    	{
    		if (hunter.contains(p))
    		{
    			e.getDrops().clear();
    		}
    	}
    }
}