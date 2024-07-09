package me.joda.spooky;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;



public class spookyCommand implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("spooky");
	String baseMessage = ChatColor.WHITE + "    /spooky ";
	TextComponent message;
	double speed;

	String[][] commands = new String[][]{
		{ChatColor.YELLOW + "start", ChatColor.GREEN + "Give players a " + ChatColor.YELLOW.toString() + "clock (" + ChatColor.AQUA.toString() + "/spooky on" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "stop", ChatColor.RED + "Stop " + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + "the plugin, and remove the clock (" + ChatColor.AQUA.toString() + "/money off" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "help", ChatColor.YELLOW + "Pulls up this super dandy help menu!"}
		};
	
	public spookyCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("spooky"))
			{
				if (args.length > 0)
				{
					if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("stop"))
					{
						Bukkit.getScheduler().cancelTasks(plugin);
						main.on = false;
						for (Player p : main.PlayerScore.keySet())
						{
							if (p.getInventory().contains(main.clock))
							{
								p.getInventory().remove(main.clock);
							}
						}
						main.PlayerScore.clear();
						main.PlayerChoice.clear();
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("start"))
					{
						Bukkit.getScheduler().cancelTasks(plugin);
						main.on = true;
						main.PlayerScore.clear();
						main.PlayerChoice.clear();
						sender.sendMessage(ChatColor.GOLD + "Plugin enabled");
						for (Player p : Bukkit.getOnlinePlayers())
						{
							if (!p.getInventory().contains(main.clock)) p.getInventory().addItem(main.clock);
							main.PlayerScore.put(p, 0);
							main.PlayerChoice.put(p, new ItemStack(Material.STICK));
						}
						
						points(main.PlayerScore);
				        return false;
					}
					else if (args[0].equalsIgnoreCase("help"))
					{
						sender.sendMessage(ChatColor.AQUA + "Usage (hover for info, click to run):");
						for(int i = 0; i < commands.length; i++)
						{
							message = new TextComponent("");
							message.setText(baseMessage + commands[i][0]);
							message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(commands[i][1])));
							message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spooky " + ChatColor.stripColor(commands[i][0])));
						    sender.spigot().sendMessage(message);
						}
					}
				}
			}
		}
		return false;
	}
	
	public void points(Map<Player, Integer> PS)
	{
		new BukkitRunnable() {
		    @Override
		    public void run() {
		    	for (Map.Entry<Player, Integer> PlayerScore : PS.entrySet()) 
	        	{
	      		  PlayerScore.getKey().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Score:" + ChatColor.RESET + " " + PlayerScore.getValue()));
	      	  	}
		    }
		}.runTaskTimer(plugin, 0L, 10L);
	}
	
	@EventHandler
	public void playerKillEntity(EntityDeathEvent e)
	{
		if (main.on)
		{
			if (e.getEntity().getKiller() != null)
			{
				if (e.getEntity().getKiller() instanceof Player)
				{
					if (e.getEntity() instanceof Player)
					{
						main.PlayerScore.put(e.getEntity().getKiller(), main.PlayerScore.get(e.getEntity().getKiller()) + 3);
					}
					else
					{
						main.PlayerScore.put(e.getEntity().getKiller(), main.PlayerScore.get(e.getEntity().getKiller()) + 1);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void playerClickClock(PlayerInteractEvent e)
	{
		if (main.on)
		{
			Player p = e.getPlayer();
			ItemStack clock = p.getInventory().getItemInMainHand();
			if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
			{
				if (clock.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Magic Clock") && clock.getType() == Material.CLOCK)
				{
					Inventory spooky = Bukkit.createInventory(null, 6 * 9, "Spooky stuff");
					spooky.setItem(12, main.jumpscare);
					spooky.setItem(13, main.spawnCreeper);
					spooky.setItem(14, main.giveBlindness);
					spooky.setItem(21, main.spawnWarden);
					spooky.setItem(22, main.teleportUp);
					spooky.setItem(23, main.giveSlowness);
					spooky.setItem(30, main.spawnGhast);
					spooky.setItem(31, main.givePoison);
					spooky.setItem(32, main.giveGlowing);
					spooky.setItem(39, main.turnInvisible);
					spooky.setItem(40, main.spawnTNT);
					spooky.setItem(41, main.scaryNoise);
					ItemStack coins = new ItemStack(Material.SUNFLOWER);
					ItemMeta meta = coins.getItemMeta();
					meta.setDisplayName("Tokens");
					ArrayList<String> lore = new ArrayList<String>();
					lore.add("Amount: " + main.PlayerScore.get(p));
					meta.setLore(lore);
					coins.setItemMeta(meta);
					spooky.setItem(53, coins);
		            p.openInventory(spooky);
				}
			}
		}
	}
	
	@EventHandler
	public void playerClickInventory(InventoryClickEvent e)
	{
		if (e.getView().getTitle().equalsIgnoreCase("spooky stuff"))
		{
			if (e.getClick() == ClickType.LEFT)
			{
				if (e.getCurrentItem() != null)
				{
					Inventory players = Bukkit.createInventory(null, 4 * 9, "Players");
					int count = 0;
					for (Player p : main.PlayerScore.keySet())
					{
						ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
						SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
						skullMeta.setOwningPlayer(p);
						skullMeta.setDisplayName(p.getDisplayName());
						skull.setItemMeta(skullMeta);
						if (count > 6)
						{
							players.setItem(19 + count - 7 + count, skull);
						}
						players.setItem(10 + count, skull);
						count += 1;
					}
					main.PlayerChoice.put((Player) e.getWhoClicked(), e.getCurrentItem());
					e.getWhoClicked().closeInventory();
					for(Material m : main.prices.keySet())
					{
						if(e.getCurrentItem().getType() == m)
						{
							if (main.PlayerScore.get((Player) e.getWhoClicked()) >= main.prices.get(m))
							{
								main.PlayerScore.put((Player) e.getWhoClicked(), main.PlayerScore.get(e.getWhoClicked()) - main.prices.get(m));
								e.getWhoClicked().openInventory(players);
							}
							else if(main.PlayerScore.get((Player) e.getWhoClicked()) < main.prices.get(m))
							{
								((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
								((Player) e.getWhoClicked()).sendMessage(ChatColor.RED + "Sorry, but you don't have enough tokens for that!");
							}
						}
					}
							
				}	
			}
			else e.setCancelled(true);
		}
		if (e.getView().getTitle().equalsIgnoreCase("players"))
		{
			if (e.getClick() == ClickType.LEFT)
			{
				if (e.getCurrentItem() != null)
				{
					e.getWhoClicked().closeInventory();
					for (Player p : main.PlayerScore.keySet())
					{
						if (p.getDisplayName().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName()))
						{
							if (main.PlayerChoice.get(p).getType() == Material.SKELETON_SKULL)
							{
								p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1));
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 1));
								Random rand =  new Random();
								int choice = rand.nextInt(2);
								if (choice == 0) p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1f, 1f);
								else p.playSound(p.getLocation(), Sound.ENTITY_GHAST_HURT, 1f, 1f);
								
							}
							if (main.PlayerChoice.get(p).getType() == Material.CREEPER_SPAWN_EGG)
							{
								p.getWorld().spawnEntity(p.getLocation(), EntityType.CREEPER);
							}
							if (main.PlayerChoice.get(p).getType() == Material.SPYGLASS)
							{
								p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 1));
							}
							if (main.PlayerChoice.get(p).getType() == Material.SCULK_SENSOR)
							{
								p.getWorld().spawnEntity(p.getLocation(), EntityType.WARDEN);
							}
							if (main.PlayerChoice.get(p).getType() == Material.ENDER_PEARL)
							{
								p.teleport(p.getLocation().add(0, 10, 0));
							}
							if (main.PlayerChoice.get(p).getType() == Material.COBWEB)
							{
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1));
							}
							if (main.PlayerChoice.get(p).getType() == Material.GHAST_SPAWN_EGG)
							{
								p.getWorld().spawnEntity(p.getLocation(), EntityType.GHAST);
							}
							if (main.PlayerChoice.get(p).getType() == Material.SPIDER_EYE)
							{
								p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
							}
							if (main.PlayerChoice.get(p).getType() == Material.BLAZE_POWDER)
							{
								p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 300, 1));
							}
							if (main.PlayerChoice.get(p).getType() == Material.POTION)
							{
								p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 1));
							}
							if (main.PlayerChoice.get(p).getType() == Material.TNT)
							{
								p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
							}
							if (main.PlayerChoice.get(p).getType() == Material.MUSIC_DISC_11)
							{
								p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1f, 1f);
							}
						}
					}
				}
			}
			else e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerModifyInventory(InventoryDragEvent e)
	{
		if (e.getView().getTitle().equalsIgnoreCase("spooky stuff") || e.getView().getTitle().equalsIgnoreCase("players"))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerDie(PlayerDeathEvent e)
	{
		if (e.getDrops().contains(main.clock))
		{
			e.getDrops().remove(main.clock);
		}
	}
	
	@EventHandler
	public void playerDropClock(PlayerDropItemEvent e)
	{
		if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(main.clock.getItemMeta().getDisplayName()))
		{
			int randomNum = ThreadLocalRandom.current().nextInt(1, 7 + 1);
			e.setCancelled(true);
    		Player p = e.getPlayer();
    		switch(randomNum)
    		{
    			case 1:
    	    		p.sendMessage(ChatColor.RED + "Hey, that's important! Don't lose it!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 2:
    	    		p.sendMessage(ChatColor.RED + "Just hold on to it! You'll need it!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 3:
    	    		p.sendMessage(ChatColor.RED + "Ah ah ah! You need that!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 4:
    	    		p.sendMessage(ChatColor.RED + "Just keep it! I promise you'll have much more fun with that in your inventory!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 5:
    	    		p.sendMessage(ChatColor.RED + "You need that! Don't go around trying to drop it!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 6:
    	    		p.sendMessage(ChatColor.RED + "Don't drop that! Seriously!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    			case 7:
    	    		p.sendMessage(ChatColor.RED + "That's a very important clock, don't lose it!");
    	    		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); 
    	    		break;
    		}
		}
	}
	
	@EventHandler
	public void playerRespawn(PlayerRespawnEvent e)
	{
		e.getPlayer().getInventory().addItem(main.clock);
	}

}
