package me.joda.enchant;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.netty.util.internal.ThreadLocalRandom;

public class potionCommand implements Listener, CommandExecutor {

	private Main main;
	public potionCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("thanks"))
		{
			if (args == null)
			{
				sender.sendMessage("Enter on/off after the command!");
				return false;
			}
			else if (args != null) 
			{
				if (args[0].toString().toLowerCase().contains("off"))
				{
					main.setPluginStatus(false);
					sender.sendMessage("Plugin disabled!");
				}
				if (args[0].toString().toLowerCase().contains("on")) 
				{
					main.setPluginStatus(true);
					sender.sendMessage("Plugin enabled!");
					if (args.length > 1)
					{
						main.enchantval = Integer.parseInt(args[1]);
					}
				}
			}
		}
		return false;
	}
	
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(!(e.getEntity() instanceof Player))
        {     
            LivingEntity ent = e.getEntity();
            Object mcPlayer = ((LivingEntity) ent).getKiller();
            if(mcPlayer == null) return;
            else
            {
            	if (main.getPluginStatus())
            	{
            		randomEnchant((Player) mcPlayer);
            	}
            	else if (!main.getPluginStatus()) return;
            }
        }
    }
	
	public void randomEnchant(Player play)
	{
		Player p = play;
		ArrayList <ItemStack> tools = new ArrayList<>();
    	int rnd = 0;
		for (ItemStack i : p.getInventory())
		{
			if (i != null && i.getType() != Material.AIR)
			{
				if (i.getType().toString().toLowerCase().contains("stick") || i.getType().toString().toLowerCase().contains("shovel") || i.getType().toString().toLowerCase().contains("axe") || i.getType().toString().toLowerCase().contains("sword") || i.getType().toString().toLowerCase().equals("shield") || i.getType().toString().toLowerCase().equals("bow") || i.getType().toString().toLowerCase().equals("boots") || i.getType().toString().toLowerCase().equals("leggings") || i.getType().toString().toLowerCase().equals("chestplate") || i.getType().toString().toLowerCase().equals("helmet") || i.getType().toString().toLowerCase().equals("sword"))
    			{
    				tools.add(i);
    			}
			}
		}
		
		if (tools != null)
		{
			
			Collections.shuffle(tools);
    		ItemStack item = tools.get(0);
    		for (ItemStack i : p.getInventory())
    		{
    			if (i != null && i.getType() != Material.AIR)
    			{
    				if (i.isSimilar(item))
        			{
        				if (i.getType().toString().toLowerCase().contains("pick")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.pickOnly.size());
        					Enchantment e = main.pickOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("hoe")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.hoeOnly.size());
        					Enchantment e = main.hoeOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("axe") && !i.getType().toString().toLowerCase().contains("pick")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.axeOnly.size());
        					Enchantment e = main.axeOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("sword")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.swordOnly.size());
        					Enchantment e = main.swordOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("shield")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.shieldOnly.size());
        					Enchantment e = main.shieldOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("boots")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.bootsOnly.size());
        					Enchantment e = main.bootsOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("helmet")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.helmetOnly.size());
        					Enchantment e = main.helmetOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("shovel")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.shovelOnly.size());
        					Enchantment e = main.shovelOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("chestplate") || i.getType().toString().toLowerCase().contains("leggings")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.chestLegsOnly.size());
        					Enchantment e = main.chestLegsOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().equals("crossbow")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.crossbowOnly.size());
        					Enchantment e = main.crossbowOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().equals("bow")) 
        				{
        					rnd = ThreadLocalRandom.current().nextInt(main.bowOnly.size());
        					Enchantment e = main.bowOnly.get(rnd);
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        				if (i.getType().toString().toLowerCase().contains("stick")) 
        				{
        					Enchantment e = Enchantment.KNOCKBACK;
        					if (i.containsEnchantment(e)) randomEnchant(p);
        					else i.addUnsafeEnchantment(e, main.enchantval);
        				}
        			}
    			}
    		}
		}
	}

}
