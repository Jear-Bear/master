package com.joda.assassin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;


public class assassinCommand implements CommandExecutor, Listener {
	boolean start = false;
	ItemStack[] Inv;
	ArrayList<String> lore = new ArrayList<>();
	ArrayList<ItemStack> clutchItems = new ArrayList<>(Arrays.asList(
			new ItemStack(Material.WATER_BUCKET),
			new ItemStack(Material.SWEET_BERRIES),
			new ItemStack(Material.BIRCH_BOAT)			
			));
	private Main main;
	int x = 0;
	int y = 0;
	boolean countdown = false;
	Random rand = new Random();
	double wbSize = 100.0;
	ItemStack star = new ItemStack(Material.NETHER_STAR);
	ItemMeta starMeta = star.getItemMeta();
	ArrayList<Player> ready = new ArrayList<>();
	
	public assassinCommand(Main main)
	{
		this.main = main;
		starMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Ready up");
		lore.add("right click to ready up");
		starMeta.setLore(lore);
		star.setItemMeta(starMeta);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("start"))
		{
			if (sender instanceof Player)
			{
				if (Bukkit.getOnlinePlayers().size() <= 1) 
				{
					((Player) sender).sendMessage(ChatColor.RED + "Sorry, you can't play this game alone! Go grab some friends real quick!");
					((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
					return false;
				}
				makeInvs();
				main.powerBlocks.clear();
				main.assassins.clear();
				main.players.clear();
				ready.clear();
				main.assassinPoints = 0;
				main.runnerPoints = 0;
				main.runner = ((Player) sender);
				for (Player p : Bukkit.getServer().getOnlinePlayers()) 
				{
					main.players.add(p);
					if (p != main.runner) main.assassins.add(p);
				}
				startRound(main.runner);
			}
		}
		return false;
	}
	
	@EventHandler
	public void noBreak(BlockBreakEvent e)
	{
		if (e.getPlayer().getInventory().contains(star))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void noPickup(InventoryPickupItemEvent e)
	{
		if (e.getInventory().contains(star))
		{
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void noDrop(PlayerDropItemEvent e)
	{
		if (e.getItemDrop().getItemStack() == star)
		{
			e.getPlayer().sendMessage(ChatColor.RED + "Sorry, but you can't drop that!");
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
			e.setCancelled(true);
		}
	}
	
	@EventHandler
    public void onDeath(PlayerDeathEvent e) {
        
        if (e.getEntity() instanceof Player)
        {
        	Player p = e.getEntity();
        	if (p.getDisplayName() == main.runner.getDisplayName())
        	{
        		main.assassinPoints += 1;
        		e.setDeathMessage("");
        		main.inGame = false;
        		if (lore != null) lore.clear();
        		e.getDrops().clear();
            	p.getWorld().getWorldBorder().setSize(3000000);
            	for (Player play : Bukkit.getOnlinePlayers())
            	{
            		int random = rand.nextInt(5);
            		switch(random)
            		{
            		case 0:
            			play.sendMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.RESET.toString() + " took the L!");
            			play.playSound(play.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            			break;
            		case 1:
            			play.sendMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.RESET.toString() + " made an oopsie!");
            			play.playSound(play.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            			break;
            		case 2:
            			play.sendMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.RESET.toString() + " was using their brain too hard!");
            			play.playSound(play.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            			break;
            		case 3:
            			play.sendMessage("Dang " + ChatColor.AQUA + p.getDisplayName() + ChatColor.RESET.toString() + "... that's rough");
            			play.playSound(play.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            			break;
            		case 4:
            			play.sendMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.RESET.toString() + " disappointed their parents!");
            			play.playSound(play.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            			break;
            		}
            	}
            	for (Player p1 : Bukkit.getOnlinePlayers()) 
            	{
            		p1.getInventory().clear();
            		p1.getInventory().addItem(star);
            	}
            	if (ready != null) ready.clear();
        	}
        	else
            {
            	e.getEntity().setGameMode(GameMode.SPECTATOR);
            	int c = 0;
            	for (Player play : main.assassins)
            	{
            		if (play.getGameMode() == GameMode.SURVIVAL) c += 1;
            	}
            	if (c == 0)
            	{
            		e.setDeathMessage("");
            		e.getDrops().clear();
            		main.inGame = false;
            		if (lore != null) lore.clear();
            		e.getEntity().getWorld().getWorldBorder().setSize(3000000);
            		main.runnerPoints += 1;
            		for (Player play : Bukkit.getOnlinePlayers()) 
            		{
            			play.getInventory().clear();
                		play.getInventory().addItem(star);
            			play.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + main.runner.getDisplayName() + ChatColor.RESET + " has won!");
            			play.playSound(play.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            		}
            	}
            }
        }
        
    }
	
	@EventHandler
    public void onPlayerAttack(EntityDamageEvent event)
    {
    	if(event.getEntity() instanceof Player)
    	{
    		Player p = (Player) event.getEntity();
    		if (p.getInventory().contains(star))
    		{
    			event.setCancelled(true);
    		}
    	}
    }
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e)
	{
		if (main.spawn != null) e.setRespawnLocation(main.spawn);
		main.findSpawn(e.getPlayer());
		if (main.inGame)
		{
			if (e.getPlayer() != main.runner)
			{
		        e.getPlayer().setGameMode(GameMode.SPECTATOR);
			}
		}
		else if (!main.inGame)
		{
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() 
	        {
	            @Override
	            public void run() {
	            	e.getPlayer().getInventory().addItem(star);
	            }
	        }, 20L);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		if (player.getItemInHand().getType().equals(Material.NETHER_STAR))
		{
			EquipmentSlot hand = e.getHand();
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if (hand.equals(EquipmentSlot.HAND))
				{
					if (ready.contains(player))
					{
						Bukkit.broadcastMessage(player.getDisplayName() + " is " + ChatColor.RED + "not ready");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, .5f);
						ready.remove(player);
					}
					else
					{
						Bukkit.broadcastMessage(player.getDisplayName() + " is " + ChatColor.GREEN + "ready");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
						if (!(ready.contains(player))) ready.add(player);
					}
				}
			}
		}
		if (ready.size() == (1 + main.assassins.size()))
		{
			ready.clear();
			for (Player p : main.players) p .getInventory().clear();
			Bukkit.broadcastMessage(ChatColor.GOLD + "Starting next round in: ");
			Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
			{
				@Override
				public void run() {
					Bukkit.broadcastMessage("3");
					player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
				}
			}, 20L);
			Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
			{
				@Override
				public void run() {
					Bukkit.broadcastMessage("2");
					player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
				}
			}, 40L);
			Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
			{
				@Override
				public void run() {
					Bukkit.broadcastMessage("1");
					player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
				}
			}, 60L);
			Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
			{
				@Override
				public void run() {
					main.nextRound(player);
				}
			}, 80L);
		}
	}
	
	public void startRound(Player play)
	{
		play.getWorld().setTime(0L);
		start = true;
		countdown = true;
		main.inGame = true;
		if (main.spawn != null) main.spawn.getChunk().setForceLoaded(false);
		Bukkit.broadcastMessage(ChatColor.GREEN + "Loading map..");
		main.findSpawn(play);
		main.spawn.getChunk().setForceLoaded(true);
		main.spawn.getChunk().load(true);
		main.spawn.add(0, 2, 0);
		main.powerBlocks.clear();
		wbSize = 160.0;
		play.getWorld().setSpawnLocation(main.spawn);
        play.getWorld().getWorldBorder().setCenter(main.spawn.getBlockX(), main.spawn.getBlockZ());
        play.getWorld().getWorldBorder().setSize(160);
    	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
		        	for (Player p : main.players) 
		        	{
		      		  p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + ChatColor.BOLD.toString() + "Assassins: " + ChatColor.RESET + main.assassinPoints + "   :   " + ChatColor.AQUA.toString()  + ChatColor.BOLD.toString() + main.runner.getDisplayName() + ": " + ChatColor.RESET + main.runnerPoints));
		      		  p.setGameMode(GameMode.SURVIVAL);
		      	  	}
				}
        }, 20L, 20L);
        for (int i = 0; i <= 4; i++)
        {
        	switch(i)
        	{
	        	case 0:
	        		x = (rand.nextInt(54) + 26)*-1 + main.spawn.getBlockX();
	        		y = rand.nextInt(80)*-1 + main.spawn.getBlockZ();
	        		main.powerBlocks.add(play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0));
	        		play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0).getBlock().setType(Material.DIAMOND_BLOCK);
	        		break;
	        	case 1:
	        		x = (rand.nextInt(26) - 26) + main.spawn.getBlockX();
	        		y = rand.nextInt(80)*-1 + main.spawn.getBlockZ();
	        		main.powerBlocks.add(play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0));
	        		play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0).getBlock().setType(Material.DIAMOND_BLOCK);
	        		break;
	        	case 2:
	        		x = (rand.nextInt(55) + 26) + main.spawn.getBlockX();
	        		y = rand.nextInt(80)*-1 + main.spawn.getBlockZ();
	        		main.powerBlocks.add(play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0));
	        		play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0).getBlock().setType(Material.DIAMOND_BLOCK);
	        		break;
	        	case 3:
	        		x = rand.nextInt(80)*-1 + main.spawn.getBlockX();
	        		y = rand.nextInt(80) + main.spawn.getBlockZ();
	        		main.powerBlocks.add(play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0));
	        		play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0).getBlock().setType(Material.DIAMOND_BLOCK);
	        		break;
	        	case 4:
	        		x = rand.nextInt(80) + main.spawn.getBlockX();
	        		y = rand.nextInt(80) + main.spawn.getBlockZ();
	        		main.powerBlocks.add(play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0));
	        		play.getWorld().getHighestBlockAt(x, y).getLocation().add(0, 1, 0).getBlock().setType(Material.DIAMOND_BLOCK);
	        		break;
        	}
        }
		String r = "Target: " + ChatColor.GOLD.toString() + ChatColor.BOLD + main.runner.getDisplayName();
		String s = "Objective: " + ChatColor.RED.toString() + ChatColor.BOLD + "Run";
		String hunt = "You're an " + ChatColor.RED + "assassin";
		String targ = "You're the " + ChatColor.BLUE + "runner";
		
		for (Player p : main.assassins) 
		{
			makeInvs();
        	p.getInventory().setContents(Inv);
			p.sendTitle(r, hunt, 20, 40, 20);
			p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
	        p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
	        p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
	        p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
	        p.getInventory().addItem(new ItemStack(Material.COMPASS));
	        p.teleport(main.spawn);
		}
		
		makeInvs();
    	main.runner.getInventory().setContents(Inv);
		main.runner.sendTitle(s, targ, 20, 40, 20);
		main.runner.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
		main.runner.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		main.runner.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		main.runner.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
		main.runner.teleport(main.spawn);
        
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				for (Player p : main.assassins)
				{
					p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 220, 100));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 100));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 100));
				}
				main.runner.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 100));
				main.countdownTitle(10);
			}
		}, 80L);
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				start = false;
				countdown = false;
				for (Player p : main.assassins) p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
				main.runner.playSound(main.runner.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
			}
		}, 280L);
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e)
	{
		if (e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.DIAMOND_BLOCK))
		{
			for (Player p : Bukkit.getOnlinePlayers()) 
			{
				p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + e.getPlayer().getDisplayName() + ChatColor.RESET + " has enabled a " + ChatColor.AQUA.toString() + "power block!");
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
			}
			int random = rand.nextInt(5);
			switch(random)
			{
			case 0:
				for (Player p : main.assassins) p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2400, 1));
				for (Player p : Bukkit.getOnlinePlayers()) 
				{
					p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Assassins" + ChatColor.RESET + " are now " + ChatColor.AQUA.toString() + "invisible!");
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
				}
				break;
			case 1:
				for (Player p : main.assassins) p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2400, 1));
				main.runner.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2400, 1));
				for (Player p : Bukkit.getOnlinePlayers()) 
				{
					p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Everyone" + ChatColor.RESET + " has " + ChatColor.AQUA.toString() + "levitation!");
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
				}
				break;
			case 2:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder add 100");
				for (Player p : Bukkit.getOnlinePlayers()) 
				{
					p.sendMessage("The " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "world border" + ChatColor.RESET + " has expanded by " + ChatColor.AQUA.toString() + "100 blocks!");
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
				}
				break;
			case 3:
				for (int x = main.spawn.getBlockX() - 80; x <= main.spawn.getBlockX() + 79; x++) 
            	{
                	for (int z = main.spawn.getBlockZ() - 80; z <= main.spawn.getBlockZ() + 79; z++) 
                	{
            			Location loc = new Location(e.getPlayer().getWorld(), x, e.getPlayer().getWorld().getHighestBlockYAt(main.spawn) + 50, z);
            			loc.getBlock().setType(Material.LAVA);
                	}
        		}
				for (Player p : Bukkit.getOnlinePlayers()) 
				{
					p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Watch out" + ChatColor.AQUA.toString() + " above! " + ChatColor.RESET + ChatColor.RED.toString() + "Lava " + ChatColor.RESET + "incoming!");
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
				}
				break;
			case 4:
				for (Player p : Bukkit.getOnlinePlayers())
				{
					p.sendMessage("Get ready to" + ChatColor.YELLOW.toString() + " clutch!");
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
					int Random = rand.nextInt(101);
					if (Random <= 16)
					{
						ItemStack potion = new ItemStack(Material.POTION, 1);
		                PotionMeta potionmeta = (PotionMeta) potion.getItemMeta();
		                potionmeta.setMainEffect(PotionEffectType.SLOW_FALLING);
		                potion.setItemMeta(potionmeta);
		                p.getInventory().addItem(potion);
					}
					else
					{
						Random = rand.nextInt(3);
						p.getInventory().addItem(clutchItems.get(Random));
					}
					Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() 
			        {
			            @Override
			            public void run() {
			            	p.teleport(p.getLocation().add(0, 100, 0));
			            }
			        }, 40L);
					
				}
				break;
			}
			
			for (Location l : main.powerBlocks)
			{
				l.getBlock().setType(Material.AIR);
			}
		}
		for (Player p : main.assassins) p.setCompassTarget(main.runner.getLocation());
	}
	
	public void makeInvs() {
        final ItemStack Axe = new ItemStack(Material.IRON_AXE);
        Axe.addEnchantment(Enchantment.DIG_SPEED, 2);
        final ItemStack Shovel = new ItemStack(Material.IRON_SHOVEL);
        Shovel.addEnchantment(Enchantment.DIG_SPEED, 2);
        final ItemStack pickAxe = new ItemStack(Material.IRON_PICKAXE);
        pickAxe.addEnchantment (Enchantment.DIG_SPEED, 2);
        Inv = new ItemStack[6];
        Inv[0] = new ItemStack(Material.IRON_SWORD);
        Inv[1] = Axe;
        Inv[2] = pickAxe;
        Inv[3] = Shovel;
        Inv[4] = new ItemStack(Material.COBBLESTONE, 16);
        Inv[5] = new ItemStack(Material.COOKED_BEEF, 16);
    }
}
