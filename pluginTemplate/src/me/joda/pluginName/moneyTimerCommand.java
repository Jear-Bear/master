package me.joda.pluginName;

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
			Bukkit.getScheduler().runTaskTimer(p, new Runnable()
	        {
				double money = dollars;

	            @Override
	            public void run()
	            {
	            	if (main.dragonIsAlive)
	            	{
	            		if (money <= 0.00)
		                {
		                	for (final Player player : Bukkit.getOnlinePlayers())
			                {
			                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + ChatColor.BOLD.toString() + "-$" + df.format(money*-1.00)));
			                }
		                }
		               
	            		else
	            		{
	            			for (final Player player : Bukkit.getOnlinePlayers())
			                {
			                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(main.color + ChatColor.BOLD.toString() + "$" + df.format(money)));
			                }
	            		}
	            	}
	            	else if (!main.dragonIsAlive)
	            	{
	            		if (money <= 0.00)
		                {
	            			for (final Player player : Bukkit.getOnlinePlayers())
			                {
			                    player.sendTitle(ChatColor.RED + ChatColor.BOLD.toString() + "-$" + df.format(money*-1.00), ChatColor.WHITE + "Oof, that's gotta hurt...", 20, 180, 20);
			                    Bukkit.getScheduler().cancelTasks(plugin);
			                    main.on = false;
			                }
		                }
		               
	            		else
	            		{
	            			for (final Player player : Bukkit.getOnlinePlayers())
			                {
			                    player.sendTitle(main.color + ChatColor.BOLD.toString() + "$" + df.format(money), ChatColor.WHITE + "Jackpot!!!", 20, 180, 20);
			                    Bukkit.getScheduler().cancelTasks(plugin);
			                    main.on = false;
			                }
	            		}
	            		
	            	}
	                money += main.speed*.1;
	            }
	        }, 2L, 2L);
		}
	}
    
    @EventHandler
    public void dragonDeath(EntityDeathEvent e)
    {
    	if (e.getEntityType() == EntityType.ENDER_DRAGON)
    	{
    		main.dragonIsAlive = false;
    	}
    }

}
