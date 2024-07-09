package me.joda.ultimate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class tagCommand implements Listener, CommandExecutor {

	private Main main;
	
	public tagCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().toLowerCase().contains("tag")) {
            if (args.length == 2) {
                main.startGame(args[1], args[2], (Player) sender);
            }
        }
        return false;
	}

}
