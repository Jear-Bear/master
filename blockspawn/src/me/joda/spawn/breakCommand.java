package me.joda.spawn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class breakCommand implements Listener, CommandExecutor {

	private Main main;
	Random rand = new Random();
	ArrayList<EntityType> mobs = new ArrayList<>(Arrays.asList(
			EntityType.BAT,	
			EntityType.BEE,
			EntityType.BLAZE,	
			EntityType.CAT,
			EntityType.CAVE_SPIDER,	
			EntityType.CHICKEN,	
			EntityType.COD,
			EntityType.COW,	
			EntityType.CREEPER,
			EntityType.DOLPHIN,
			EntityType.DONKEY,
			EntityType.DROWNED,
			EntityType.EGG,
			EntityType.ELDER_GUARDIAN,
			EntityType.ENDER_DRAGON,
			EntityType.ENDERMAN,
			EntityType.ENDERMITE,
			EntityType.EVOKER,	
			EntityType.FOX,
			EntityType.GHAST,
			EntityType.GIANT,
			EntityType.GUARDIAN,
			EntityType.HOGLIN,
			EntityType.HORSE,
			EntityType.HUSK,
			EntityType.ILLUSIONER,
			EntityType.IRON_GOLEM,
			EntityType.LLAMA,
			EntityType.MAGMA_CUBE,
			EntityType.MULE,
			EntityType.MUSHROOM_COW,
			EntityType.OCELOT,
			EntityType.PANDA,
			EntityType.PARROT,
			EntityType.PHANTOM,
			EntityType.PIG,
			EntityType.PIGLIN,
			EntityType.PIGLIN_BRUTE,
			EntityType.PILLAGER,
			EntityType.POLAR_BEAR,
			EntityType.PUFFERFISH,
			EntityType.RABBIT,
			EntityType.RAVAGER,
			EntityType.SALMON,
			EntityType.SHEEP,
			EntityType.SHULKER,
			EntityType.SILVERFISH,
			EntityType.SKELETON,
			EntityType.SKELETON_HORSE,
			EntityType.SLIME,
			EntityType.SNOWMAN,
			EntityType.SPIDER,
			EntityType.SQUID,
			EntityType.STRAY,
			EntityType.STRIDER,
			EntityType.TRADER_LLAMA,
			EntityType.TROPICAL_FISH,
			EntityType.TURTLE,
			EntityType.VEX,
			EntityType.VILLAGER,
			EntityType.VINDICATOR,
			EntityType.WANDERING_TRADER,
			EntityType.WITHER,
			EntityType.WITHER_SKELETON,
			EntityType.WOLF,
			EntityType.ZOGLIN,
			EntityType.ZOMBIE,
			EntityType.ZOMBIE_HORSE,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.ZOMBIFIED_PIGLIN));
	
	public breakCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("break"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().equals("on"))
				{
					main.on = true;
					Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Plugin enabled!");
					for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
				}
				if (args[0].toLowerCase().equals("off"))
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
	public void breakBlock(BlockBreakEvent e)
	{
		if (main.on)
		{
			int choice = rand.nextInt(101);
			Bukkit.getServer().getScheduler().runTaskLater(main, (Runnable)new Runnable() {
				@Override
                public void run() {
					e.getPlayer().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.ENDER_DRAGON);//mobs.get(choice));
    			}
    		}, 1);
		}
	}
}
