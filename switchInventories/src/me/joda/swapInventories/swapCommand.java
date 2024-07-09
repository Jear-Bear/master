package me.joda.swapInventories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;



public class swapCommand implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("swapInventories");
	String baseMessage = ChatColor.WHITE + "    /swap ";
	TextComponent message;
	double speed;
	String[][] commands = new String[][]{
		{ChatColor.YELLOW + "start", ChatColor.GREEN + "Start " + ChatColor.YELLOW.toString() + "the timer (" + ChatColor.AQUA.toString() + "/swap on" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "stop", ChatColor.RED + "Stop " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "the plugin" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " (" + ChatColor.AQUA.toString() + "/swap off" + ChatColor.YELLOW + " works too)"},
		{ChatColor.YELLOW + "help", ChatColor.YELLOW + "Pulls up this " + ChatColor.GOLD.toString() + ChatColor.UNDERLINE.toString() + "super helpful" + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " list of commands you're looking at now"}
		};
	
	public swapCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("swap"))
			{
				if (args.length > 0)
				{
					if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("stop"))
					{
						main.on = false;
						main.players.clear();
						Bukkit.getScheduler().cancelTasks(plugin);
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("start"))
					{
						main.players.clear();
						Bukkit.getScheduler().cancelTasks(plugin);
						sender.sendMessage(ChatColor.GREEN + "Plugin enabled");
						for (Player p : Bukkit.getOnlinePlayers())
						{
							main.players.add(p);
						}
						if (main.players.size() < 2) {
							main.players.clear();
							main.on = false;
							sender.sendMessage(ChatColor.RED + "Can't swap with less than two players");
						    return false;
						}
						main.on = true;
						timer(plugin);
					}
					else if (args[0].equalsIgnoreCase("help"))
					{
						sender.sendMessage(ChatColor.AQUA + "Usage (hover for info, click to run):");
						for(int i = 0; i < commands.length; i++)
						{
							message = new TextComponent("");
							message.setText(baseMessage + commands[i][0]);
							message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(commands[i][1])));
							message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/swap " + ChatColor.stripColor(commands[i][0])));
						    sender.spigot().sendMessage(message);
						}
					}
				}
			}
		}
		return false;
	}
	
	public void timer(Plugin p)
	{
		if (main.on)
		{
			new BukkitRunnable() {
				int seconds = 301;
					public void run() {
						seconds -= 1;
						if (seconds <= 5)
						{
							switch(seconds)
							{
							case 5:
								for (final Player player : Bukkit.getOnlinePlayers()) 
								{
									player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + seconds);
								}
								break;
							case 4:
								for (final Player player : Bukkit.getOnlinePlayers()) 
								{
									player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + seconds);
								}
								break;
							case 3:
								for (final Player player : Bukkit.getOnlinePlayers()) 
								{
									player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + seconds);
								}
								break;
							case 2:
								for (final Player player : Bukkit.getOnlinePlayers())
								{
									player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + seconds);
								}
								break;
							case 1:
								for (final Player player : Bukkit.getOnlinePlayers()) 
								{
									player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + seconds);
								}
								break;
							case 0:
								shuffleInventories(main.players);
								timer(p);
								cancel();
								break;
							}
							
						}
					}
				}.runTaskTimer(p, 0L, 20L);
		}
	}
	
	public void shuffleInventories(ArrayList<Player> players)
	{
		Collections.shuffle(players);
		for (int i = 0; i <= players.size() - 2; i += 2) {
		    if (players.size() - i == 3) {
		    	Player one = players.get(i);
		        ItemStack[] oneInv = one.getInventory().getContents();
		        ItemStack[] oneArmor = one.getInventory().getArmorContents();
		        
		        Player two = players.get(i + 1);
		        ItemStack[] twoInv = two.getInventory().getContents();
		        ItemStack[] twoArmor = two.getInventory().getArmorContents();
		        
		        Player three = players.get(i + 1);
		        ItemStack[] threeInv = two.getInventory().getContents();
		        ItemStack[] threeArmor = two.getInventory().getArmorContents();
		        
		        one.getInventory().setContents(twoInv);
		        one.getInventory().setArmorContents(twoArmor);
		        one.sendMessage(ChatColor.RED + "You just swapped inventories with " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + two.getDisplayName());
		        
		        two.getInventory().setContents(threeInv);
		        two.getInventory().setArmorContents(threeArmor);
		        two.sendMessage(ChatColor.RED + "You just swapped inventories with " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + three.getDisplayName());
				
				three.getInventory().setContents(oneInv);
		        three.getInventory().setArmorContents(oneArmor);
		        three.sendMessage(ChatColor.RED + "You just swapped inventories with " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + one.getDisplayName());
		    } else {
		        Player one = players.get(i);
		        ItemStack[] oneInv = one.getInventory().getContents();
		        ItemStack[] oneArmor = one.getInventory().getArmorContents();
		        
		        Player two = players.get(i + 1);
		        ItemStack[] twoInv = two.getInventory().getContents();
		        ItemStack[] twoArmor = two.getInventory().getArmorContents();
		        
		        one.getInventory().setContents(twoInv);
		        one.getInventory().setArmorContents(twoArmor);
		        one.sendMessage(ChatColor.RED + "You just swapped inventories with " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + two.getDisplayName());
		        
		        two.getInventory().setContents(oneInv);
		        two.getInventory().setArmorContents(oneArmor);
		        two.sendMessage(ChatColor.RED + "You just swapped inventories with " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + one.getDisplayName());
		    }
		}
	}

}
