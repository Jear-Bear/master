package me.joda.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class tracking implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("hungergames");
	
	public tracking(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	void track(PlayerMoveEvent e)
	{
		if (main.ben == null) main.ben = main.joda; //REMOVE THIS LINE AFTER DONE TESTING OR ELSE BEN WILL BE INVISIBLE
		main.ben.setCompassTarget(main.egg.getLocation());
		main.joda.setCompassTarget(main.egg.getLocation());
		Double benDist = main.egg.getLocation().distance(main.ben.getLocation());
		Double jodaDist = main.egg.getLocation().distance(main.joda.getLocation());
		if (main.ben.getItemInHand().getType() == Material.COMPASS)
		{
			main.ben.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Distance: " + ChatColor.WHITE + benDist.intValue() + " blocks"));
		}
		else if (main.ben.getItemInHand().getType() != Material.COMPASS)
		{
			main.ben.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
		}
		if (main.ben.getItemInHand().getType() == Material.COMPASS)
		{
			main.joda.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Distance: " + ChatColor.WHITE + jodaDist.intValue() + " blocks"));
		}
		else if (main.ben.getItemInHand().getType() != Material.COMPASS)
		{
			main.joda.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
		}
		if (benDist < jodaDist && main.ben.getGameMode() == GameMode.SURVIVAL)
		{
			main.egg.setCompassTarget(main.ben.getLocation());
	        main.egg.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Distance: " + ChatColor.WHITE + benDist.intValue() + " blocks"));
		}
		else if (benDist > jodaDist && main.joda.getGameMode() == GameMode.SURVIVAL)
		{
			main.egg.setCompassTarget(main.joda.getLocation());
	        main.egg.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Distance: " + ChatColor.WHITE + jodaDist.intValue() + " blocks"));
		}
	}

}
