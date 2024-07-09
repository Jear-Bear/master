package me.joda.talentShow;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class audienceCommand implements CommandExecutor, Listener {

	public Main main;
	
	public audienceCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player && sender.isOp())
		{
			Location audience = new Location(((Player) sender).getWorld(), 143, 55, -66);
			audience.setYaw(90f);
			audience.setPitch(0f);
			if (cmd.getName().equalsIgnoreCase("fin"))
			{
				for (Player play : main.performers)
				{
					play.getInventory().clear();
					play.teleport(audience);
					play.setGameMode(GameMode.SURVIVAL);
					main.performers.remove(play);
				}
				clearStage(main.placedBlocks);
				if (!main.placedBlocks.isEmpty()) main.placedBlocks.clear();
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "You don't have permission to do this!");
		}
		return false;
	}
	
	public void clearStage(ArrayList<Block> blocks){
		int c = 0;
		for (int i = blocks.size() - 1; i >= 0; i--)
		{
			Block b = blocks.get(i);
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() 
	        {
	            @Override
	            public void run() {
	            	b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 10f);
	            	b.setType(Material.AIR);
	            }
	        }, c);
			c += 1;
		}
    }
}
