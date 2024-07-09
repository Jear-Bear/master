package me.joda.adaptiveDeathSwap;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarFlag;
import java.util.Collections;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener
{
	int a;
    public boolean inGame = false;
    public ArrayList<Player> playerList = new ArrayList<>();
    public int x;
    public int z;
    public int y;
    public Location loc;
    public FileConfiguration config;
    BossBar bar;
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("swap").setExecutor((CommandExecutor)new CommandExe(this));
        this.x = 0;
        this.z = 0;
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
    
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event)
    {
    	if(inGame == true && event.getEntity() instanceof Player && event.getDamager() instanceof Player)
    	{
    		event.setCancelled(true);
    	}
    }
    
    public void nextGame(final Player p) {
    	if (bar != null) bar.removeAll();
    	bar = Bukkit.getServer().createBossBar("Time until swapping", BarColor.BLUE, BarStyle.SEGMENTED_6);
        bar.setVisible(true);
        bar.setProgress(1.0);
    	inGame = true;
    	playerList.clear();
    	a = 0;
        if (inGame) 
        {
        	this.getServer().getScheduler().cancelTasks((Plugin)this);;
            p.getLocation().getWorld().setTime(1000);
            for(Player play : this.getServer().getOnlinePlayers()) {
            	play.getInventory().clear();
            	play.setGameMode(GameMode.SURVIVAL);
            	play.setHealth(20.0);
                play.setFoodLevel(20);
            	playerList.add(play);
            	bar.addPlayer(play);
            }
            for (int i = 0; i < 72000; i++)
            {
            	if (i % 7200 == 0)
            	{
            		bar.setProgress(1.0);
            		p.getLocation().getWorld().setTime(1000);
            	}
        		this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
	                @Override
	                public void run() {
	                	a += 1;
	                	bar.setProgress((7200.0 - ((double) a % 7200)) / 7200.0);
	                	if (a % 7200 == 2400 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("4 minutes until swapping!");
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        }
	                	}
	                	if (a % 7200 == 3600 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("3 minutes until swapping!");
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        }
	                	}
	                	if (a % 7200 == 4800 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("2 minutes until swapping!");
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        }
	                	}
	                	if (a % 7200 == 6000 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("1 minute until swapping!");
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        }
	                	}
	                	if (a % 7200 == 6300 && inGame)
	                	{
	                		for (int z = 0; z < playerList.size() -1; z+=2)
	                		{
	                			String swap =  new StringBuilder().append(ChatColor.WHITE).append("You are swapping with ").append(ChatColor.RED).append(ChatColor.BOLD).append(playerList.get(z + 1).getDisplayName()).toString();
	                			playerList.get(z).sendTitle("", swap, 20, 80, 20);
	                			swap =  new StringBuilder().append(ChatColor.WHITE).append("You are swapping with ").append(ChatColor.RED).append(ChatColor.BOLD).append(playerList.get(z).getDisplayName()).toString();
	                			playerList.get(z + 1).sendTitle("", swap, 20, 80, 20);
	                		}
	                		if (playerList.size() % 2 == 1)
	                        {
	                        	playerList.get(playerList.size() - 1).sendTitle("", "You are swapping with nobody...", 0, 7200, 0);
	                        }
	                	}
	                	if (a % 7200 == 6600 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("30 seconds until swapping!");
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        }
	                	}
	                	if (a % 7200 == 7000 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("10", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7020 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("9", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7040 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("8", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7060 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("7", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7080 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("6", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7100 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("5", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7120 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("4", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7140 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("3", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7160 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("2", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7180 && inGame)
	                	{
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("1", "", 0, 20, 0);
	                        }
	                		
	                	}
	                	if (a % 7200 == 0 && inGame && a >= 7200)
	                	{
	                		if (playerList.size() % 2 == 0 && inGame)
	                		{
	                			for (int i = 0; i < playerList.size() - 1; i+=2)
		                		{
		                			Player one = playerList.get(i);
		                			Player two = playerList.get(i + 1);
		                			Location first = one.getLocation();
		                			one.teleport(two.getLocation());
		                			two.teleport(first);
		                		}
	                			Collections.shuffle(playerList);
	                		}
	                		else
	                		{
	                			for (int i = 0; i < playerList.size() - 1; i+=2)
		                		{
		                			Player one = playerList.get(i);
		                			Player two = playerList.get(i + 1);
		                			Location first = one.getLocation();
		                			one.teleport(two.getLocation());
		                			two.teleport(first);
		                		}
	                			Collections.shuffle(playerList);
	                		}
	                		for(int j = 0; j < playerList.size(); j++) 
	                		{
	                        	playerList.get(j).playSound(playerList.get(j).getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
	                        	playerList.get(j).sendTitle("SWAPPED", "", 0, 40, 0);
	                        }
	                		
	                	}
	                }
	            }, i);
            }
        }
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {	
    	Player p = e.getEntity();
    	playerList.remove(playerList.indexOf(p));
    	if (inGame)
    	{  		
            if (playerList.size() == 1) 
            {
            	Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append(playerList.get(0).getDisplayName()).append(ChatColor.WHITE).append(" has won the game!").toString());
            	inGame = false;
            	bar.removeAll();
            	Location winner = playerList.get(0).getLocation();
            	for(Player play : this.getServer().getOnlinePlayers()) {
            		play.getInventory().clear();
        			play.setGameMode(GameMode.SURVIVAL);
        			play.teleport(winner);
        			play.setHealth(20.0);
                    play.setFoodLevel(20);
                }       	
            }
            else if (playerList.size() == 0) 
            {
            	Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append(p.getDisplayName()).append(ChatColor.WHITE).append(" should really get some friends to play with :(").toString());
            	bar.removeAll();
            	inGame = false;
            	p.setGameMode(GameMode.SURVIVAL);
            }
            else
            {
            	p.setGameMode(GameMode.SPECTATOR);
            	Location s = p.getLocation();
            	if (p.getKiller() != null && p.getKiller() instanceof Player) 
        		{
            		s = p.getKiller().getLocation();
        		}
            	p.teleport(s);
            }
    	}
    }
    
    @EventHandler
    public void respawn(PlayerRespawnEvent e)
    {
    	e.setRespawnLocation(e.getPlayer().getWorld().getHighestBlockAt(this.x, this.z).getLocation());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
    	if (inGame)
    	{
    		Player p = e.getPlayer();
    		playerList.remove(playerList.indexOf(p));
            if (playerList.size() == 1)
            {
            	Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append(playerList.get(0).getDisplayName()).append(ChatColor.WHITE).append(" has won the game!").toString());
                bar.removeAll();
            	this.getServer().getScheduler().cancelTasks((Plugin)this);
                inGame = false; 
            }
    	}	
    }
    
    public int getRand() {
        if (Math.random() > 0.5) return (int)(Math.random() * 8000.0);
        return (int)(Math.random() * -8000.0);
    }  
}