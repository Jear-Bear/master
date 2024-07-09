package me.joda.randomMinigame;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.concurrent.ThreadLocalRandom;

public class randomCommand implements CommandExecutor {

	private Main main;
	int choice = 1;
	public randomCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("random"))
		{
			if (sender instanceof Player)
			{
				choice = ThreadLocalRandom.current().nextInt(1, 3 + 1);
				Player p = (Player) sender;
				if (choice == 1) p.chat("/server marathon");
				if (choice == 2) p.chat("/server Hide and Seek");
				if (choice == 3) p.chat("/server KitPvP");
			}
		}
		return false;
	}

}
