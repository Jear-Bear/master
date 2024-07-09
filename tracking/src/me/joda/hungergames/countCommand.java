package me.joda.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class countCommand implements Listener, CommandExecutor {

	private Main main;
	public int time;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("hungergames");
	double size = 1.0;
	
	public countCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("count"))
			{
				main.pvp = false;
				main.value = 5;
				Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				    @Override
				    public void run() {
				    	if (main.value == 0)
				    	{
				    		for (Player p : Bukkit.getOnlinePlayers())
							{
					    		p.sendTitle(ChatColor.RED + "GO", "", 0, 20, 0);
					    		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
							}
				    	}
				    	if (main.value > 0)
				    	{
				    		if (main.value >= 4)
				    		{
				    			for (Player p : Bukkit.getOnlinePlayers())
								{
						    		p.sendTitle(ChatColor.BLUE.toString() + main.value + "", "", 0, 20, 0);
						    		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
								}
				    		}
				    		else if (main.value > 0)
				    		{
				    			for (Player p : Bukkit.getOnlinePlayers())
								{
						    		p.sendTitle(ChatColor.GREEN.toString() + main.value + "", "", 0, 20, 0);
						    		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
								}
				    		}
				    		
				    	}
				    	main.value -= 1;
				    }
				}, 0L, 20L); //0 Tick initial delay, 20 Tick (1 Second) between repeats
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    @Override
				    public void run() {
				    	main.pvp = true;
				    	for (Player p : Bukkit.getOnlinePlayers())
						{
				    		p.sendTitle(ChatColor.RED + "Pvp is now enabled!", "", 10, 60, 10);
				    		p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_STING, 1f, 1f);
						}
				    }
				}, 700L);
			}
		}
		return false;
	}
	
	@EventHandler
	void noPvP(EntityDamageByEntityEvent e)
	{
		if (!main.pvp) e.setCancelled(true);
	}
}
