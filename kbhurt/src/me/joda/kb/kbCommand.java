package me.joda.kb;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class kbCommand implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("knockback");
	int task;
	double size = 1.0;
	DecimalFormat df = new DecimalFormat("###.###");
	
	public kbCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("kb"))
			{
				
				if (args[0].equalsIgnoreCase("start"))
				{
					main.time = 0;
					if (!main.Damage.isEmpty()) main.Damage.clear();
					if (!main.Distance.isEmpty()) main.Distance.clear();
					main.on = true;
					Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Plugin enabled!");
					for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
					for (Player p : Bukkit.getOnlinePlayers())
					{
						main.Damage.put(p, 0);
						main.Distance.put(p, 0.0);
					}
				}
				if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("off"))
				{
					main.on = false;
					Bukkit.broadcastMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Plugin disabled!");
					for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, .5f);
				}
				
			}
			
		}
		return false;
	}
	
	@EventHandler
	void hurt(EntityDamageEvent e)
	{
		if (main.on)
		{
			
			if (e.getEntity() instanceof Player)
			{
				Player play = (Player) e.getEntity();
				if (main.Damage.containsKey(play))
				{
					main.Damage.replace(play, main.Damage.get(play) + 1);
					play.sendMessage(main.Damage.get(play) + "");
					main.start = play.getLocation();
					if (main.Damage.get(play) < 7)
					{
						play.setVelocity(play.getLocation().getDirection().multiply(-1 * Math.pow(2, main.Damage.get(play))));
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						    @Override
						    public void run() {
						    	main.Distance.replace(play, main.Distance.get(play) + main.start.distance(play.getLocation()));
						    }
						}, 35);
					}
					else
					{
						for (int i = 0; i < main.Damage.get(play) - 6; i++)
						{
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							    @Override
							    public void run() {
							    	if (play.getHealth() > 0.0 && play.getHealth() < 20.0) play.setVelocity(play.getLocation().getDirection().multiply(-1 * Math.pow(2.0, 6)));
							    }
							}, main.time);
							main.time = 5*(i+1);
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						    @Override
						    public void run() {
						    	main.Distance.replace(play, main.Distance.get(play) + main.start.distance(play.getLocation()));
						    }
						}, main.Damage.get(play)*10);
					}
					play.sendMessage(main.Distance.get(play) + "");
				}
			}
		}
	}
	
	@EventHandler
    void win(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType().equals(EntityType.ENDER_DRAGON)) 
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
            	p.sendTitle("Ayyyy gg bro", "", 10, 80, 10);
            	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    @Override
				    public void run() {
				    	p.sendTitle("You got hurt like " + ChatColor.GOLD + main.Damage.get(p) + ChatColor.WHITE + " times", "", 10, 80, 10);
				    }
				}, 80L);
            	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    @Override
				    public void run() {
				    	p.sendTitle("You traveled like " + ChatColor.GOLD + df.format(main.Distance.get(p)) + ChatColor.WHITE + " blocks", "", 10, 80, 10);
				    }
				}, 160L);
            }
        }
    }

}
