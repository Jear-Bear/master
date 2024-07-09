package me.joda.moneyTimer;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;


public class moneyTimerCommand extends moneyCommand implements Listener, CommandExecutor 
{
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private Main main;
	
	public moneyTimerCommand(Main main) {
		super(main);
		this.main = main;
	}

	public void moneyTimer(double dollars, Main main, Plugin p){
		if (main.on)
		{
			for (final Player player : Bukkit.getOnlinePlayers()) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(main.color + ChatColor.BOLD.toString() + "$" + df.format(dollars)));
				new BukkitRunnable() {
					double money = dollars;
						public void run() {
							if (main.timerRunning)
			            	{
			            		if (money <= 0.00)
				                {
				                	for (final Player player : Bukkit.getOnlinePlayers())
					                {
					                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + ChatColor.BOLD.toString() + "-" + main.currency + df.format(money*-1.00)));
					                }
				                }
				               
			            		else
			            		{
			            			for (final Player player : Bukkit.getOnlinePlayers())
					                {
					                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(main.color + ChatColor.BOLD.toString() + main.currency + df.format(money)));
					                }
			            		}
			            	}
			            	else if (!main.timerRunning)
			            	{
			            		showTitleCard(money, p);
			            		cancel();
			            	}
			                money += main.speed*.1;
						
						}
					}.runTaskTimer(p, 2L, 2L);
		}
	}
    
	public void showTitleCard(double money, Plugin plugin)
	{
		if (money <= 0.00)
        {
			for (final Player player : Bukkit.getOnlinePlayers())
            {
                player.sendTitle(ChatColor.RED + ChatColor.BOLD.toString() + "-" + main.currency + df.format(money*-1.00), ChatColor.WHITE + "Oof, that's gotta hurt...", 20, 180, 20);
                Bukkit.getScheduler().cancelTasks(plugin);
                main.on = false;
            }
        }
       
		else
		{
			for (final Player player : Bukkit.getOnlinePlayers())
            {
                player.sendTitle(main.color + ChatColor.BOLD.toString() + main.currency + df.format(money), ChatColor.WHITE + "Jackpot!!!", 20, 180, 20);
                Bukkit.getScheduler().cancelTasks(plugin);
                main.on = false;
            }
		}
	}

}
