package me.joda.noWaterLog;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;



public class waterLogCommand implements Listener, CommandExecutor {

	private Main main;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("noWaterLog");
	
	public waterLogCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
		}
		return false;
	}
	

	@EventHandler
	public void onPlaceWater(PlayerBucketEmptyEvent e)
	{
		if (e.getBlockClicked().getType().toString().toLowerCase().contains("leaves"))
		{
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			    @Override
			    public void run() {
			    	unWaterLog(e.getBlockClicked());
			    }
			}, 1L);
			Location l = e.getBlockClicked().getLocation();
			e.getPlayer().getWorld().getBlockAt(l.add(e.getBlockFace().getModX(), e.getBlockFace().getModY(), e.getBlockFace().getModZ())).setType(Material.WATER);
		}
	}
	
	public static void unWaterLog(Block block) {
		Block b = block;
		if(b.getBlockData() instanceof Waterlogged && b.getType().toString().toLowerCase().contains("leaves")){
			BlockData data = b.getBlockData();
			((Waterlogged) data).setWaterlogged(false);
			b.setBlockData(data);
		}
	}
	
	/*
	@EventHandler 
	public void playerLoadChunk(ChunkLoadEvent e)
	{
		for (Block b : getBlocksInRadius(e.getWorld().getHighestBlockAt(e.getChunk().getX(), e.getChunk().getZ()).getLocation(), 30))
		{
			if (b.getBlockData() instanceof Waterlogged)
			{
				unWaterLog(b);
			}
		}
	}
	
	@Nonnull
	public static Collection<Block> getBlocksInRadius(Location location, int radius) {
	    Collection<Block> blocks = new HashSet<>();

	    int playerX = location.getBlockX();
	    int playerY = location.getBlockY();
	    int playerZ = location.getBlockZ();

	    for(int x = playerX - radius; x <= playerX + radius; x++) {
	        for(int y = playerY - radius; y <= playerY + radius; y++) {
	            for(int z = playerZ - radius; z <= playerZ + radius; z++) {
	                blocks.add(location.getWorld().getBlockAt(x,y,z));
	            }
	        }
	    }
	    return blocks;
	}
	*/

}
