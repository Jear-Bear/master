package me.joda.cow;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class opList
{

	private Main main;
	ArrayList<ItemStack> diamondArmour = new ArrayList<ItemStack>();
	ArrayList<ItemStack> diamondTools = new ArrayList<ItemStack>();
	ArrayList<ItemStack> opStuff = new ArrayList<ItemStack>();
	public opList(Main main) {
		this.main = main;
	}
	
	public ArrayList<ItemStack> makeList()
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		diamondArmour.add(new ItemStack(Material.DIAMOND_BOOTS));
		diamondArmour.add(new ItemStack(Material.DIAMOND_LEGGINGS));
		diamondArmour.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
		diamondArmour.add(new ItemStack(Material.DIAMOND_HELMET));
		diamondTools.add(new ItemStack(Material.DIAMOND_AXE));
		diamondTools.add(new ItemStack(Material.DIAMOND_PICKAXE));
		diamondTools.add(new ItemStack(Material.DIAMOND_SHOVEL));
		diamondTools.add(new ItemStack(Material.DIAMOND_HOE));
		diamondTools.add(new ItemStack(Material.DIAMOND_SWORD));
		opStuff.add(new ItemStack(Material.ENDER_PEARL));
		opStuff.add(new ItemStack(Material.TOTEM_OF_UNDYING));
		for (ItemStack i : diamondArmour)
		{
			Random random = new Random();
			int prot = random.nextInt(4) + 1;
			int dura = random.nextInt(3) + 1;
			i.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, prot);
			i.addEnchantment(Enchantment.DURABILITY, dura);
			if (i.getType() == Material.DIAMOND_BOOTS) i.addEnchantment(Enchantment.PROTECTION_FALL, prot);
			list.add(i);
		}
		for (ItemStack f : diamondTools)
		{
			Random random = new Random();
			int sharp = random.nextInt(5) + 1;
			int dura = random.nextInt(3) + 1;
			f.addEnchantment(Enchantment.DURABILITY, dura);
			if (f.getType() == Material.DIAMOND_AXE || f.getType() == Material.DIAMOND_SWORD) f.addEnchantment(Enchantment.DAMAGE_ALL, sharp);
			//f.addEnchantment(Enchantment.DIG_SPEED, 3);
			list.add(f);
		}
		for (ItemStack b : opStuff)
		{
			list.add(b);
		}
		return list;
	}
	
}