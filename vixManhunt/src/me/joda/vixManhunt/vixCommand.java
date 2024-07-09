package me.joda.vixManhunt;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.minecraft.server.v1_16_R3.Entity;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;



public class vixCommand implements Listener, CommandExecutor {

	private Main main;
	Random rand = new Random();
	ItemStack ender = new ItemStack(Material.ENDER_PEARL);
	ItemStack blaze = new ItemStack(Material.BLAZE_ROD);
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("vixManhunt");
	String baseMessage = ChatColor.WHITE + "    /vix ";
	TextComponent message;
	double speed;
	String[][] commands = new String[][]{
		{ChatColor.YELLOW + "on", ChatColor.GREEN + "Enable " + ChatColor.YELLOW.toString() + "the plugin"},
		{ChatColor.YELLOW + "off", ChatColor.RED + "Disable " + ChatColor.RESET.toString() + ChatColor.YELLOW.toString() + " the plugin"}
		};
	
	public vixCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("vix"))
			{
				if (args.length > 0)
				{
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					else if (args[0].equalsIgnoreCase("on"))
					{
						main.on = true;
						killBrutes((Player) sender);
						sender.sendMessage(ChatColor.GREEN + "Plugin enabled");
					}
					else if (args[0].equalsIgnoreCase("help"))
					{
						sender.sendMessage(ChatColor.AQUA + "Usage (hover for info, click to run):");
						for(int i = 0; i < commands.length; i++)
						{
							message = new TextComponent("");
							message.setText(baseMessage + commands[i][0]);
							message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(commands[i][1])));
							message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vix " + ChatColor.stripColor(commands[i][0])));
						    sender.spigot().sendMessage(message);
						}
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void betterTrades(PiglinBarterEvent e)
	{
		if (main.on)
		{
			int random = rand.nextInt(101);
			boolean isPearl = false;
			if (random <= 16)
			{
				for (ItemStack i : e.getOutcome())
				{
					if (i.getType() == Material.ENDER_PEARL)
					{
						isPearl = true;
					}
				}
				if (!isPearl)
				{
					random = rand.nextInt(3) + 2;
					e.getOutcome().add(new ItemStack(Material.ENDER_PEARL, random));
				}
			}
		}
	}
	
	
	public void killBrutes(Player sender)
	{
		new BukkitRunnable() {
		    @Override
		    public void run() {
		    	if (main.on)
		    	{
		    		for (org.bukkit.entity.Entity entity : sender.getNearbyEntities(100, 100, 100))
		    		{
		    			if (entity.getType() == EntityType.PIGLIN_BRUTE)
		    			{
		    				entity.teleport(entity.getLocation().subtract(0, 200, 0));
		    			}
		    		}
		    	}
		    	else if (!main.on)
		    	{
		    		cancel();
		    	}
		    }
		}.runTaskTimer(plugin, 0L, 600L);
	}
	
	//gets rid of piglin brutes
	@EventHandler
	public void noBrutes(EntitySpawnEvent e)
	{
		if (main.on)
		{
			if (e.getEntityType() == EntityType.PIGLIN_BRUTE)
			{
				e.setCancelled(true);
			}
		}
	}
	
	//increases drops from endermen and blaze to 85%
	@EventHandler
	public void betterDrops(EntityDeathEvent e)
	{
		if (main.on)
		{
			int random = rand.nextInt(101);
			if (random <= 81)
			{
				if (e.getEntityType() == EntityType.ENDERMAN)
				{
					boolean eyes = false;
					for (ItemStack i : e.getDrops())
					{
						if (i.getType() == Material.ENDER_PEARL)
						{
							eyes = true;
						}
					}
					if (!eyes)
					{
						e.getDrops().add(ender);
					}
				}
				if (e.getEntityType() == EntityType.BLAZE)
				{
					boolean rod = false;
					for (ItemStack i : e.getDrops())
					{
						if (i.getType() == Material.ENDER_PEARL)
						{
							rod = true;
						}
					}
					if (!rod)
					{
						e.getDrops().add(blaze);
					}
				}
			}
			else
			{
				for (ItemStack i : e.getDrops())
				{
					if (i.getType() == Material.ENDER_PEARL || i.getType() == Material.BLAZE_ROD)
					{
						e.getDrops().remove(i);
					}
				}
			}
		}
	}

}
