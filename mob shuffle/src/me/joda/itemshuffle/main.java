package me.joda.itemshuffle;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.apache.commons.lang.WordUtils;
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

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class main extends JavaPlugin implements Listener
{
	int a;
    public boolean inGame = false;
    public ArrayList<Player> inProg = new ArrayList<>();
    public ArrayList<Player> done = new ArrayList<>();
    public ArrayList<Integer> playerItem = new ArrayList<>();
    public int x;
    public int z;
    public int y;
    Player winner;
    public int round = 0;
    public int item;
    Player placehold;
    public String itemName;
    public String finName;
    public String get;
    public Location loc;
    public FileConfiguration config;
    BossBar bar;
    ArrayList<EntityType> mobs = new ArrayList<>(
		Arrays.asList(
			EntityType.CHICKEN,
			EntityType.COW,
			EntityType.PIG,
			EntityType.SHEEP,
			EntityType.SQUID,
			EntityType.SALMON
		)
    );
    		
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("mob").setExecutor((CommandExecutor)new CommandExe(this));
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
    
    @SuppressWarnings("deprecation")
	public void nextGame(final Player p) {
    	placehold = p;
    	if (winner != null)
    	{
    		for(Player play : this.getServer().getOnlinePlayers()) {    
            	if (round == 1)
            	{
            		play.setGameMode(GameMode.SURVIVAL);
                	play.setHealth(20.0);
                    play.setFoodLevel(20);
                    play.getInventory().clear();
                    play.teleport(winner.getLocation());
            	}
            	if (play.getGameMode() == GameMode.SURVIVAL) play.sendTitle("", get, 20, 80, 20);
            }
    	}
		round += 1;
    	if (inProg != null) inProg.clear();
    	if (done != null) done.clear();
    	if (playerItem != null) playerItem.clear();
    	if (round == 4)
    	{
    		mobs.add(EntityType.CREEPER);
    		mobs.add(EntityType.SPIDER);
    		mobs.add(EntityType.SKELETON);
    		mobs.add(EntityType.ZOMBIE);
    		mobs.add(EntityType.DROWNED);
    		mobs.add(EntityType.COD);
    	}
    	if (round == 5)
    	{
    		mobs.add(EntityType.RABBIT);
    		mobs.add(EntityType.HUSK);
    		mobs.add(EntityType.DOLPHIN);
    		mobs.add(EntityType.HORSE);
    		mobs.add(EntityType.DONKEY);
    	}
    	if (round == 7)
    	{
    		mobs.add(EntityType.VILLAGER);
    		mobs.add(EntityType.IRON_GOLEM);
    		mobs.add(EntityType.BAT);
    		mobs.add(EntityType.BEE);
    		mobs.remove(EntityType.CREEPER);
    		mobs.remove(EntityType.SPIDER);
    		mobs.remove(EntityType.SKELETON);
    		mobs.remove(EntityType.SQUID);
    		mobs.add(EntityType.CAVE_SPIDER);
    		mobs.add(EntityType.ENDERMAN);
    	}
    	if (round == 9)
    	{
    		mobs.remove(EntityType.DROWNED);
    		mobs.remove(EntityType.COD);
    		mobs.remove(EntityType.SQUID);
    		mobs.remove(EntityType.SALMON);
    	}
    	if (round == 15)
    	{
    		mobs.add(EntityType.BLAZE);
    		mobs.add(EntityType.ZOMBIFIED_PIGLIN);
    		mobs.add(EntityType.PIGLIN);
    		mobs.add(EntityType.GHAST);
    		mobs.add(EntityType.STRIDER);
    		mobs.add(EntityType.WITHER_SKELETON);
    		mobs.add(EntityType.PIGLIN_BRUTE);
    		mobs.remove(EntityType.COW);
    		mobs.remove(EntityType.SHEEP);
    		mobs.remove(EntityType.PIG);
    		mobs.remove(EntityType.CHICKEN);
    	}
    	if (bar != null) bar.removeAll();
    	bar = Bukkit.getServer().createBossBar("Time left", BarColor.GREEN, BarStyle.SEGMENTED_6);
        bar.setVisible(true);
        bar.setProgress(1.0);
    	inGame = true;
    	inProg.clear();
    	done.clear();
    	a = 0;
        if (inGame) 
        {
        	this.getServer().getScheduler().cancelTasks((Plugin)this);;
            for(Player play : this.getServer().getOnlinePlayers()) {    
            	bar.addPlayer(play);
            	if (round == 1)
            	{
            		play.setGameMode(GameMode.SURVIVAL);
                	play.setHealth(20.0);
                    play.setFoodLevel(20);
                    play.getInventory().clear();
                    play.setBedSpawnLocation(play.getLocation());
            	}
            	Random random = new Random();
            	item = random.nextInt(mobs.size());
        		itemName = mobs.get(item).getName();
        		finName = WordUtils.capitalizeFully(itemName.replace("_", " "));
    			get = new StringBuilder().append(ChatColor.WHITE).append("Your mob is ").append(ChatColor.GREEN).append(ChatColor.BOLD).append(finName).toString();
    			if (play.getGameMode() == GameMode.SURVIVAL) playerItem.add(item);
            	if (play.getGameMode() == GameMode.SURVIVAL) inProg.add(play);
            	if (play.getGameMode() == GameMode.SURVIVAL)
            	{
            		play.sendTitle("Round " + ChatColor.GOLD.toString() + ChatColor.BOLD + round, get, 20, 80, 20);
            		play.sendMessage(get);
            	}
            }
            for (int i = 0; i < 7201; i++)
            {	
        		this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
	                @Override
	                public void run() {
	                	a += 1;
	                	bar.setProgress((7200.0 - ((double) a % 7200)) / 7200.0);
	                	if (inProg.size() == 0 && done.size() == 1)
	                    {
	                    	Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append(done.get(0).getDisplayName()).append(ChatColor.WHITE).append(" has won the game!").toString());
	                    	for (Player player : Bukkit.getOnlinePlayers())
	                    	{
	                    		player.getInventory().clear();
	                    		player.setGameMode(GameMode.SURVIVAL);
	                    	}
	                    	round = 0;
	                    	bar.removeAll();
	                    	winner = done.get(0);
	                    	done.get(0).getInventory().clear();
	                    	inGame = false;
	                    	done.clear(); 
	                    }
	                	if (inProg.size() == 0 && done.size() > 1 && a < 7200)
	                	{
	                		Bukkit.broadcastMessage("Everyone killed their mobs! Time for the next round.");
	                		for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
	                		bar.removeAll();
	                		done.clear();
	                		nextGame(placehold);
	                	}
	                	if (a % 7200 == 2400 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("4 minutes left!");
	                        for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                	}
	                	if (a % 7200 == 3600 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("3 minutes left!");
	                		for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                	}
	                	if (a % 7200 == 4800 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("2 minutes left!");
	                		for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                	}
	                	if (a % 7200 == 6000 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("1 minute left!");
	                		for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                	}
	                	if (a % 7200 == 6600 && inGame) 
	                	{
	                		Bukkit.broadcastMessage("30 seconds left!");
	                		for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                	}
	                	if (a % 7200 == 7000 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("10", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7020 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("9", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7040 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("8", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7060 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("7", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7080 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("6", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7100 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("5", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7120 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("4", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7140 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("3", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7160 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("2", "", 0, 20, 0);
	                        }
	                	}
	                	if (a % 7200 == 7180 && inGame)
	                	{
	                		for(int j = 0; j < inProg.size(); j++) 
	                		{
	                        	inProg.get(j).playSound(inProg.get(j).getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	                        	inProg.get(j).sendTitle("1", "", 0, 20, 0);
	                        }
	                		
	                	}
	                	if (a == 7200 && inGame)
	                	{
	                		for (Player p : inProg)
	                		{

                				Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append(p.getDisplayName()).append(ChatColor.WHITE).append(" didn't find their mob in time!").toString());
                				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, .5f);
	                			p.setGameMode(GameMode.SPECTATOR);
	                			inProg.remove(p);
	                			bar.removeAll();
	                			if (inGame == true) nextGame(placehold);
	                		}
	                	}
	                }
	            }, i);
            }
        }
    }

    @EventHandler
    public void mobKill(EntityDeathEvent e)
    {
    	if (e.getEntity().getKiller() != null)
    	{
    		if (e.getEntity().getKiller() instanceof Player)
    		{
    			Player p = (Player) e.getEntity().getKiller();
    			if (e.getEntityType() == mobs.get(playerItem.get(inProg.indexOf(p))))
    			{
    				playerItem.remove(inProg.indexOf(p));
        			done.add(p);
        			inProg.remove(p);
        			Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append(p.getDisplayName()).append(ChatColor.WHITE).append(" found their mob!").toString());
        			p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
    			}
    		}
    		
    	}
    }
    
    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
    	if (inGame)
    	{
    		Player p = e.getPlayer();
    		inProg.remove(inProg.indexOf(p));
            if (inProg.size() == 1)
            {
            	Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append(inProg.get(0).getDisplayName()).append(ChatColor.WHITE).append(" has won the game!").toString());
                bar.removeAll();
            	this.getServer().getScheduler().cancelTasks((Plugin)this);
                inGame = false; 
            }
    	}	
    }
}