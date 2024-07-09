package me.joda.shift;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

public class shiftCommand implements Listener, CommandExecutor {

	private Main main;
	boolean clickTable = false;
	Location craftLoc;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("secret");
	public shiftCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("secret"))
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
						main.on = true;
						sender.sendMessage(ChatColor.GREEN + "Plugin enabled");
				        return false;
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void shift(PlayerToggleSneakEvent e)
	{
		if (main.on)
		{
			Player p = e.getPlayer();
			if (e.isSneaking())
			{
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, false, false));
				e.getPlayer().getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 20f, 3.5f);
			}
			else if (!e.isSneaking())
			{
				if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				      p.removePotionEffect(PotionEffectType.INVISIBILITY);
				 }
				e.getPlayer().getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 20f, 1f);
			}
		}
		
	}

}
