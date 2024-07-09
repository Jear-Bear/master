package me.joda.stand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class standCommand implements CommandExecutor, Listener {

	private Main main;
	double health;
	
	public standCommand(Main main) {
		this.main = main;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("stand"))
		{
			if (args != null)
			{
				if (sender instanceof Player)
				{
					Player p = (Player) sender;
					if (args.length == 1)
					{
						if (isDouble(args[0]))
						{
							this.health = Double.parseDouble(args[0]);
							p.setMaxHealth(health);
						}
						else
						{
							if (args[0].equalsIgnoreCase("on"))
							{
								if (health != 0.0)
								{
									p.setMaxHealth(health);
								}
							}
							else if (args[0].equalsIgnoreCase("off"))
							{
								p.setMaxHealth(20.0);
							}
						}
					}
					else if (args.length == 2)
					{
						if (args[0].equalsIgnoreCase("on") && isDouble(args[1]))
						{
							this.health = Double.parseDouble(args[1]);
							p.setMaxHealth(health);
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isDouble(String str) {
	  	try {
	      	@SuppressWarnings("unused")
	    	double x = Double.parseDouble(str);
	      	return true;
		} catch (NumberFormatException e) {
	    	return false;
		}
	}
}
