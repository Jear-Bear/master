package com.joda.assassin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class countdown
{
	BukkitTask counter1, counter2, counter3;
	public void countDownTitle(int count, Plugin plug)
	{
		Bukkit.getScheduler().runTaskTimer(plug, new Runnable()
	    {
	        int time = count;
	
	        @Override
	        public void run()
	        {
	            for (Player p : Bukkit.getOnlinePlayers())
	            {
	            	p.sendTitle(time + "", "", 0, 20, 0);
					p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
	            }
	           
	            this.time--;
	        }
	    }, 0L, 20L);
	}
	
}