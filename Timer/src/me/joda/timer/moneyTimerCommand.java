package me.joda.timer;


import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;


public class moneyTimerCommand extends moneyCommand implements Listener, CommandExecutor 
{
	private Main main;
	boolean isStill = false;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	public moneyTimerCommand(Main main) {
		super(main);
		this.main = main;
	}

	public void moneyTimer(double time, Main main, Plugin p){
		if (main.on)
		{
			Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
			Team team = board.registerNewTeam("runner");
			team.setColor(ChatColor.RED);
			team.addEntry(main.runner.getName());
			main.runner.setGlowing(true);
			new BukkitRunnable() {
				double seconds = time*60.0;
				Location runnerLoc = main.runner.getLocation();
					public void run() {
						if (main.timerRunning)
		            	{
							int minutes = (int) (((seconds - (seconds % 60)) / 60));
							int hours = (int) ((minutes - (minutes % 60)) / 60);
							int mins = (int) (((seconds - (seconds % 60)) / 60) - hours * 60);
							double secs = seconds % 60;
							if (hours == 0)
							{
								for (final Player player : Bukkit.getOnlinePlayers())
				                {
				                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + ChatColor.BOLD.toString() + mins + ":" + df.format(secs)));
				                }
							}
							else
							{
								for (final Player player : Bukkit.getOnlinePlayers())
								{
				                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + ChatColor.BOLD.toString() + hours + ":" + mins + ":" + df.format(secs)));
				                }
							}
			                
		            	}
						if (seconds == 0 || main.on == false)
						{
							main.runner.setGlowing(false);
							board.getTeam("runner").unregister();
							this.cancel();
						}
						if (main.runner.getLocation().getX() == runnerLoc.getX() && main.runner.getLocation().getY() == runnerLoc.getY() && main.runner.getLocation().getZ() == runnerLoc.getZ())
						{
							seconds -= main.speed;
							team.setColor(ChatColor.GREEN);
						}
						else 
						{
							seconds -= 0;
							team.setColor(ChatColor.RED);
						}
						runnerLoc = main.runner.getLocation();
					}
				}.runTaskTimer(p, 2L, 2L);
		}
	}
    
}
