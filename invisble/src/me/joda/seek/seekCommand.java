package me.joda.seek;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BlockIterator;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class seekCommand implements Listener, CommandExecutor {

	private Main main;
	int secondCount = 0;
	int sec = 0;
	int min = 0;
	boolean full = false;
	boolean inGame = false;
	boolean q = true;
	ItemStack sword = new ItemStack(Material.NETHERITE_HOE);
	ItemStack[] hider = new ItemStack[] {new ItemStack(Material.FIREWORK_ROCKET, 16)};
	ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
    
	public seekCommand(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("seek"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().equals("on"))
				{
					main.on = true;
					if (main.inGamePlayers != null)
					{
						if (!main.inGamePlayers.isEmpty()) main.inGamePlayers.clear();
					}
					for (Player p : Bukkit.getOnlinePlayers())
					{
						main.inGamePlayers.add(p);
					}
					chooseSeeker(main.inGamePlayers);
				}
				if (args[0].toLowerCase().equals("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}
	
	public void chooseSeeker(ArrayList<Player> players)
	{
		ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET, 16);
		ItemMeta fireworkMeta = firework.getItemMeta();
		FireworkMeta fwm = (FireworkMeta) fireworkMeta;
		FireworkEffect effects = FireworkEffect.builder().withColor(Color.AQUA, Color.FUCHSIA).withFade(Color.WHITE).with(Type.BALL_LARGE).withTrail().trail(true).build();
		fwm.addEffect(effects);
		fwm.setPower(2);
		fwm.setDisplayName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Big Firework");
		firework.setItemMeta(fwm);
		hider[0] = firework;
		for (Player p : Bukkit.getOnlinePlayers()) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
		for (int i = 0; i <= 120; i+=5)
        {	
			int c = i;
    		Bukkit.getServer().getScheduler().runTaskLater(main, (Runnable)new Runnable() {
				@Override
                public void run() {
					Random rand = new Random(); //instance of random class
				    int upperbound = main.inGamePlayers.size();
				    int randomPlayer = rand.nextInt(upperbound); 
					String title = ChatColor.WHITE + ChatColor.BOLD.toString() + main.inGamePlayers.get(randomPlayer).getDisplayName();
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (c <= 100)
						{
							p.sendTitle(title, "", 0, 5, 0);
							p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
						}
						else if (c == 120)
						{
							p.sendTitle(ChatColor.RED + title, "", 0, 40, 0);
							p.playSound(p.getLocation(), Sound.ENTITY_ENDERMITE_DEATH, 1f, 1f);
							Player play = main.inGamePlayers.get(randomPlayer);
							Bukkit.getServer().getScheduler().runTaskLater(main, (Runnable)new Runnable() {
								@Override
				                public void run() {
									start(play);
								}
							}, 40);
						}
						
					}
    			}
    		}, i);
        }
	}
	
	public void lobby()
	{
		for (Player play : Bukkit.getOnlinePlayers())
		{
			for (PotionEffect potion : play.getActivePotionEffects())
			{
					play.removePotionEffect(potion.getType());
			}
		}
		if (main.inGamePlayers != null)
		{
			if (!main.inGamePlayers.isEmpty()) main.inGamePlayers.clear();
		}
		q = true;
		int task = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) main, new Runnable() {
		    @Override
		    public void run() {
		    	String message = ChatColor.GREEN + ChatColor.BOLD.toString() + Bukkit.getOnlinePlayers().size() + "/12";
				for (Player p : Bukkit.getOnlinePlayers())
				{
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
				}
		    }
		}, 0L, 10L);
		if (Bukkit.getOnlinePlayers().size() == 8)
		{
			full = true;
		}
		if (Bukkit.getOnlinePlayers().size() >= 4)
		{
			q = false;
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "10", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 0L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "9", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 20L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "8", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 40L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "7", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 60L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "6", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 80L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "5", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 100L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "4", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 120L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "3", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 140L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "2", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 160L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player play : Bukkit.getOnlinePlayers()) 
	            	{
	            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "1", "", 0, 20, 0);
	            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            	}
	            }
	        }, 180L);
			if (Bukkit.getOnlinePlayers().size() < 4)
			{
				lobby();
			}
			q = false;
			Bukkit.getScheduler().cancelTask(task);
			for (Player p : Bukkit.getOnlinePlayers())
			{
				main.inGamePlayers.add(p);
			}
			chooseSeeker(main.inGamePlayers);
		}
	}
	
	public void start(Player p)
	{
		boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 2);
		Location lock1 = new Location (p.getWorld(), -1277, 67, -289);
		Location lock2 = new Location (p.getWorld(), -1277, 68, -289);
		Location lock3 = new Location (p.getWorld(), -1279, 67, -289);
		Location lock4 = new Location (p.getWorld(), -1279, 68, -289);
		Location seekerSpawn = new Location(p.getWorld(), -1277, 67, -283, 180, 0);
		Location hiderSpawn = new Location(p.getWorld(), -1273, 66, -302, 90, 0);
		if (main.hiders != null)
		{
			if (!main.hiders.isEmpty()) main.hiders.clear();
		}
		if (main.seeker != null)
		{
			if (!main.seeker.isEmpty()) main.seeker.clear();
		}
		q = false;
		inGame = true;
		main.won = false;
		secondCount = 0;
		for (Player player : main.inGamePlayers)
		{
			player.setGameMode(GameMode.SURVIVAL);
		}
		sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
		main.death = false;
		Bukkit.getServer().getScheduler().cancelTasks(main);
		main.readyOrNot = false;
		main.seeker.add(p);
		for (Player play : Bukkit.getOnlinePlayers()) play.sendMessage(ChatColor.GREEN + "Loading map..");
		for (Player play : Bukkit.getOnlinePlayers())
		{
			play.setHealth(20.0);
			play.setFoodLevel(20);
			play.getInventory().clear();
			for (PotionEffect potion : play.getActivePotionEffects())
			{
					play.removePotionEffect(potion.getType());
			}
			if (!main.seeker.contains(play))
			{
				main.hiders.add(play);
				play.getInventory().setContents(hider);
				play.teleport(hiderSpawn);
			}
			else
			{
				lock1.getBlock().setType(Material.BEDROCK);
				lock2.getBlock().setType(Material.BEDROCK);
				lock3.getBlock().setType(Material.BEDROCK);
				lock4.getBlock().setType(Material.BEDROCK);
				play.teleport(seekerSpawn);
				play.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100000, 1, false, false));
			}
		}
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
            @Override
            public void run() {
            	for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("Hiders have 90 seconds to hide!");
            	for (Player play : Bukkit.getOnlinePlayers()) 
            	{
            		play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .5f, 1f);
            		buildSidebar(play, main.seeker.size(), main.hiders.size(), "10", "00");
            	}
            }
        }, 80L);
		for (int i = 80; i < 1880; i+=20)
        {	
    		Bukkit.getServer().getScheduler().runTaskLater(main, (Runnable)new Runnable() {
				@Override
                public void run() {
					secondCount += 1;
    				int total = 91 - secondCount;
    				min = total / 60;
    				sec = total % 60;
    				String secon = String.valueOf(sec);
    				if (secon.length() == 1) secon = "0" + secon;
    				String color = "";
    				if (total <= 90 && total > 60) color = ChatColor.GREEN + ChatColor.BOLD.toString();
    				if (total <= 60 && total > 45) color = ChatColor.YELLOW + ChatColor.BOLD.toString();
    				if (total <= 45 && total > 15) color = ChatColor.GOLD + ChatColor.BOLD.toString();
    				if (total <= 15 && total > 5) color = ChatColor.RED + ChatColor.BOLD.toString();
    				if (total <= 5) color = ChatColor.DARK_RED + ChatColor.BOLD.toString();
					String message = color + min + ":" + secon;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
					} 
    			}
    		}, i);
        }
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
            @Override
            public void run() {
            	for (Player play : Bukkit.getOnlinePlayers()) 
            	{
            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "5", "", 0, 20, 0);
            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            	}
            }
        }, 1780L);
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
            @Override
            public void run() {
            	for (Player play : Bukkit.getOnlinePlayers()) 
            	{
            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "4", "", 0, 20, 0);
            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            	}
            }
        }, 1800L);
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
            @Override
            public void run() {
            	for (Player play : Bukkit.getOnlinePlayers()) 
            	{
            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "3", "", 0, 20, 0);
            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            	}
            }
        }, 1820L);
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
            @Override
            public void run() {
            	for (Player play : Bukkit.getOnlinePlayers()) 
            	{
            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "2", "", 0, 20, 0);
            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            	}
            }
        }, 1840L);
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
            @Override
            public void run() {
            	for (Player play : Bukkit.getOnlinePlayers()) 
            	{
            		play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "1", "", 0, 20, 0);
            		play.playSound(play.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            	}
            }
        }, 1860L);
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
            @Override
            public void run() {
            	main.readyOrNot = true;
            	lock1.getBlock().setType(Material.AIR);
				lock2.getBlock().setType(Material.AIR);
				lock3.getBlock().setType(Material.AIR);
				lock4.getBlock().setType(Material.AIR);
            	for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("The seeker is free!");
            	main.seeker.get(0).getInventory().clear();
            	main.seeker.get(0).getInventory().addItem(sword);
            	main.seeker.get(0).getInventory().setBoots(boots);
		        for (Player p : Bukkit.getOnlinePlayers())
		        {
		        	p.sendTitle(ChatColor.RED.toString() + ChatColor.BOLD + "RUN", "", 0, 20, 0);
		        	p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
		        	p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
		        }
		        secondCount = 0;
            }
        }, 1879L);
		for (int i = 1880; i < 13880; i+=20)
        {	
			int c = i;
    		Bukkit.getServer().getScheduler().runTaskLater(main, (Runnable)new Runnable() {
				@Override
                public void run() {
    				secondCount += 1;
    				int total = 600 - secondCount;
    				min = total / 60;
    				sec = total % 60;
    				String secon = String.valueOf(sec);
    				if (secon.length() == 1) secon = "0" + secon;
    				for (Player p : Bukkit.getOnlinePlayers())
    				{
    					buildSidebar(p, main.seeker.size(), main.hiders.size(), String.valueOf(min), secon);
    				}
    				if (c >= 7900)
    				{
    					for(Player p : main.seeker)
    					{
    						if (!p.getInventory().contains(new ItemStack(Material.COMPASS)))
    						{
    							p.getInventory().addItem(new ItemStack(Material.COMPASS));
    						}
    					}
    				}
    			}
    		}, i);
        }
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
            @Override
            public void run() {
		        for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("Seekers will receive compasses in 1 minute!");
		        for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            }
        }, 6679L);
		Bukkit.getServer().getScheduler().runTaskLater(main, (Runnable)new Runnable() {
			@Override
            public void run() {
				for (Player p : main.seeker)
				{
					p.getInventory().addItem(new ItemStack(Material.COMPASS));
				}
				for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("Seekers have received compasses!");
		        for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
			}
		}, 7880);
		Bukkit.getServer().getScheduler().runTaskLater(main, (Runnable)new Runnable() {
			@Override
            public void run() {
				main.won = true;
				for (Player play : Bukkit.getOnlinePlayers())
				{
					play.sendTitle(ChatColor.GOLD + "Hiders have won!", "", 10, 40, 10);
					play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
					Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
        	            @Override
        	            public void run() {
        	            	for (Player p : Bukkit.getOnlinePlayers())
        	            	{
        	            		buildSidebar(play, main.seeker.size(), main.hiders.size(), String.valueOf(min), String.valueOf(sec));
        	            		p.getInventory().clear();
        	            	}
        	            }
        	        }, 60L);
				}
			}
		}, 13881);
	}
	
	@EventHandler
	public void fall(EntityDamageEvent e)
	{
		if (!main.readyOrNot)
		{
			e.setCancelled(true);
		}
		if (e.getCause() == DamageCause.FALL)
		{
			e.setCancelled(true);
		}
	}
	
	/*
	@EventHandler
    public void sneakHide(PlayerToggleSneakEvent e)
    {
		if (main.on) 
		{
			if (main.hiders.contains(e.getPlayer()))
			{
				if (e.isSneaking())
		    	{
		    		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, false, false));
		    	}
		    	else if (!e.isSneaking())
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
		}
		else return;
    }
	*/
	
	/*
	@EventHandler
	public void hit(EntityDamageByEntityEvent e)
	{
		if (!main.death)
		{
			if (main.seeker.contains(e.getDamager()) && main.hiders.contains(e.getEntity()))
			{
				main.hiders.remove(((Player) e.getEntity()));
				main.seeker.add(((Player) e.getEntity()));
				Player player = ((Player) e.getEntity());
				for (PotionEffect potion : player.getActivePotionEffects())
				{
					player.removePotionEffect(potion.getType());
				}
				for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + (((Player) e.getEntity())).getDisplayName() + " has been found!");
				for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
				Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
		            @Override
		            public void run() {
		            	if (main.hiders.size() == 0)
		            	{
		            		for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("Seekers have won!");
		            		for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
		            	}
		            	((Player)e.getEntity()).getInventory().clear();
		            	((Player)e.getEntity()).getInventory().setContents(inv);
		            	for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("They are now a seeker!");
		            	for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .5f, 1f);
		            }
		        }, 40L);
			}
		}
	}
	*/
	
	@EventHandler
    public void FrameEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Firework) {
			e.setCancelled(true);
		}
        if (e.getEntity() instanceof ItemFrame) {
            if (e.getDamager() instanceof Player) {
            	e.setCancelled(true);
            }
        }
        if ((main.seeker.contains(e.getDamager()) && (main.seeker.contains(e.getEntity())) || main.hiders.contains(e.getDamager()) && main.hiders.contains(e.getEntity())))
        {
        	e.setCancelled(true);
        }
    }
	
	public void buildSidebar(Player player, int seekers, int hiders, String minutes, String seconds) {
		if (main.won)
		{
			for (Player p : Bukkit.getOnlinePlayers())
        	{
        		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        	}
			return;
		}
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        
		@SuppressWarnings("deprecation")
		Objective obj = board.registerNewObjective("test", "dummy");
		obj.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Hide and Seek");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		Team seeker = board.registerNewTeam("Seekers");
		seeker.addEntry(ChatColor.RED.toString());
		seeker.setPrefix(ChatColor.RED.toString() + ChatColor.BOLD + "Seekers: ");
		seeker.setSuffix(ChatColor.WHITE + "" + seekers);
		obj.getScore(ChatColor.RED.toString()).setScore(5);
		
		Score gap = obj.getScore(" ");
		gap.setScore(4);
		
		Team hider = board.registerNewTeam("Hiders");
		hider.addEntry(ChatColor.BLUE.toString());
		hider.setPrefix(ChatColor.BLUE.toString() + ChatColor.BOLD + "Hiders: ");
		hider.setSuffix(ChatColor.WHITE + "" + hiders);
		obj.getScore(ChatColor.BLUE.toString()).setScore(3);
		
		Score gap2 = obj.getScore("  ");
		gap2.setScore(2);
		
		Team timer = board.registerNewTeam("Timer");
		timer.addEntry(ChatColor.GREEN.toString());
		timer.setPrefix(ChatColor.GREEN.toString() + ChatColor.BOLD + "Time left: ");
		timer.setSuffix(ChatColor.WHITE + (minutes + ":" + seconds));
		obj.getScore(ChatColor.GREEN.toString()).setScore(1);
		player.setScoreboard(board);
		
		if (Integer.parseInt(minutes) + Integer.parseInt(seconds) == 0)
		{
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			player.setDisplayName(player.getName());
			player.setPlayerListName(player.getName());
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
	            @Override
	            public void run() {
	            	for (Player p : Bukkit.getOnlinePlayers())
	            	{
	            		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	            	}
	            }
	        }, 40L);
		}
	}
	@EventHandler
	public void death(PlayerDeathEvent e)
	{
		if (main.on) 
		{
			e.setDeathMessage("");
			if (e.getEntity() instanceof Player)
			{
				e.getDrops().clear();
				Player p = e.getEntity();
				if (main.hiders.contains(p))
				{
					if (!main.readyOrNot)
					{
						return;
					}
					else if (main.readyOrNot)
					{
						main.hiders.remove(p);
						main.seeker.add(p);
						if (main.hiders.size() == 0)
						{
							String secon = String.valueOf(sec);
		    				if (secon.length() == 1) secon = "0" + secon;
		    				main.won = true;
		            		for (Player play : Bukkit.getOnlinePlayers())
		            		{
		            			play.sendTitle(ChatColor.GOLD + "Seekers have won!", "", 10, 40, 10);
		            			play.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		    					play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
		            			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
		            	            @Override
		            	            public void run() {
		            	            	for (Player p : Bukkit.getOnlinePlayers())
		            	            	{
		            	            		buildSidebar(play, main.seeker.size(), main.hiders.size(), String.valueOf(min), String.valueOf(sec));
		            	            		p.getInventory().clear();
		            	            	}
		            	            }
		            	        }, 60L);
		            		}
						}
						/*
						else if (main.hiders.size() == 1)
						{
							main.death = true;
						}
						*/
		            	String secon = String.valueOf(sec);
	    				if (secon.length() == 1) secon = "0" + secon;
						for (Player play : Bukkit.getOnlinePlayers())
						{
							play.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + p.getDisplayName() + " has died!");
							play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
							buildSidebar(play, main.seeker.size(), main.hiders.size(), String.valueOf(min), String.valueOf(secon));
						}
						if (main.hiders.size() > 0)
						{
							Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
					            @Override
					            public void run() {
					            	e.getEntity().getInventory().clear();
					            	e.getEntity().getInventory().addItem(sword);
					            	Bukkit.broadcastMessage("They are now a seeker!");
					            	for (Player play : Bukkit.getOnlinePlayers()) 
					            	{
					            		play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .5f, 1f);
					            	}
					            }
					        }, 40L);
						}
						if (main.hiders.size() > 0)
						{
							Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
					            @Override
					            public void run() {
					            	int count = 0;
									for (Player player : main.hiders)
									{
										count += 1;
									}
									Bukkit.broadcastMessage("There are " + ChatColor.GOLD + count + ChatColor.WHITE + " hiders left");
									for (Player players : Bukkit.getOnlinePlayers())
									{
										players.playSound(players.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
									}
					            }
					        }, 80L);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void place(BlockPlaceEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
    public void onEnterPortal(PlayerPortalEvent e)
	{
		e.setCancelled(true);
    }
	
	@EventHandler
	public void painting(HangingBreakByEntityEvent e)
	{
		if (e.getEntity() instanceof Painting)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e)
	{
		if (main.on && main.hiders.size() > 0) {
			if (main.seeker.contains(e.getPlayer()))
			{
				Location seekerSpawn = new Location(e.getPlayer().getWorld(), -1277, 67, -283, 180, 0);
				e.setRespawnLocation(seekerSpawn);
				e.getPlayer().getInventory().clear();
				ItemMeta swordMeta = sword.getItemMeta();
				swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
				sword.setItemMeta(swordMeta);
				e.getPlayer().getInventory().addItem(sword);
				e.getPlayer().getInventory().setBoots(boots);
			}
			else if (main.hiders.contains(e.getPlayer()))
			{
				Location hiderSpawn = new Location(e.getPlayer().getWorld(), -1273, 66, -302, 90, 0);
				e.getPlayer().getInventory().setContents(hider);
				e.getPlayer().teleport(hiderSpawn);
			}
		}
	}
	
	@EventHandler
	public void playerInteract(PlayerInteractEvent e){
		if(e.getClickedBlock() instanceof Bed) {
	        e.setCancelled(true);
	    }
	}
	
	@EventHandler
    public void noDrop(PlayerDropItemEvent e)
    {
    	e.setCancelled(true);
    }
	
	@EventHandler
	public void look(PlayerMoveEvent e)
	{
		if (main.on) 
		{
			for (Player p : main.seeker) 
			{
				if (!p.hasPotionEffect(PotionEffectType.GLOWING)) p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100000, 1, false, false));
			}
			if (main.seeker.contains(e.getPlayer()))
			{
				double distance = 10000.0;
				Player target = main.hiders.get(0);
				for (Player player : main.hiders)
				{
					if (e.getPlayer().getLocation().distance(player.getLocation()) < distance && distance != 0.0)
					{
						distance = e.getPlayer().getLocation().distance(player.getLocation());
						target = player;
					}
				}
				if (target != null) e.getPlayer().setCompassTarget(target.getLocation());
			}
			if (main.hiders.contains(e.getPlayer()) || q || !main.readyOrNot)
			{
				e.getPlayer().setFoodLevel(20);
			}
		}
	//if (main.readyOrNot) getEntityInLineOfSightBlockIterator(e.getPlayer(), 50);
	}
	
	@EventHandler
	public void breakBlock(BlockBreakEvent e)
	{
		e.setCancelled(true);
		e.getPlayer().sendMessage(ChatColor.RED + "Sorry, but you can't do that");
	}
	/*
	public void cagePlayer(Player p, Material m)
	{
		Location l = p.getLocation();
		l.add(0, 1, 0);
		l.getBlock().setType(Material.AIR);
		l.add(0, 1, 0);
		l.getBlock().setType(Material.AIR);
		l = p.getLocation();
		l.getBlock().setType(m);
		l.add(0, 3, 0);
		l.getBlock().setType(m);
		l = p.getLocation();
		l.subtract(1, -1, 0);
		l.getBlock().setType(m);
		l.add(0, 1, 0);
		l.getBlock().setType(m);
		l = p.getLocation();
		l.subtract(-1, -1, 0);
		l.getBlock().setType(m);
		l.add(0, 1, 0);
		l.getBlock().setType(m);
		l = p.getLocation();
		l.subtract(0, -1, 1);
		l.getBlock().setType(m);
		l.add(0, 1, 0);
		l.getBlock().setType(m);
		l = p.getLocation();
		l.subtract(0, -1, -1);
		l.getBlock().setType(m);
		l.add(0, 1, 0);
		l.getBlock().setType(m);
	}
	*/

	@EventHandler
    public void onSleep(PlayerBedEnterEvent e) 
	{
        e.setCancelled(true);
    }
	
	@EventHandler
	public void noChest(InventoryClickEvent e)
	{
		if (e.getInventory().getType().name().equalsIgnoreCase("chest"))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e)
	{
		Location hiderSpawn = new Location(e.getPlayer().getWorld(), -1273, 66, -302, 90, 0);
			if (!q)
			{
				e.getPlayer().setGameMode(GameMode.SPECTATOR);
				e.getPlayer().setMaxHealth(20.0);
				if (main.seeker != null)
				{
					e.getPlayer().teleport(main.seeker.get(0).getLocation());
				}
			}
			else if (q)
			{
				e.getPlayer().teleport(hiderSpawn);
				main.inGamePlayers.add(e.getPlayer());
				e.getPlayer().setMaxHealth(20.0);
				e.getPlayer().setGameMode(GameMode.SURVIVAL);
				if (Bukkit.getOnlinePlayers().size() == 1)
				{
					lobby();
				}
			}
			if (e.getPlayer().getWorld().getPlayers().size() == 1)
			{
				e.getPlayer().teleport(hiderSpawn);
				lobby();
			}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e)
	{
			if (inGame)
			{
				Player p = e.getPlayer();
				if (main.seeker.contains(p)) main.seeker.remove(p);
				if (main.hiders.contains(p)) main.hiders.remove(p);
			}
			else if (q)
			{
				if (main.inGamePlayers.contains(e.getPlayer()))
				{
					main.inGamePlayers.remove(e.getPlayer());
				}
			}
	}
	
	public void getEntityInLineOfSightBlockIterator(Player p, int range)
	{
		List<Entity> targetList = p.getNearbyEntities(range, range, range);
		BlockIterator bi = new BlockIterator(p, range);
		Entity target = null;
		while(bi.hasNext())
		{
			Block b = bi.next();
			int bx = b.getX();
			int by = b.getY();
			int bz = b.getZ();
	
			if (b.getType().isSolid()) 
			{ 
				break;
			} 
			
			else 
			{
	
				for(Entity e : targetList) 
				{
					Location l = e.getLocation();
					double ex = l.getX();
					double ey = l.getY();
					double ez = l.getZ();
					if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) 
					{
						target = e;
					}
	
				}
	
			}

		}

		if (target instanceof Player)
		{
			if (main.hiders.contains((Player) target))
			{
				if (main.seeker.contains(p))
				{
					for (PotionEffect potion : ((Player) target).getActivePotionEffects())
					{
						if (potion.getType().toString().toLowerCase().contains("invis"))
						{
							return;
						}
					}
					main.hiders.remove((Player) target);
					main.seeker.add((Player) target);
					Player player = (Player) target;
					for (PotionEffect potion : player.getActivePotionEffects())
					{
						player.removePotionEffect(potion.getType());
					}
					for (Player play : Bukkit.getOnlinePlayers())
					{
						play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
						play.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + ((Player) target).getDisplayName() + " has been found!");
					}
					Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
			            @Override
			            public void run() {
			            	for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("They are now a seeker!");
			            	for (Player play : Bukkit.getOnlinePlayers()) play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .5f, 1f);
			            }
			        }, 40L);
				}
			}
		}
	}
}
