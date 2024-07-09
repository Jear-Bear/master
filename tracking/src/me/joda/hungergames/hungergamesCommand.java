package me.joda.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class hungergamesCommand implements Listener, CommandExecutor {

	private Main main;
	public int time;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("hungergames");
	int task;
	double size = 1.0;
	Location spawn;
	
	public hungergamesCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("hungergames"))
			{
				main.console = sender;
				if (!main.brokeLoc.isEmpty()) main.brokeLoc.clear();
				if (!main.brokeType.isEmpty()) main.brokeType.clear();
				if (!main.placed.isEmpty()) main.placed.clear();
				if (!main.respawn.isEmpty()) main.placed.clear();
				main.on = true;
				for (Player p : Bukkit.getOnlinePlayers())
				{
					p.sendTitle(ChatColor.GOLD + "Hunger Games", "by jodabeats & eggsocks", 20, 60, 20);
					if (p.getDisplayName().equalsIgnoreCase("egg_socks"))
					{
						main.egg = p;
						spawn = new Location(p.getWorld(), 285.5, 67.5, -104.5);
						spawn.setPitch(0f);
						spawn.setYaw(0f);
						p.teleport(spawn);
						p.getInventory().setItem(6, new ItemStack(Material.RED_WOOL, 64));
						p.getInventory().setItem(7, new ItemStack(Material.RED_WOOL, 64));
						p.getInventory().setItem(8, new ItemStack(Material.COMPASS));
					}
					else if (p.getDisplayName().equalsIgnoreCase("jodabeats"))
					{
						main.joda = p;
						spawn = new Location(p.getWorld(), 276.5, 67.0, -95.5);
						spawn.setPitch(0f);
						spawn.setYaw(-90f);
						p.teleport(spawn);
						p.getInventory().setItem(6, new ItemStack(Material.LIGHT_BLUE_WOOL, 64));
						p.getInventory().setItem(7, new ItemStack(Material.LIGHT_BLUE_WOOL, 64));
						p.getInventory().setItem(8, new ItemStack(Material.COMPASS));
					}
					else if (p.getDisplayName().equalsIgnoreCase("bencar"))
					{
						main.ben = p;
						spawn = new Location(p.getWorld(), 294.5, 67.0, -95.5);
						spawn.setPitch(0f);
						spawn.setYaw(90f);
						p.teleport(spawn);
						p.getInventory().setItem(6, new ItemStack(Material.LIME_WOOL, 64));
						p.getInventory().setItem(7, new ItemStack(Material.LIME_WOOL, 64));
						p.getInventory().setItem(8, new ItemStack(Material.COMPASS));
					}
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    @Override
				    public void run() {
				    	Bukkit.getServer().dispatchCommand(sender, "count");
				    }
				}, 100L);
			}
		}
		return false;
	}

}
