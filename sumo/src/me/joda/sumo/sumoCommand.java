package me.joda.sumo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class sumoCommand implements Listener, CommandExecutor {

	private Main main;
	ItemStack bow = new ItemStack(Material.BOW);
	ItemStack arrow = new ItemStack(Material.ARROW);
	ItemMeta bowMeta = bow.getItemMeta();
	public sumoCommand(Main main)
	{
		this.main = main;
		bowMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Bow");
		bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
		bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		bowMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		bowMeta.addEnchant(Enchantment.DURABILITY, 3, true);
		arrow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		bow.setItemMeta(bowMeta);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().equals("sumo"))
		{
			Player player = (Player) sender;
			Location l = player.getLocation();
			l = new Location(player.getWorld(), l.getX(), 200.0, l.getZ());
			cylinder(l, Material.STONE, 10);
			for (Player p : Bukkit.getOnlinePlayers())
			{
				p.setGameMode(GameMode.SURVIVAL);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120000, 100));
				p.getInventory().addItem(bow);
				p.getInventory().addItem(arrow);
				p.teleport(l.add(0, 2, 0));
			}
		}
		return false;
	}
	
	public void cylinder(Location loc, Material mat, int r) {
	    int cx = loc.getBlockX();
	    int cy = loc.getBlockY();
	    int cz = loc.getBlockZ();
	    World w = loc.getWorld();
	    int rSquared = r * r;
	    for (int x = cx - r; x <= cx +r; x++) {
	        for (int z = cz - r; z <= cz +r; z++) {
	            if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
	                w.getBlockAt(x, cy, z).setType(mat);
	            }
	        }
	    }
	}	
}
