package me.joda.cow;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Random;

import javax.swing.text.html.parser.Entity;

public class cowCommand implements Listener, CommandExecutor {

	private Main main;
	ArrayList<ItemStack> OP = new ArrayList<>();
	
	public cowCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toString().toLowerCase().contains("cow"))
		{
			if (args != null)
			{
				if (args[0].toLowerCase().contains("on"))
				{
					main.on = true;
					sender.sendMessage("Plugin enabled!");
				}
				else if (args[0].toLowerCase().contains("off"))
				{
					main.on = false;
					sender.sendMessage("Plugin disabled!");
				}
			}
		}
		return false;
	}

	@EventHandler
	public void cowDeath(EntityDeathEvent e)
	{
		if (main.on)
		{
			OP = main.makeList();
			int i = OP.size();
			Random random = new Random();
			if (e.getEntityType() == EntityType.COW)
			{
				int choice = random.nextInt(i - 1) + 1;
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), OP.get(choice));
			}
		}
	}
	
	@EventHandler
    public void CowMilk(PlayerInteractEntityEvent e) {
		if (main.on)
		{
			OP = main.makeList();
			int i = OP.size();
	        Player p = (Player) e.getPlayer();
	        Random random = new Random();
	        if(p.getInventory().getItemInHand().getType().equals(Material.BUCKET) && e.getRightClicked().getType() == EntityType.COW || e.getRightClicked().getType() == EntityType.CHICKEN)
	        {
	        	int choice = random.nextInt(i - 1) + 1;
				p.getWorld().dropItemNaturally(e.getRightClicked().getLocation(), OP.get(choice));
				if (!(p.getInventory().getItemInHand().getType().equals(Material.MILK_BUCKET)) && e.getRightClicked().getType() == EntityType.CHICKEN)
				{
					p.getItemInHand().setType(Material.MILK_BUCKET);
				}
	            return;
	        }
	        e.setCancelled(true);
		}
    }
}
