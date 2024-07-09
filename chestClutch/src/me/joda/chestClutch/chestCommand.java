package me.joda.chestClutch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class chestCommand implements Listener, CommandExecutor {

	private Main main;
	Location chestLoc;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("chestClutch");
	public chestCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("chest"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					else if (isInt(args[0]))
					{
						main.on = true;
						main.distance = Integer.parseInt(args[0]);
					}
					else if (!isInt(args[0]))
					{
						sender.sendMessage(ChatColor.RED + "Enter max distance from the chest! (default is 8) \n" + ChatColor.GOLD.toString() + ChatColor.BOLD + "Usage: " + ChatColor.GRAY + " (/table 9)");
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
    public void onClickBlock(PlayerInteractEvent e){
		if (main.on)
		{
			Player player = e.getPlayer();
		    if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST){
		    	this.chestLoc = e.getClickedBlock().getLocation();
		    	Chest chest = (Chest) e.getClickedBlock().getState();
		    	int inventorySize = chest.getInventory().getSize();
		    	String inventoryName = "";
		    	if (inventorySize > 9)
		    	{
		    		inventoryName = "Large Chest";
		    	}
		    	else inventoryName = "Chest";
		    	Inventory inventory = Bukkit.createInventory(null, inventorySize, inventoryName);
		    	inventory.setContents(chest.getInventory().getContents());
		    	e.setCancelled(true);
		    	if (e.getPlayer().getLocation().distance(chestLoc) < main.distance)
		    	{
		    		  player.openInventory(inventory);
		    	}
		          }
		}
	}
	
	@EventHandler
    public void onComputerCraft(PlayerMoveEvent e){
		if (main.on)
		{
			if (this.chestLoc != null)
			{
				if (e.getPlayer().getLocation().distance(chestLoc) > main.distance)
		        {
		        	e.getPlayer().closeInventory();
		        	this.chestLoc = null;
		        }
			}
		}
	}
	
	public static boolean isInt(String str) {
	  	try {
	      	@SuppressWarnings("unused")
	    	int x = Integer.parseInt(str);
	      	return true;
		} catch (NumberFormatException e) {
	    	return false;
		}
	}

}
