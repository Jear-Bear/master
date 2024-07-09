package me.joda.CK;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class cursedCommand implements CommandExecutor, Listener {

	private Main main;
	
	public cursedCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().contains("cursed"))
		{
			if (args != null)
			{
				if (args[0].equals("on"))
				{
					sender.sendMessage("Minecraft is now cursed");
					main.on = true;
				}
				if (args[0].equals("off"))
				{
					sender.sendMessage("The curse has now been lifted!");
					main.on = false;
				}
			}
		}
		return false;
	}

	@EventHandler
    public void mineBlock(BlockBreakEvent e)
    {
		if (main.on)
		{
			Player p = e.getPlayer();
			if (p.getGameMode() == GameMode.SURVIVAL)
			{
				if (e.getBlock().getType() == Material.BLUE_ORCHID) 
		    	{
					e.setDropItems(false);
		    		p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND));
		    	}
				if (e.getBlock().getType() == Material.RED_TULIP) 
		    	{
					e.setDropItems(false);
					p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.REDSTONE));
		    	}
				if (e.getBlock().getType() == Material.WITHER_ROSE) 
		    	{
					e.setDropItems(false);
		    		p.getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.WITHER);
		    	}
				if (e.getBlock().getType() == Material.CORNFLOWER) 
		    	{
					e.setDropItems(false);
					p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.LAPIS_LAZULI));
		    	}
				if (e.getBlock().getType() == Material.DANDELION) 
		    	{
					e.setDropItems(false);
					p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
		    	}
				if (e.getBlock().getType() == Material.DIAMOND_ORE) 
		    	{
					e.setDropItems(false);
		    		p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.BLUE_ORCHID));
		    	}
				if (e.getBlock().getType() == Material.REDSTONE_ORE) 
		    	{
					e.setDropItems(false);
					p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.RED_TULIP));
		    	}
				if (e.getBlock().getType() == Material.COAL_ORE) 
		    	{
					e.setDropItems(false);
		    		p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.WITHER_ROSE));
		    	}
				if (e.getBlock().getType() == Material.LAPIS_ORE) 
		    	{
					e.setDropItems(false);
					p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.CORNFLOWER));
		    	}
				if (e.getBlock().getType() == Material.GOLD_ORE) 
		    	{
					e.setDropItems(false);
					p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.DANDELION));
		    	}
			}
		}
    }
}
