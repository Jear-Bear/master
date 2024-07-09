package me.joda.jean;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class jeanCommand implements Listener, CommandExecutor {

	private Main main;
	NamespacedKey key = NamespacedKey.minecraft("end/kill_dragon");
	
	public jeanCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("jean"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().contains("on"))
				{
					if (main.target != null) main.target.clear();
					if (main.hunter != null) main.hunter.clear();
					main.target.add((Player) sender);
					Player play = (Player) sender;
					play.getInventory().clear();
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (!(p.getDisplayName().toLowerCase().equals("jeantpm")))
						{
							p.getInventory().clear();
							main.hunter.add(p);
							p.getInventory().addItem(new ItemStack(Material.COMPASS));
						}
					}
					main.on = true;
					sender.sendMessage("Plugin enabled! Good luck Jean :)");
				}
				if (args[0].toLowerCase().contains("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}

	@EventHandler
	public void playerDeath(PlayerDeathEvent e)
	{
		if (main.on)
		{
			if (e.getEntity() instanceof Player)
			{
				Player p = (Player) e.getEntity();
				if (p.getDisplayName().toLowerCase().equals("jeantpm"))
				{
					AdvancementProgress progress = p.getAdvancementProgress(Bukkit.getAdvancement(key));					
					for(String criteria : progress.getRemainingCriteria()) progress.awardCriteria(criteria);
					for (Player play : Bukkit.getOnlinePlayers())
					{
						play.sendTitle(org.bukkit.ChatColor.GREEN + "Speedrunner" + org.bukkit.ChatColor.WHITE + " wins", "", 10, 80, 10);
						play.playSound(play.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void targetTrack(PlayerMoveEvent e)
	{
		if (main.hunter != null && main.target != null)
		{
			for (Player p : main.hunter)
			{
				p.setCompassTarget(main.target.get(0).getLocation());
			}
		}
	}
	
	@EventHandler
	public void hunterInv(PlayerRespawnEvent e)
	{
		if (main.hunter != null && main.target != null)
		{
			if (main.hunter.contains(e.getPlayer()))
			{
				e.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));
			}
		}
	}
}
