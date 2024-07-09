package me.joda.risingEnd;

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
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
    public Location loc;
    public FileConfiguration config;
    Plugin plugin = (Plugin) this;
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("rise").setExecutor((CommandExecutor)new CommandExe(this));
        this.x = 0;
        this.z = 0;
        makeInvs();
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void makeInvs() {
        final ItemStack Axe = new ItemStack(Material.DIAMOND_AXE);
        Axe.addEnchantment(Enchantment.DIG_SPEED, 5);
        final ItemStack Shovel = new ItemStack(Material.DIAMOND_SHOVEL);
        Shovel.addEnchantment(Enchantment.DIG_SPEED, 4);
        final ItemStack pickAxe = new ItemStack(Material.DIAMOND_PICKAXE);
        pickAxe.addEnchantment (Enchantment.DIG_SPEED, 5);
        final ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.DURABILITY, 3);
        Inv = new ItemStack[7];
        Inv[0] = Axe;
        Inv[1] = pickAxe;
        Inv[2] = Shovel;
        Inv[3] = bow;
        Inv[4] = new ItemStack(Material.COBBLESTONE, 32);
        Inv[5] = new ItemStack(Material.COOKED_BEEF, 32);
        Inv[6] = new ItemStack(Material.ARROW);
    }
    
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event)
    {
    	Player player = event.getPlayer();
        Location loc = player.getLocation();
        Location loca = new Location(player.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
        for(Player play : this.getServer().getOnlinePlayers()) {
			if (play.getGameMode() == GameMode.SURVIVAL)
			{
				if (play.getLocation().getBlockY() < y && inGame == true)
				{
					play.setHealth(0.0);
		        	Bukkit.broadcastMessage(play.getDisplayName() + " died to the void.");
		        	playerList.remove(playerList.indexOf(play));
				}
			}
        }
    }
    
    @EventHandler
    public void bow(EntityShootBowEvent event)
    {
    	if (event.getEntity() instanceof Player && y < 100)
    	{
    		int randomNum = ThreadLocalRandom.current().nextInt(1, 7 + 1);
    		event.setCancelled(true);
    		Player p = (Player) event.getEntity();
    		switch(randomNum)
    		{
    			case 1:
    	    		p.sendMessage(ChatColor.RED + "You can't take aim quite yet! Wait for the void to reach level 100!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 2:
    	    		p.sendMessage(ChatColor.RED + "Patience is virtue... Wait for the void to reach level 100!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 3:
    	    		p.sendMessage(ChatColor.RED + "Ah ah ah! Wait for the void to reach level 100!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 4:
    	    		p.sendMessage(ChatColor.RED + "Just hold on a minute! The void will reach level 100 soon!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 5:
    	    		p.sendMessage(ChatColor.RED + "You can't shoot quite yet! Wait for the void to reach level 100!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 6:
    	    		p.sendMessage(ChatColor.RED + "You need to wait a sec! The void hasn't reached level 100!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 7:
    	    		p.sendMessage(ChatColor.RED + "Now hold on there! Wait for the void to reach level 100!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    		}
    	}
    }
    
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event)
    {
    	if(inGame == true && event.getEntity() instanceof Player && event.getDamager() instanceof Player)
    	{
    		if (y <= 100)
    		{
    			int randomNum = ThreadLocalRandom.current().nextInt(1, 7 + 1);
        		event.setCancelled(true);
        		Player p = (Player) event.getEntity();
        		switch(randomNum)
        		{
        			case 1:
        	    		p.sendMessage(ChatColor.RED + "You can't hit anyone quite yet! Wait for the void to reach level 100!");
        	    		((Player) event.getEntity()).playSound(event.getDamager().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
        	    		break;
        			case 2:
        	    		p.sendMessage(ChatColor.RED + "Patience is virtue... Wait for the void to reach level 100!");
        	    		((Player) event.getEntity()).playSound(event.getDamager().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
        	    		break;
        			case 3:
        	    		p.sendMessage(ChatColor.RED + "Ah ah ah! Wait for the void to reach level 100!");
        	    		((Player) event.getEntity()).playSound(event.getDamager().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
        	    		break;
        			case 4:
        	    		p.sendMessage(ChatColor.RED + "Just hold on a minute! The void will reach level 100 soon!");
        	    		((Player) event.getEntity()).playSound(event.getDamager().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
        	    		break;
        			case 5:
        	    		p.sendMessage(ChatColor.RED + "You can't do that quite yet! Wait for the void to reach level 100!");
        	    		((Player) event.getEntity()).playSound(event.getDamager().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
        	    		break;
        			case 6:
        	    		p.sendMessage(ChatColor.RED + "You need to wait a sec! The void hasn't reached level 100!");
        	    		((Player) event.getEntity()).playSound(event.getDamager().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
        	    		break;
        			case 7:
        	    		p.sendMessage(ChatColor.RED + "Now hold on there! Wait for the void to reach level 100!");
        	    		((Player) event.getEntity()).playSound(event.getDamager().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
        	    		break;
        		}			
    		}
    	}
    }
    
    public void nextGame(final Player p) {
    	inGame = true;
    	playerList.clear();
		for(Player play : this.getServer().getOnlinePlayers()) {
			play.setGameMode(GameMode.SURVIVAL);
        	play.getInventory().setContents(Inv);
        	play.setHealth(20.0);
            play.setFoodLevel(20);
            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
            boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
            boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            play.getInventory().setBoots(boots);
        }
    	p.getLocation().getWorld().setTime(1000);
        if (inGame) 
        {
        	y = -65;
        	won = false;
            this.getServer().getScheduler().cancelTasks((Plugin)this);
            p.getWorld().getWorldBorder().setCenter((double)p.getLocation().getBlockX(), (double)p.getLocation().getBlockZ());
            p.getWorld().getWorldBorder().setSize(wbSize);
            Location spawn = p.getWorld().getWorldBorder().getCenter();
            for(Player play : this.getServer().getOnlinePlayers()) {
            	playerList.add(play);
            }
            for (a = 0; a <= 38100; a+= 100)
            {
            	if (inGame)
            	{
            		this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
    	                @Override
    	                public void run() {
    	                	y += 1;
    	                	if (y == 100)
    	                	{
    	                    	Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append("You can now ").append(ChatColor.RED).append(ChatColor.BOLD).append("attack ").append(ChatColor.WHITE).append(ChatColor.BOLD).append("other players!").toString());
    	                    	for(Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f); 
    	                	}
    	                		if (inGame) 
    	                		{
    	                			new BukkitRunnable() {
    	                			    @Override
    	                			    public void run() {
    	                			    	for (Player p : Bukkit.getOnlinePlayers()) 
    	    	        		        	{
    	    	        		      		  p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Level:" + ChatColor.RESET + " " + y));
    	    	        		      	  	}
    	                			    }
    	                			}.runTaskTimer(plugin, 0L, 5L);
    	                			
    	                		}
    		                	for (int x = spawn.getBlockX() - 64; x <= spawn.getBlockX() + 63; x++) 
    		                	{
    	                        	for (int z = spawn.getBlockZ() - 64; z <= spawn.getBlockZ() + 63; z++) 
    	                        	{
    	                        		if (inGame)
    	                        		{
    	                        			Location loc = new Location(p.getWorld(), x, y, z);
    	                        			loc.getBlock().setType(Material.AIR);
    	                        		}
    	                        	}
    	                		}
    	                }
    	            }, a);
            	}
            	
            }
            if (y == 316)
            {
            	Bukkit.broadcastMessage("The void has stopped rising!");
            	for(Player play : Bukkit.getOnlinePlayers()) {
            		play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f); 
                }
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
    		Location loc = p.getLocation();
            Location loca = new Location(p.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
    		
            if (playerList.size() == 1) 
            {
            	Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append(playerList.get(0).getDisplayName()).append(ChatColor.WHITE).append(" has won the game!").toString());
            	inGame = false;
            	p.getWorld().getWorldBorder().reset();
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
            	inGame = false;
            	p.getWorld().getWorldBorder().reset();
            	p.setGameMode(GameMode.SURVIVAL);
            	for(Player play : this.getServer().getOnlinePlayers()) {
            		play.getInventory().clear();
        			play.setGameMode(GameMode.SURVIVAL);
        			play.setHealth(20.0);
                    play.setFoodLevel(20); 
                }   
            }
            else
            {
            	p.setGameMode(GameMode.SPECTATOR);
            	Location s = p.getKiller().getLocation();
            	if (s != null) p.teleport(s);
            	else p.teleport(p.getWorld().getHighestBlockAt(loca).getLocation());
            }
    	}
    }
    
    @EventHandler
    public void breakVoid(BlockBreakEvent e)
    {
    	if (inGame && e.getBlock().getType() == Material.BLACK_CONCRETE) e.setCancelled(true);
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
            	p.getWorld().getWorldBorder().reset();
            	won = true;
                this.getServer().getScheduler().cancelTasks((Plugin)this);
                inGame = false; 
                for(Player play : this.getServer().getOnlinePlayers()) {
                	play.getInventory().clear();
        			play.setGameMode(GameMode.SURVIVAL);
        			play.teleport(playerList.get(0).getLocation());
        			play.setHealth(20.0);
                    play.setFoodLevel(20); 
                }   
            }
    	}	
    }
}