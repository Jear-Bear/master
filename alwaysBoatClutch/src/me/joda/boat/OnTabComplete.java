package me.joda.boat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class OnTabComplete implements TabCompleter {
   @Override
   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      if (args.length == 1)
      {
  	  	return StringUtil.copyPartialMatches(args[0], Arrays.asList(new String[]{"start", "stop", "on", "off", "help"}), new ArrayList<>());
      }
        else
            return Collections.emptyList();
   }
}