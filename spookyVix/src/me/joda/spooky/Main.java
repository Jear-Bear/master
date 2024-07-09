package me.joda.spooky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	Map<Player, Integer> PlayerScore = new HashMap<>();
	Map<Player, ItemStack> PlayerChoice = new HashMap<>();
	Map<Material, Integer> prices = new HashMap<>();
	ItemStack clock = new ItemStack(Material.CLOCK);
	ArrayList<String> lore = new ArrayList<String>();
	
	ItemStack jumpscare = new ItemStack(Material.SKELETON_SKULL);
	ItemStack spawnCreeper = new ItemStack(Material.CREEPER_SPAWN_EGG);
	ItemStack giveBlindness = new ItemStack(Material.SPYGLASS);

	ItemStack spawnWarden = new ItemStack(Material.SCULK_SENSOR);
	ItemStack teleportUp = new ItemStack(Material.ENDER_PEARL);
	ItemStack giveSlowness = new ItemStack(Material.COBWEB);

	ItemStack spawnGhast = new ItemStack(Material.GHAST_SPAWN_EGG);
	ItemStack givePoison = new ItemStack(Material.SPIDER_EYE);
	ItemStack giveGlowing = new ItemStack(Material.BLAZE_POWDER);

	ItemStack turnInvisible = new ItemStack(Material.POTION);
	ItemStack spawnTNT = new ItemStack(Material.TNT);
	ItemStack scaryNoise = new ItemStack(Material.MUSIC_DISC_11);
	
	
	
	@Override
	public void onEnable()
	{
		ItemMeta meta = jumpscare.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Jumpscare someone");
		lore.add(ChatColor.WHITE + "Cost: 100 Tokens");
		lore.add(ChatColor.GOLD+ "Jumpscare a player of your choice!");
		meta.setLore(lore);	
		jumpscare.setItemMeta(meta);
		lore.clear();
	
		
		meta = spawnCreeper.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Spawn a Creeper");
		lore.add(ChatColor.WHITE + "Cost: 40 Tokens");
		lore.add(ChatColor.GOLD + "Spawn a Creeper near a player of your choice!");
		meta.setLore(lore);
		spawnCreeper.setItemMeta(meta);
		lore.clear();
		
		meta = giveBlindness.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Give someone blindness");
		lore.add(ChatColor.WHITE + "Cost: 25 Tokens");
		lore.add(ChatColor.GOLD + "Give blindness to a player of your choice for 30 seconds!");
		meta.setLore(lore);
		giveBlindness.setItemMeta(meta);
		lore.clear();

		meta = spawnWarden.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Spawn Warden");
		lore.add(ChatColor.WHITE + "Cost: 75 Tokens");
		lore.add(ChatColor.GOLD + "Spawn a Warden near a player of your choice!");
		meta.setLore(lore);
		spawnWarden.setItemMeta(meta);
		lore.clear();
		
		meta = teleportUp.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Teleport");
		lore.add(ChatColor.WHITE + "Cost: 30 Tokens");
		lore.add(ChatColor.GOLD + "Teleport a player of your choice 10 blocks up!");
		meta.setLore(lore);
		teleportUp.setItemMeta(meta);
		lore.clear();
		
		meta = giveSlowness.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Give someone slowness");
		lore.add(ChatColor.WHITE + "Cost: 20 Tokens");
		lore.add(ChatColor.GOLD + "Give slowness to a player of your choice for 10 seconds!");
		meta.setLore(lore);
		giveSlowness.setItemMeta(meta);
		lore.clear();

		meta = spawnGhast.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Spawn a Ghast");
		lore.add(ChatColor.WHITE + "Cost: 60 Tokens");
		lore.add(ChatColor.GOLD + "Spawn a Ghast near a player of your choice!");
		meta.setLore(lore);
		spawnGhast.setItemMeta(meta);
		lore.clear();
		
		meta = givePoison.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Give someone poision");
		lore.add(ChatColor.WHITE + "Cost: 25 Tokens");
		lore.add(ChatColor.GOLD + "Give a player of your choice poison for 10 seconds!");
		meta.setLore(lore);
		givePoison.setItemMeta(meta);
		lore.clear();
		
		meta = giveGlowing.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Give someone glowing");
		lore.add(ChatColor.WHITE + "Cost: 10 Tokens");
		lore.add(ChatColor.GOLD + "Give a player of your choice glowing for 15 seconds!");
		meta.setLore(lore);
		giveGlowing.setItemMeta(meta);
		lore.clear();

		meta = turnInvisible.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Turn Invisible");
		lore.add(ChatColor.WHITE + "Cost: 50 Tokens");
		lore.add(ChatColor.GOLD + "Turn Invisible for 30 seconds!");
		meta.setLore(lore);
		turnInvisible.setItemMeta(meta);
		lore.clear();
		
		meta = spawnTNT.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Spawn TNT");
		lore.add(ChatColor.WHITE + "Cost: 25 Tokens");
		lore.add(ChatColor.GOLD + "Spawn TNT near a player of your choice!");
		meta.setLore(lore);
		spawnTNT.setItemMeta(meta);
		lore.clear();
		
		meta = scaryNoise.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Scary noise");
		lore.add(ChatColor.WHITE + "Cost: 5 Tokens");
		lore.add(ChatColor.GOLD + "Play a scary noise for a player of your choice!");
		meta.setLore(lore);
		scaryNoise.setItemMeta(meta);
		lore.clear();
		
		meta = clock.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Magic Clock");
		lore.add(ChatColor.WHITE + "Right click me!");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		clock.setItemMeta(meta);
		clock.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		
		prices.put(Material.SKELETON_SKULL, 100);
		prices.put(Material.CREEPER_SPAWN_EGG, 40);
		prices.put(Material.SPYGLASS, 25);

		prices.put(Material.SCULK_SENSOR, 75);
		prices.put(Material.ENDER_PEARL, 30);
		prices.put(Material.COBWEB, 20);

		prices.put(Material.GHAST_SPAWN_EGG, 60);
		prices.put(Material.SPIDER_EYE, 25);
		prices.put(Material.BLAZE_POWDER, 10);

		prices.put(Material.POTION, 50);
		prices.put(Material.TNT, 25);
		prices.put(Material.MUSIC_DISC_11, 5);
		
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new spookyCommand(this), this);
		getCommand("spooky").setExecutor(new spookyCommand(this));
		getCommand("spooky").setTabCompleter(new OnTabComplete());
	}
	
	private Main instance;
	public Main getInstance(){
	    return instance;
	}
	
	@EventHandler
    public void on (PlayerCommandSendEvent e)
    {
        e.getCommands().removeIf(command -> command.contains(":"));
    }
}
