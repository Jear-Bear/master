package com.joda.proxhunt;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ProxCommand extends Main implements CommandExecutor, Listener{

	ItemStack[] hunterInv = new ItemStack[] {new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.COMPASS)};
    ItemStack[] netherite = new ItemStack[]{new ItemStack(Material.NETHERITE_BOOTS), new ItemStack(Material.NETHERITE_LEGGINGS), new ItemStack(Material.NETHERITE_CHESTPLATE), new ItemStack(Material.NETHERITE_HELMET)};
    ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
    ItemMeta m = sword.getItemMeta();
    
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("prox"))
		{
			sender.sendMessage("ran!");
			if (!(hunters.isEmpty())) hunters.clear();
			if (args.length == 1)
			{
				if (!(args[0].equals(sender.getName())))
				{
					hunters.add((Player) sender);
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getName() == args[0]) hunters.add(p);
						else if (!(p.getName().equals(sender.getName()))) 
						{
							runners.add(p);
							p.sendMessage("you're a runner");
						}
					}
				}
			}
			else if (args.length == 0)
			{
				hunters.add((Player) sender);
			}
			m.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
		    sword.setItemMeta(m);
		    for (Player hunt : hunters)
	    	{
		    	hunt.sendMessage("you're a hunter");
				hunt.getInventory().clear();
	    		hunt.setHealth(20.0);
	    		hunt.setFoodLevel(20);
	    		hunt.getInventory().setArmorContents(netherite);
	    		hunt.getInventory().addItem(sword);
	    		hunt.getInventory().addItem(new ItemStack(Material.COMPASS));
	    	}
		}
		return false;
	}
}
