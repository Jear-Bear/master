package com.joda.health;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class HungerCommand implements CommandExecutor {

	FileConfiguration config = Main.instance.getConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("hunger"))
		{
			if (sender instanceof Player)
			{
				int hunger = config.getInt("hunger");
				if (args.length == 1)
				{
					
					hunger = (int) Math.round(Double.parseDouble(args[0]));
					Double h = config.getDouble("hunger");
					if (hunger == h)
					{
						((Player) sender).setFoodLevel(hunger);
					}
					else
					{
						this.config.set("hunger", hunger);
						System.out.println("Changed hunger in config to " + hunger);
						((Player) sender).setFoodLevel((int) hunger);
					}
				}
				else
				{
					((Player) sender).setFoodLevel(hunger);
					System.out.println("No number was entered! Set hunger to " + hunger);
				}
			}
		}
		return false;
	}
}
