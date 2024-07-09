package me.joda.UltimateAssassin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCommand implements TabCompleter
{
	@EventHandler
	private void onTabComplete(TabCompleteEvent event) {
	   if (!event.getBuffer().contains(" ")) {
	      //Doesn't contain space -> we are completing a command, not an argument
	      event.getCompletions().removeIf((string) -> string.contains(":"));
	   }
	}
	
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final List<String> list = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (label.equalsIgnoreCase("assassin")) {
                    list.clear();
                    if ("help".indexOf(args[0].toLowerCase()) == 0) {
                        list.add("help");
                    }
                    if ("nextGame".indexOf(args[0].toLowerCase()) == 0) {
                        list.add("nextGame");
                    }
                    if ("start".indexOf(args[0].toLowerCase()) == 0) {
                        list.add("start");
                    }
                    return list;
                }
            }
            else if (args.length == 2) {
                if (label.equalsIgnoreCase("assassin")) {
                    if (args[0].equalsIgnoreCase("start")) {
                        list.clear();
                        final Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                        Bukkit.getServer().getOnlinePlayers().toArray(players);
                        Player[] array;
                        for (int length = (array = players).length, i = 0; i < length; ++i) {
                            final Player p = array[i];
                            if (p.getDisplayName().toLowerCase().indexOf(args[0].toLowerCase()) == 0) {
                                list.add(p.getDisplayName());
                            }
                            if (p.getDisplayName().toLowerCase().indexOf(args[1].toLowerCase()) == 0) {
                                list.add(p.getDisplayName());
                            }
                        }
                        return list;
                    }
                }
                else if (args.length == 3 && label.equalsIgnoreCase("assassin") && args[0].equalsIgnoreCase("start")) {
                    list.clear();
                    final Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    Player[] array2;
                    for (int length2 = (array2 = players).length, j = 0; j < length2; ++j) {
                        final Player p = array2[j];
                        if (p.getDisplayName().toLowerCase().indexOf(args[2].toLowerCase()) == 0) {
                            list.add(p.getDisplayName());
                        }
                    }
                    return list;
                }
            }
        }
        return new ArrayList<String>();
    }
}