package me.joda.thousand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.netty.util.internal.ThreadLocalRandom;

public class thousandCommand implements Listener, CommandExecutor {

	private Main main;
	
	public thousandCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("1k"))
		{
			if (args != null)
			{
				if (args[0].equalsIgnoreCase("on"))
				{
					main.on = true;
					sender.sendMessage("Plugin enabled!");
				}
				else if (args[0].equalsIgnoreCase("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e)
	{
		if (main.on) {
			if (e.getPlayer() instanceof Player)
			{
				randomPotion(e.getPlayer());
			}
		}
	}
	
	public void randomPotion(Player p)
	{
		int rnd = ThreadLocalRandom.current().nextInt(PotionEffectType.values().length);
		p.addPotionEffect(new PotionEffect(PotionEffectType.values()[rnd], 1200, 999));
	}
}
