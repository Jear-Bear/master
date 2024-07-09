package com.joda.assassin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.event.block.Action;


public class assassinCommand implements CommandExecutor, Listener {
	boolean start = false;
	Player winner;
	ItemStack[] Inv;
	ArrayList<String> lore = new ArrayList<>();
	private Main main;
	int p1 = 0;
	int p2 = 0;
	int secondCount = 0;
	int sec = 0;
	int min = 0;
	int round = 0;
	BukkitTask shrink1, shrink2, count1;
	Location spawn;
	Player hunter;
	Player target;
	double wbSize = 100.0;
	ItemStack star = new ItemStack(Material.NETHER_STAR);
	ItemMeta starMeta = star.getItemMeta();
	ArrayList<Player> ready = new ArrayList<>();
	private int taskId;
	
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
				winner = (Player) sender;
				makeInvs();
				if (main.players != null) main.players.clear();
				if (main.roundsWon != null) main.roundsWon.clear();
				Player play = (Player) sender;
			}
			for (Player p : main.players)
			{
				main.roundsWon.put(p, 0);
				main.buildScore(p, main.roundsWon, main.players, main.score1, main.score2);
			}
			startRound((Player) sender);
		}
		return false;
	}
	
	@EventHandler
    public void onDeath(PlayerDeathEvent e) {
		main.inGame = false;
		main.dead = true;
		if (lore != null) lore.clear();
		e.getDrops().clear();
        e.setDeathMessage("");
        if (e.getEntity() instanceof Player)
        {
        	Player p = e.getEntity();
        	p.getWorld().getWorldBorder().setSize(3000000);
        	if (main.players.get(1).equals(p))
        	{
        		Bukkit.broadcastMessage(ChatColor.YELLOW + main.players.get(0).getDisplayName() + " has won this round!");
        		main.roundsWon.put(main.players.get(0), main.roundsWon.get(main.players.get(0)) + 1);
        		main.players.get(0).getScoreboard().getTeam(main.players.get(0).getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(main.players.get(0)) + "/20");
        		main.players.get(1).getScoreboard().getTeam(main.players.get(0).getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(main.players.get(0)) + "/20");
        	}
        	else if (main.players.get(0).equals(p))
        	{
        		Bukkit.broadcastMessage(ChatColor.YELLOW + main.players.get(1).getDisplayName() + " has won this round!");
        		main.roundsWon.put(main.players.get(1), main.roundsWon.get(main.players.get(1)) + 1);
        		main.players.get(0).getScoreboard().getTeam(main.players.get(1).getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(main.players.get(1)) + "/20");
        		main.players.get(1).getScoreboard().getTeam(main.players.get(1).getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(main.players.get(1)) + "/20");
        	}
        	for (Player p1 : Bukkit.getOnlinePlayers()) 
        	{
        		p1.getInventory().clear();
        		p1.getInventory().addItem(star);
        	}
        	Bukkit.getScheduler().cancelTasks((Plugin) this);
        	if (ready != null) ready.clear();
        }
    }
	
	@EventHandler
    public void onPlayerAttack(EntityDamageEvent event)
    {
    	if(event.getEntity() instanceof Player)
    	{
    		Player p = (Player) event.getEntity();
    		if (p.getInventory().contains(star) || main.cDown.getCounting())
    		{
    			event.setCancelled(true);
    		}
    	}
    }
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e)
	{
		if (spawn != null) e.setRespawnLocation(spawn);
		main.findSpawn(e.getPlayer());
		Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() 
        {
            @Override
            public void run() {
            	e.getPlayer().getInventory().addItem(star);
            }
        }, 20L);
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
		if (ready.contains(main.players.get(0)) && ready.contains(main.players.get(1)))
		{
			ready.clear();
			main.players.get(0).getInventory().clear();
			main.players.get(1).getInventory().clear();
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
		for (Player p : Bukkit.getOnlinePlayers())
        {
        	p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
        }
		secondCount = 0;
		sec = 0;
		min = 0;
		start = true;
		main.hasTold = false;
		main.inGame = true;
		main.dead = false;
		if (spawn != null) spawn.getChunk().setForceLoaded(false);
		Bukkit.broadcastMessage(ChatColor.GREEN + "Loading map..");
		main.findSpawn(play);
		spawn = main.spawn;
		spawn.getChunk().setForceLoaded(true);
		spawn.getChunk().load(true);
		spawn.add(0, 2, 0);
		wbSize = 160.0;
		play.getWorld().setSpawnLocation(spawn);
        play.getWorld().getWorldBorder().setCenter(spawn.getBlockX(), spawn.getBlockZ());
        play.getWorld().getWorldBorder().setSize(160);
		round = main.roundsWon.get(main.players.get(0)) + main.roundsWon.get(main.players.get(1)) + 1;
		if (round == 21) return;
		String r = "Round " + ChatColor.GOLD.toString() + ChatColor.BOLD + round;
		String hunt = "You're the " + ChatColor.RED + "hunter";
		String targ = "You're the " + ChatColor.BLUE + "target";
		for (Player p : main.players)
		{
			p.setHealth(20.0);
			p.setFoodLevel(20);
			p.getInventory().setContents(Inv);
			if (round % 2 == 0)
			{
				hunter = main.players.get(0);
				target = main.players.get(1);
			}
			else
			{
				hunter = main.players.get(1);
				target = main.players.get(0);
			}
		}
		hunter.sendTitle(r, hunt, 20, 40, 20);
		target.sendTitle(r, targ, 20, 40, 20);
		hunter.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        hunter.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        hunter.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        hunter.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        target.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        target.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        target.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        target.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        hunter.getInventory().addItem(new ItemStack(Material.COMPASS));
        hunter.teleport(spawn);
        target.teleport(spawn);
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				hunter.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 220, 100));
				hunter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 100));
				hunter.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 100));
				main.countdownTitle(10);
			}
		}, 80L);
        Bukkit.getScheduler().runTaskLater((Plugin) main, new Runnable()
		{
			@Override
			public void run() {
				start = false;
				hunter.playSound(hunter.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
				target.playSound(hunter.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
			}
		}, 280L);
    	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
            	if (main.inGame)
				{
					secondCount += 1;
    				int total = 301 - secondCount;
    				min = total / 60;
    				sec = total % 60;
    				String secon = String.valueOf(sec);
    				if (secon.length() == 1) secon = "0" + secon;
    				String color = "";
    				if (total <= 300 && total > 150) color = ChatColor.GREEN + ChatColor.BOLD.toString();
    				if (total <= 150 && total > 90) color = ChatColor.YELLOW + ChatColor.BOLD.toString();
    				if (total <= 90 && total > 60) color = ChatColor.GOLD + ChatColor.BOLD.toString();
    				if (total <= 60 && total > 30) color = ChatColor.RED + ChatColor.BOLD.toString();
    				if (total <= 30) color = ChatColor.DARK_RED + ChatColor.BOLD.toString();
					String message = color + min + ":" + secon;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
					} 
					if (total == 0)
					{
						Bukkit.broadcastMessage(ChatColor.YELLOW + target.getDisplayName() + " has won this round!");
						main.inGame = false;
						main.dead = true;
						if (lore != null) lore.clear();
						if (ready != null) ready.clear();
						main.roundsWon.put(target, main.roundsWon.get(target) + 1);
		        		main.players.get(0).getScoreboard().getTeam(target.getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(target) + "/20");
		        		main.players.get(1).getScoreboard().getTeam(target.getDisplayName()).setSuffix(ChatColor.GOLD + "" + main.roundsWon.get(target) + "/20");
						scheduler.cancelTasks(main);
					}
				}
            	else scheduler.cancelTasks(main);
            }
        }, 280L, 20L);
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e)
	{
		main.players.get(0).setCompassTarget(main.players.get(1).getLocation());
		main.players.get(1).setCompassTarget(main.players.get(0).getLocation());
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
