package me.joda.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class deathSystem implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("hungergames");
	
	public deathSystem(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
	
	@EventHandler
	void death(PlayerDeathEvent e)
	{
		if (main.on)
		{
			Player p = (Player) e.getEntity();
			p.setGameMode(GameMode.SPECTATOR);
			main.respawn.put(p, p.getLocation());
			p.getDisplayName().equalsIgnoreCase("egg_socks");
			main.on = false;
			for (Player play : Bukkit.getOnlinePlayers())
			{
				play.sendMessage(ChatColor.GOLD + "Joda and Ben" + ChatColor.WHITE + " have won this round!");
				play.playSound(play.getLocation(), Sound.ENTITY_VILLAGER_YES, 1f, 1f);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    @Override
				    public void run() {
				    	if (main.console != null)
				    	{
				    		Bukkit.getServer().dispatchCommand(main.console, "rollback");
				    	}
				    }
				}, 100L);
			}
		}
	}
	
	@EventHandler
	void respawn(PlayerRespawnEvent e)
	{
		if (main.on)
		{
			if (main.respawn.containsKey(e.getPlayer()))
			{
				e.getPlayer().teleport(main.respawn.get(e.getPlayer()));
			}
		}
	}
}
