package me.joda.risingLava;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.EndGateway;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener
{
	public boolean won = false;
	public double wbSize = 128.0;
    public boolean inGame = false;
    public ItemStack[] Inv;
    public ArrayList<Player> playerList = new ArrayList<>();
    public ArrayList<Block> world = new ArrayList<>();
    public int x;
    public int z;
    public int y;
    public int a;
    BossBar bar;
    public Location loc;
    public FileConfiguration config;
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("rise").setExecutor((CommandExecutor)new CommandExe(this));
        this.x = 0;
        this.z = 0;
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void nextGame(final Player p) {
    	if (bar != null) bar.removeAll();
    	bar = Bukkit.getServer().createBossBar("Void Level", BarColor.PURPLE, BarStyle.SOLID);
        bar.setVisible(true);
        bar.setProgress((double) y/319.0);
    	inGame = true;
    	playerList.clear();
    	p.getLocation().getWorld().setTime(1000);
        if (inGame) 
        {
        	y = -1;
        	won = false;
            this.getServer().getScheduler().cancelTasks((Plugin)this);
            p.getWorld().getWorldBorder().setCenter((double)p.getLocation().getBlockX(), (double)p.getLocation().getBlockZ());
            p.getWorld().getWorldBorder().setSize(wbSize);
            Location spawn = p.getWorld().getWorldBorder().getCenter();
            for (a = 0; a <= 63800; a+= 200)
            {
            	if (inGame)
            	{
            		this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
    	                @Override
    	                public void run() {
    	                	y += 1;
    	                	bar.setProgress((double) y/319.0);
    	                		if (inGame) Bukkit.broadcastMessage("Level: " + y);
    		                	for (int x = spawn.getBlockX() - 64; x <= spawn.getBlockX() + 63; x++) 
    		                	{
    	                        	for (int z = spawn.getBlockZ() - 64; z <= spawn.getBlockZ() + 63; z++) 
    	                        	{
    	                        		if (inGame)
    	                        		{
    	                        			
    	                        			Location loc = new Location(p.getWorld(), x, y, z);
    	                        			Location underLoc = new Location(p.getWorld(), loc.getBlockX(), loc.getBlockY() -1, loc.getBlockZ());
		                            		if (underLoc.getBlock().getType() == Material.BLACK_WOOL) underLoc.getBlock().setType(Material.AIR);
    	                        			world.add(loc.getBlock());
    		                            	if (loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.CAVE_AIR || loc.getBlock().getType() == Material.WATER) 
    		                            	{
    		                            		
    		                            		loc.getBlock().setType(Material.LAVA);
    		                            		//Block end = loc.getBlock();
    		                            		//BlockState endState = end.getState();
    		                            		//EndGateway gateWay = (EndGateway) endState;
    		                            		//gateWay.setAge(-50000);
    		                            		
    		                            	}
    		                            	
    		                            	
    		                            	
    	                        		}
    	                            	
    	                        	}
    	                		}
    	                }
    	            }, a);
            	}
            	
            }
            
            if (y == 319)
            {
            	Bukkit.broadcastMessage("The lava has stopped rising!");
            	for(Player play : Bukkit.getOnlinePlayers()) {
            		play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f); 
                }
            }
        }
    }
}