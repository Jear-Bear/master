package me.joda.enchant;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener
{
	public HashMap<Player, Integer> roundsWon;
	boolean pluginOn;
	int enchantval = 0;
	
	ArrayList<Player> players = new ArrayList<>();
	ArrayList<Enchantment> pickOnly = new ArrayList<>(Arrays.asList(
			Enchantment.DIG_SPEED,
			Enchantment.DURABILITY,
			Enchantment.MENDING,
			Enchantment.LOOT_BONUS_BLOCKS
	));
	ArrayList<Enchantment> bowOnly = new ArrayList<>(Arrays.asList(
			Enchantment.ARROW_FIRE,
			Enchantment.ARROW_KNOCKBACK,
			Enchantment.ARROW_DAMAGE,
			Enchantment.DURABILITY,
			Enchantment.ARROW_INFINITE
	));
	ArrayList<Enchantment> shovelOnly = new ArrayList<>(Arrays.asList(
			Enchantment.DIG_SPEED,
			Enchantment.DURABILITY,
			Enchantment.KNOCKBACK
	));
	ArrayList<Enchantment> crossbowOnly = new ArrayList<>(Arrays.asList(
			Enchantment.PIERCING,
			Enchantment.DURABILITY,
			Enchantment.QUICK_CHARGE
	));
	ArrayList<Enchantment> shieldOnly = new ArrayList<>(Arrays.asList(
			Enchantment.DURABILITY
	));
	ArrayList<Enchantment> swordOnly = new ArrayList<>(Arrays.asList(
			Enchantment.DAMAGE_ALL,
			Enchantment.KNOCKBACK,
			Enchantment.FIRE_ASPECT,
			Enchantment.DURABILITY,
			Enchantment.LOOT_BONUS_MOBS,
			Enchantment.SWEEPING_EDGE
	));
	ArrayList<Enchantment> axeOnly = new ArrayList<>(Arrays.asList(
			Enchantment.DAMAGE_ALL,
			Enchantment.KNOCKBACK,
			Enchantment.DURABILITY,
			Enchantment.DIG_SPEED,
			Enchantment.LOOT_BONUS_MOBS
	));
	ArrayList<Enchantment> chestLegsOnly = new ArrayList<>(Arrays.asList(
			Enchantment.THORNS,
			Enchantment.PROTECTION_ENVIRONMENTAL,
			Enchantment.DURABILITY
	));
	ArrayList<Enchantment> helmetOnly = new ArrayList<>(Arrays.asList(
			Enchantment.THORNS,
			Enchantment.PROTECTION_ENVIRONMENTAL,
			Enchantment.DURABILITY,
			Enchantment.OXYGEN,
			Enchantment.WATER_WORKER
	));
	ArrayList<Enchantment> bootsOnly = new ArrayList<>(Arrays.asList(
			Enchantment.THORNS,
			Enchantment.PROTECTION_ENVIRONMENTAL,
			Enchantment.DURABILITY,
			Enchantment.DEPTH_STRIDER,
			Enchantment.SOUL_SPEED,
			Enchantment.PROTECTION_FALL
	));
	ArrayList<Enchantment> hoeOnly = new ArrayList<>(Arrays.asList(
			Enchantment.DAMAGE_ALL,
			Enchantment.KNOCKBACK,
			Enchantment.LOOT_BONUS_MOBS,
			Enchantment.FIRE_ASPECT,
			Enchantment.DURABILITY
	));
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new potionCommand(this), this);
		getCommand("thanks").setExecutor(new potionCommand(this));
	}
	
	public boolean getPluginStatus()
	{
		return pluginOn;
	}
	
	public void setPluginStatus(boolean b)
	{
		pluginOn = b;
	}
}

