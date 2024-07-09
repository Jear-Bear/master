package com.joda.health;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

	FileConfiguration config = Main.instance.getConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("health"))
		{
			if (sender instanceof Player)
			{
				Double health = config.getDouble("health");
				if (args.length == 1)
				{
					
					health = Double.parseDouble(args[0]);
					Double h = config.getDouble("health");
					if (health == h)
					{
						((Player) sender).setHealth(health);
					}
					else
					{
						this.config.set("health", health);
						System.out.println("Changed health in config to " + health);
						((Player) sender).setHealth(health);
					}
				}
				else
				{
					((Player) sender).setHealth(health);
					System.out.println("No number was entered! Set health to " + health);
				}
			}
		}
		return false;
	}
}
