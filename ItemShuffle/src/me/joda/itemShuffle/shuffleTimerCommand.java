package me.joda.itemShuffle;

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

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;


public class shuffleTimerCommand extends shuffleCommand implements Listener, CommandExecutor 
{
	private static final DecimalFormat df = new DecimalFormat("00.00");
	private Main main;
	
	public shuffleTimerCommand(Main main) {
		super(main);
		this.main = main;
	}

	public void moneyTimer(double dollars, Main main, Plugin p){
		if (main.on)
		{
			for (final Player player : Bukkit.getOnlinePlayers()) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(main.color + ChatColor.BOLD.toString() + df.format(dollars)));
			Bukkit.getScheduler().runTaskTimer(p, new Runnable()
	        {
				double totalSeconds = dollars;
	            @Override
	            public void run()
	            {
					int minutes = (int) (totalSeconds / 60);
					double seconds = (int) (totalSeconds % 60) + (totalSeconds - Math.floor(totalSeconds));
        			for (final Player player : Bukkit.getOnlinePlayers())
	                {
	                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(main.color + ChatColor.BOLD.toString() + minutes + ":" + df.format(seconds)));
	                }
	                totalSeconds -= .1;
	            }
	        }, 2L, 2L);
		}
	}

}
