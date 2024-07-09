package me.joda.ultimate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class nextCommand implements Listener, CommandExecutor {

	private Main main;
	
	public nextCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().contains("next")) {
            main.nextGame((Player) sender);
        }
        return false;
	}

}
