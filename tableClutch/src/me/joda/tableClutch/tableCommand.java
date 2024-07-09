package me.joda.tableClutch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class tableCommand implements Listener, CommandExecutor {

	private Main main;
	boolean clickTable = false;
	Location craftLoc;
	String baseMessage = ChatColor.WHITE + "    /table ";
	TextComponent message;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("tableClutch");
	public tableCommand(Main main) {
		this.main = main;
	}
	String[][] commands = new String[][]{
		{ChatColor.YELLOW + "9", ChatColor.YELLOW + "Change the max distance from the " + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "crafting table" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString()},
		{ChatColor.YELLOW + "reset", ChatColor.RED + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "Reset" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " the max distance from the crafting table (" + ChatColor.AQUA.toString() + "/table off" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "help", ChatColor.YELLOW + "Pulls up this " + ChatColor.GOLD.toString() + ChatColor.UNDERLINE.toString() + "super helpful" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " list of commands you're looking at now"}
		};

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("table"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("reset"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					if (args[0].equalsIgnoreCase("help"))
					{
						sender.sendMessage(ChatColor.AQUA + "Usage (hover for info, click to run):");
						for(int i = 0; i < commands.length; i++)
						{
							message = new TextComponent("");
							message.setText(baseMessage + commands[i][0]);
							message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(commands[i][1])));
							message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/money " + ChatColor.stripColor(commands[i][0])));
						    sender.spigot().sendMessage(message);
						}
					}
					else if (isInt(args[0]))
					{
						main.on = true;
						main.distance = Integer.parseInt(args[0]);
					}
					else if (!isInt(args[0]))
					{
						sender.sendMessage(ChatColor.RED + "Enter max distance from the crafting table! (default is 8) \n" + ChatColor.GOLD.toString() + ChatColor.BOLD + "Usage: " + ChatColor.GRAY + " (/table 9)");
					}
				}
				else if (args == null)
				{
					sender.sendMessage(ChatColor.RED + "Enter max distance from the crafting table! (default is 8) \n" + ChatColor.GOLD.toString() + ChatColor.BOLD + "Usage: " + ChatColor.GRAY + " (/table 9)");
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
		    if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CRAFTING_TABLE){
		    	this.craftLoc = e.getClickedBlock().getLocation();
		    	e.setCancelled(true);
		    	if (e.getPlayer().getLocation().distance(craftLoc) < main.distance)
		    	{
		    		  player.openWorkbench(null, true);
		    	}
		          }
		}
	}
	
	@EventHandler
    public void onComputerCraft(PlayerMoveEvent e){
		if (main.on)
		{
			if (this.craftLoc != null)
			{
				if (e.getPlayer().getLocation().distance(craftLoc) > main.distance)
		        {
		        	e.getPlayer().closeInventory();
		        	this.craftLoc = null;
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
