package me.joda.tree;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class treeCommand implements Listener, CommandExecutor {

	private Main main;
	Location chestLoc;
	HashMap<Location, Integer> Saplings = new HashMap<Location, Integer>();
	ItemStack bonemeal = new ItemStack(Material.BONE_MEAL, 1);
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("tree");
	public treeCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("tree"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					else if (args[0].equalsIgnoreCase("on"))
					{
						this.Saplings.clear();
						main.on = true;
						sender.sendMessage(ChatColor.GREEN + "Plugin enabled");
				        return false;
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "That isn't a recognized command!");
				        return false;
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
    public void killSalmon(EntityDeathEvent e){
		if (main.on)
		{
			if (e.getEntity().getType() == EntityType.SALMON)
			{
				if (!e.getDrops().contains(bonemeal))
				{
					e.getDrops().add(bonemeal);
				}
			}
		}
	}
	


	@EventHandler
	public void growTree(PlayerInteractEvent e)
	{
		if (e.getClickedBlock() != null)
		{
			if (main.on)
			{
				Location l = e.getClickedBlock().getLocation();
				@SuppressWarnings("deprecation")
				boolean bone = e.getPlayer().getInventory().getItemInOffHand().getType() == Material.BONE_MEAL || e.getPlayer().getInventory().getItemInHand().getType() == Material.BONE_MEAL;
				if (e.getClickedBlock().getType() == Material.OAK_SAPLING && e.getAction() == Action.RIGHT_CLICK_BLOCK && bone)
				{
					if (this.Saplings.containsKey(l))
					{
						this.Saplings.replace(l, this.Saplings.get(l) + 1);
						if (this.Saplings.get(l) == 2)
						{
							e.getClickedBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().add(1, 0, 1).getBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().add(1, 0, -1).getBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().add(1, 0, 0).getBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().add(-1, 0, 1).getBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().add(-1, 0, -1).getBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().add(-1, 0, 0).getBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().add(0, 0, 1).getBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().add(0, 0, -1).getBlock().setType(Material.AIR);
							e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock().setType(Material.AIR);
							e.getPlayer().getWorld().generateTree(e.getClickedBlock().getLocation().subtract(0, 1, 0), TreeType.TREE);
							e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock().setType(Material.DIRT);
						}
					}
					else if (!this.Saplings.containsKey(l))
					{
						this.Saplings.put(l, 1);
					}
					
				}
			}
		}
	}
	
	@EventHandler
	public void grownTree(StructureGrowEvent e)
	{
		Bukkit.broadcastMessage("WOWOWOWOW");
	}
	

}
