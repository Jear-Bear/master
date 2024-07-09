package me.joda.itemshuffle;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.concurrent.ThreadLocalRandom;


import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class main extends JavaPlugin implements Listener
{
	int a;
	private static final DecimalFormat df = new DecimalFormat("0.00");
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
    Plugin plugin = this;
    public Location loc;
    public FileConfiguration config;
    ArrayList<ItemStack> items = new ArrayList<>(
		Arrays.asList(
			new ItemStack(Material.GRANITE),
			new ItemStack(Material.DIORITE),
			new ItemStack(Material.ANDESITE),
			new ItemStack(Material.DIRT, 32),
			new ItemStack(Material.COBBLESTONE, 16),
			new ItemStack(Material.SAND, 32),
			new ItemStack(Material.GRAVEL, 12),
			new ItemStack(Material.OAK_LOG, 4),
			new ItemStack(Material.ACACIA_LEAVES),
			new ItemStack(Material.SPRUCE_LEAVES),
			new ItemStack(Material.JUNGLE_LEAVES),
			new ItemStack(Material.BIRCH_LEAVES),
			new ItemStack(Material.DARK_OAK_LEAVES),
			new ItemStack(Material.OAK_LEAVES),
			new ItemStack(Material.GLASS, 8),
			new ItemStack(Material.SANDSTONE, 4),
			new ItemStack(Material.WHITE_BED),
			new ItemStack(Material.WHITE_WOOL),
			new ItemStack(Material.BLACK_WOOL),
			new ItemStack(Material.COBBLESTONE_SLAB),
			new ItemStack(Material.TORCH, 16),
			new ItemStack(Material.CHEST, 4),
			new ItemStack(Material.CRAFTING_TABLE, 8),
			new ItemStack(Material.FURNACE, 4),
			new ItemStack(Material.LADDER, 12),
			new ItemStack(Material.STONE_AXE),
			new ItemStack(Material.STONE_PICKAXE),
			new ItemStack(Material.STONE_SHOVEL),
			new ItemStack(Material.STONE_HOE),
			new ItemStack(Material.IRON_INGOT, 4)
		)
    );
    		
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("shuffle").setExecutor((CommandExecutor)new CommandExe(this));
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
    
    public void timer(double seconds, Plugin p){
		if (inGame)
		{
				new BukkitRunnable() {
					double time = seconds;
						public void run() {
		            		if (time <= 0.00 && inGame)
			                {
		            			for (Player p : inProg)
		                		{
		                			Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append(p.getDisplayName()).append(ChatColor.WHITE).append(" didn't find their item in time!").toString());
		                			p.setGameMode(GameMode.SPECTATOR);
		                			inProg.remove(p);
		                			if (inGame == true) nextGame(placehold);
		                			cancel();
		                		}
			                }
		            		else
		            		{
		            			for (final Player player : Bukkit.getOnlinePlayers())
				                {
				                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + ChatColor.BOLD.toString() + df.format(time)));
				                }
		            		}
			                time -= .05;
						
						}
					}.runTaskTimer(p, 1L, 1L);
		}
	}
    
    public void nextGame(final Player p) {
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
    	if (round == 2)
    	{
    		items.add(new ItemStack(Material.COAL_BLOCK));
    		items.add(new ItemStack(Material.ACACIA_LOG, 4));
			items.add(new ItemStack(Material.SPRUCE_LOG, 4));
			items.add(new ItemStack(Material.JUNGLE_LOG, 4));
			items.add(new ItemStack(Material.BIRCH_LOG, 4));
			items.add(new ItemStack(Material.DARK_OAK_LOG, 4));
    	}
    	if (round == 3)
    	{
    		items.add(new ItemStack(Material.IRON_BLOCK));
    		items.add(new ItemStack(Material.TNT));
    		items.add(new ItemStack(Material.ACACIA_SAPLING));
    		items.add(new ItemStack(Material.SPRUCE_SAPLING));
    		items.add(new ItemStack(Material.JUNGLE_SAPLING));
    		items.add(new ItemStack(Material.BIRCH_SAPLING));
    		items.add(new ItemStack(Material.DARK_OAK_SAPLING));
    		items.add(new ItemStack(Material.OAK_SAPLING));
    		items.add(new ItemStack(Material.IRON_CHESTPLATE));
    		items.add(new ItemStack(Material.IRON_HELMET));
    		items.add(new ItemStack(Material.IRON_LEGGINGS));
    		items.add(new ItemStack(Material.IRON_BOOTS));
    		items.add(new ItemStack(Material.BROWN_WOOL));
    	}
    	if (round == 4)
    	{
    		items.add(new ItemStack(Material.GOLD_INGOT, 2));
    		items.add(new ItemStack(Material.RED_BED));
    		items.add(new ItemStack(Material.YELLOW_BED));
    		items.add(new ItemStack(Material.BLUE_BED));
    		items.add(new ItemStack(Material.WATER_BUCKET));
    		items.add(new ItemStack(Material.HAY_BLOCK));
    		items.add(new ItemStack(Material.LAVA_BUCKET));
    	}
    	if (round == 5)
    	{
    		items.add(new ItemStack(Material.EMERALD));
    		items.add(new ItemStack(Material.DIAMOND));
    		items.add(new ItemStack(Material.NETHERRACK));
    		items.add(new ItemStack(Material.QUARTZ));
    	}
    	if (round == 6)
    	{
    		items.add(new ItemStack(Material.DIAMOND_AXE));
    		items.add(new ItemStack(Material.DIAMOND_PICKAXE));
    		items.add(new ItemStack(Material.DIAMOND_HOE));
    		items.add(new ItemStack(Material.DIAMOND_SHOVEL));
    	}
    	if (round == 7)
    	{
    		items.add(new ItemStack(Material.DIAMOND_HELMET));
    		items.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
    		items.add(new ItemStack(Material.DIAMOND_LEGGINGS));
    		items.add(new ItemStack(Material.DIAMOND_BOOTS));
    	}
    	if (round == 15)
    	{
    		items.remove(new ItemStack(Material.GRANITE));
    		items.remove(new ItemStack(Material.DIORITE));
    		items.remove(new ItemStack(Material.ANDESITE));
    		items.remove(new ItemStack(Material.DIRT, 32));
    		items.remove(new ItemStack(Material.COBBLESTONE, 16));
    		items.remove(new ItemStack(Material.SAND, 32));
    		items.remove(new ItemStack(Material.GRAVEL, 12));
    		items.remove(new ItemStack(Material.OAK_LOG, 4));
    		items.remove(new ItemStack(Material.ACACIA_LEAVES));
    		items.remove(new ItemStack(Material.SPRUCE_LEAVES));
    		items.remove(new ItemStack(Material.JUNGLE_LEAVES));
    		items.remove(new ItemStack(Material.BIRCH_LEAVES));
    		items.remove(new ItemStack(Material.DARK_OAK_LEAVES));
    		items.remove(new ItemStack(Material.OAK_LEAVES));
    		items.remove(new ItemStack(Material.GLASS, 8));
    		items.remove(new ItemStack(Material.SANDSTONE, 4));
    		items.remove(new ItemStack(Material.WHITE_BED));
    		items.remove(new ItemStack(Material.WHITE_WOOL));
    		items.remove(new ItemStack(Material.BLACK_WOOL));
    		items.remove(new ItemStack(Material.COBBLESTONE_SLAB));
    		items.remove(new ItemStack(Material.TORCH, 16));
    		items.remove(new ItemStack(Material.CHEST, 4));
    		items.remove(new ItemStack(Material.CRAFTING_TABLE, 8));
    		items.remove(new ItemStack(Material.FURNACE, 4));
    		items.remove(new ItemStack(Material.LADDER, 12));
    		items.remove(new ItemStack(Material.STONE_AXE));
    		items.remove(new ItemStack(Material.STONE_PICKAXE));
    		items.remove(new ItemStack(Material.STONE_SHOVEL));
    		items.remove(new ItemStack(Material.STONE_HOE));
    		items.remove(new ItemStack(Material.IRON_INGOT, 4));
    	}
    	inGame = true;
    	inProg.clear();
    	done.clear();
    	a = 0;
        if (inGame) 
        {
        	this.getServer().getScheduler().cancelTasks((Plugin)this);
            p.getLocation().getWorld().setTime(1000);
            for(Player play : this.getServer().getOnlinePlayers()) {    
            	if (round == 1)
            	{
            		play.setGameMode(GameMode.SURVIVAL);
                	play.setHealth(20.0);
                    play.setFoodLevel(20);
                    play.getInventory().clear();
                    play.setBedSpawnLocation(play.getLocation());
            	}
            	item = ThreadLocalRandom.current().nextInt(0, items.size() - 1);
        		itemName = items.get(item).getType().toString();
        		if (itemName.toLowerCase().contains("leaves"))
        		{
        			finName = "Leave";
        			finName = items.get(item).getAmount() + " " + finName;
        		}
        		else if (itemName.toLowerCase().contains("log"))
        		{
        			finName = "Log";
        			finName = items.get(item).getAmount() + " " + finName;
        		}
        		else finName = WordUtils.capitalizeFully(items.get(item).getAmount() + " " + itemName.replace("_", " "));
        		if (items.get(item).getAmount() > 1) finName += "s";
    			get = new StringBuilder().append(ChatColor.WHITE).append("Your item is ").append(ChatColor.GREEN).append(ChatColor.BOLD).append(finName).toString();
    			if (play.getGameMode() == GameMode.SURVIVAL) playerItem.add(item);
            	if (play.getGameMode() == GameMode.SURVIVAL) inProg.add(play);
            	if (play.getGameMode() == GameMode.SURVIVAL) play.sendTitle("", get, 20, 80, 20);
            }
            Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.WHITE).append("Round ").append(ChatColor.GOLD).append(ChatColor.BOLD).append(round).toString());
            for (int i = 0; i < 7201; i++)
            {	
    			timer(360, plugin);
        		this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
	                @Override
	                public void run() {
	                	a += 1;
	                	for (Player p : inProg)
	                	{
	                		placehold = p;
	                		if (p.getInventory().getItemInMainHand() != null)
	                		{
	                				if (p.getInventory().getItemInMainHand().isSimilar(items.get(playerItem.get(inProg.indexOf(p)))) && p.getInventory().getItemInMainHand().getAmount() >= items.get(playerItem.get(inProg.indexOf(p))).getAmount())
			                		{
			                			playerItem.remove(inProg.indexOf(p));
			                			done.add(p);
			                			inProg.remove(p);
			                			Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append(p.getDisplayName()).append(ChatColor.WHITE).append(" found their item!").toString());
			                		}
	                				else if (p.getInventory().getItemInMainHand().toString().toLowerCase().contains(("log")) && items.get(playerItem.get(inProg.indexOf(p))).toString().toLowerCase().contains("log"))
	                				{
	                					playerItem.remove(inProg.indexOf(p));
			                			done.add(p);
			                			inProg.remove(p);
			                			Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append(p.getDisplayName()).append(ChatColor.WHITE).append(" found their item!").toString());
	                				}
	                				else if (p.getInventory().getItemInMainHand().toString().toLowerCase().contains(("leaves")) && items.get(playerItem.get(inProg.indexOf(p))).toString().toLowerCase().contains("leaves"))
	                				{
	                					playerItem.remove(inProg.indexOf(p));
			                			done.add(p);
			                			inProg.remove(p);
			                			Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.BOLD).append(ChatColor.GOLD).append(p.getDisplayName()).append(ChatColor.WHITE).append(" found their item!").toString());
	                				}
	                		}
	                	}
	                	if (inProg.size() == 0 && done.size() == 1)
	                    {
	                    	Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.AQUA).append(ChatColor.BOLD).append(done.get(0).getDisplayName()).append(ChatColor.WHITE).append(" has won the game!").toString());
	                    	round = 0;
	                    	winner = done.get(0);
	                    	done.get(0).getInventory().clear();
	                    	inGame = false;
	                    	done.clear(); 
	                    }
	                	if (inProg.size() == 0 && done.size() >= 1 && a < 7200)
	                	{
	                		Bukkit.broadcastMessage("Everyone found their items! Time for the next game.");
	                		done.clear();
	                		nextGame(placehold);
	                	}
	                }
	            }, i);
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
    		e.setQuitMessage(e.getPlayer().getDisplayName() + " has rage quit");
    	}	
    }
}