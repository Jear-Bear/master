package me.joda.shuffle;

import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class BlockShuffleTabCompleter implements TabCompleter
{
    Main plugin;
    
    public BlockShuffleTabCompleter(final Main plugin) {
        this.plugin = plugin;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final List<String> list = new ArrayList<String>();
        if (args.length == 1) {
            list.add("start");
            list.add("stop");
            list.add("info");
            list.add("add");
            list.add("remove");
            list.add("skip");
            final ArrayList suggestions = new ArrayList();
            for (final String a : list) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(a);
                }
            }
            return (List<String>)suggestions;
        }
        if (args.length == 2) {
            final ArrayList suggestions = new ArrayList();
            if (args[0].equalsIgnoreCase("add")) {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
                for (final String a : list) {
                    if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                        suggestions.add(a);
                    }
                }
                return (List<String>)suggestions;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                for (final BlockShufflePlayer p2 : this.plugin.params.getAvailablePlayers()) {
                    list.add(p2.getName());
                }
                for (final String a : list) {
                    if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                        suggestions.add(a);
                    }
                }
                return (List<String>)suggestions;
            }
        }
        return null;
    }
}