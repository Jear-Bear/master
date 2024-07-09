package me.joda.shuffle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class BlockShuffleCommands implements CommandExecutor
{
    private final Main plugin;
    
    public BlockShuffleCommands(final Main plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final BlockShuffleCommandsHelper helper = new BlockShuffleCommandsHelper(this.plugin, sender);
        if (label.equalsIgnoreCase("blockshuffle")) {
            if (args.length == 0) {
                return false;
            }
            if (args[0].equalsIgnoreCase("start")) {
                return helper.startGame();
            }
            if (args[0].equalsIgnoreCase("stop")) {
                return helper.stopGame();
            }
            if (args[0].equalsIgnoreCase("add")) {
                return args.length > 1 && helper.addPlayer(args[1]);
            }
            if (args[0].equalsIgnoreCase("remove")) {
                return args.length > 1 && helper.removePlayer(args[1]);
            }
            if (args[0].equalsIgnoreCase("list")) {
                return helper.playerList();
            }
            if (args[0].equalsIgnoreCase("skip")) {
                return helper.skip();
            }
            if (args.length <= 1) {
                return false;
            }
        }
        return false;
    }
}