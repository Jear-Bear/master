package me.joda.sheep;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener
{
	public HashMap<Player, Integer> roundsWon;
	boolean pluginOn;
	int enchantval = 0;
	
	ArrayList<ItemStack> OP = new ArrayList<>(Arrays.asList(
			
			));
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new sheepCommand(this), this);
		getCommand("sheep").setExecutor(new sheepCommand(this));
	}
	
	public void makeList()
	{
		if (OP != null) OP.clear();
		ItemStack i = new ItemStack(Material.BOW);
		OP.add(i);
		i = new ItemStack(Material.DIAMOND_BLOCK, 4);
		OP.add(i);
		i = new ItemStack(Material.NETHERITE_BLOCK, 4);
		OP.add(i);
		i = new ItemStack(Material.GOLD_BLOCK, 4);
		OP.add(i);
		i = new ItemStack(Material.ENDER_PEARL, 8);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2);
		OP.add(i);
		i = new ItemStack(Material.ARROW, 64);
		OP.add(i);
		i = new ItemStack(Material.IRON_BLOCK, 8);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.DURABILITY, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.ARROW_KNOCKBACK, 20, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.ARROW_DAMAGE, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.ARROW_INFINITE, 1, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.ARROW_KNOCKBACK, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.ARROW_FIRE, 1, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.PROTECTION_FALL, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.KNOCKBACK, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.LOOT_BONUS_BLOCKS, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.LOOT_BONUS_MOBS, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.DAMAGE_ALL, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
		i = new ItemStack(Material.ENCHANTED_BOOK);
		meta = (EnchantmentStorageMeta)i.getItemMeta();
		meta.addStoredEnchant(Enchantment.DIG_SPEED, 10, true);
		i.setItemMeta(meta);
		OP.add(i);
	}
	
}

