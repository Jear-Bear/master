package me.joda.workout;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class workoutCommand implements Listener, CommandExecutor {

	private Main main;
	int workout = 0;
	
	public workoutCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("workout"))
		{
			if (args == null)
			{
				sender.sendMessage("Enter on/off after the command!");
				return false;
			}
			else if (args != null) 
			{
				if (args[0].toString().toLowerCase().contains("off"))
				{
					main.workout = false;
					Bukkit.getScheduler().cancelTask(workout);
					sender.sendMessage("Bye!");
					return false;
				}
				if (args[0].toString().toLowerCase().contains("on")) 
				{
					workout();
					main.workout = true;
					sender.sendMessage("Pump that iron!");
				}
			}
		}
		return false;
	}
	
	public void workout()
	{
		workout = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) main, new Runnable()
		{
			public void run() 
			{
				for (Player play : Bukkit.getOnlinePlayers())
				{
					if (play.getDisplayName().equalsIgnoreCase("jeantpm")) play.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Jean is awesome!", "Tell chat to subscribe to joda", 20, 80, 20);
					play.playSound(play.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
				}
				int rand = (int) (Math.random() * 12) + 1;
				if (rand == 1)
				{
					rand = (int) (Math.random() * 6) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getDisplayName().equalsIgnoreCase("eden_") || p.getDisplayName().equalsIgnoreCase("haranabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " tricep dip";
							else workout = ChatColor.WHITE + "Do " + rand * 4 + " tricep dips";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
						else if (p.getDisplayName().equalsIgnoreCase("jodabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " tricep kickback each arm";
							else workout = ChatColor.WHITE + "Do " + rand + " tricep kickbacks each arm";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
					}
				}
				if (rand == 2)
				{
					rand = (int) (Math.random() * 30) + 1;
					String workout;
					if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " pushup";
					else workout = ChatColor.WHITE + "Do " + rand + " pushups";
					for (Player p : Bukkit.getOnlinePlayers()) 
					{
						if (p.getDisplayName().equalsIgnoreCase("jeanTPM"))
						{
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "", "", 20, 80, 20);
						}
						else p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
					}
				}
				if (rand == 3)
				{
					rand = (int) (Math.random() * 60) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " jumping jack";
						else workout = ChatColor.WHITE + "Do " + (int) (rand / 2) + " jumping jacks";
						if (p.getDisplayName().equalsIgnoreCase("jeanTPM"))
						{
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "", "", 20, 80, 20);
						}
						else p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
					}
				}
				if (rand == 4)
				{
					rand = (int) (Math.random() * 15) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getDisplayName().equalsIgnoreCase("eden_") || p.getDisplayName().equalsIgnoreCase("haranabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " burpee";
							else workout = ChatColor.WHITE + "Do " + rand + " burpees";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
						else if (p.getDisplayName().equalsIgnoreCase("jodabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " chin-up";
							else workout = ChatColor.WHITE + "Do " + rand + " chin-ups";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
					}
				}
				if (rand == 5)
				{
					rand = (int) (Math.random() * 15) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getDisplayName().equalsIgnoreCase("eden_") || p.getDisplayName().equalsIgnoreCase("haranabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " decline pushup";
							else workout = ChatColor.WHITE + "Do " + rand * 2 + " decline pushups";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
						else if (p.getDisplayName().equalsIgnoreCase("jodabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " pull-up";
							else workout = ChatColor.WHITE + "Do " + rand + " pull-ups";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
					}
				}
				if (rand == 6)
				{
					rand = (int) (Math.random() * 20) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getDisplayName().equalsIgnoreCase("eden_") || p.getDisplayName().equalsIgnoreCase("haranabeats"))
						{
							if (rand %2 == 0)
							{
								int rand2 = (int) (Math.random() * 3) + 1;
								String workoutMeme = "";
								if (rand2 == 1) workoutMeme = ChatColor.WHITE + "Simultaneously whip and nae nae for 30 seconds";
								if (rand2 == 2) workoutMeme = ChatColor.WHITE + "T pose for 15 seconds";
								if (rand2 == 3) workoutMeme = ChatColor.WHITE + "Do any fortnite dance";
								p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workoutMeme, 20, 80, 20);
							}
							else
							{
								String workoutMeme = "Do " + rand + " crunches";
								p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workoutMeme, 20, 80, 20);
							}
						}
						else if (p.getDisplayName().equalsIgnoreCase("jodabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " pull-up";
							else workout = ChatColor.WHITE + "Do " + rand + " pull-ups";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
					}
				}
				if (rand == 7)
				{
					rand = (int) (Math.random() * 30) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " sit-up";
						else workout = ChatColor.WHITE + "Do " + rand + " sit-ups";
						if (p.getDisplayName().equalsIgnoreCase("jeanTPM"))
						{
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "", "", 20, 80, 20);
						}
						else p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
					}
				}
				if (rand == 8)
				{
					rand = (int) (Math.random() * 10) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getDisplayName().equalsIgnoreCase("eden_") || p.getDisplayName().equalsIgnoreCase("haranabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " incline pushup";
							else workout = ChatColor.WHITE + "Do " + rand * 3 + " incline pushups";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
						else if (p.getDisplayName().equalsIgnoreCase("jodabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " barbell bent over row";
							else workout = ChatColor.WHITE + "Do " + rand + " barbell bent over rows";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
					}
				}
				if (rand == 9)
				{
					rand = (int) (Math.random() * 10) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getDisplayName().equalsIgnoreCase("eden_") || p.getDisplayName().equalsIgnoreCase("haranabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " lunge each leg";
							else workout = ChatColor.WHITE + "Do " + (int) (rand * 1.5) + " lunges each leg";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
						else if (p.getDisplayName().equalsIgnoreCase("jodabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " lunge with dumbbells each leg";
							else workout = ChatColor.WHITE + "Do " + rand + " lunges with dumbbells each leg";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
					}
				}
				if (rand == 10)
				{
					rand = (int) (Math.random() * 20) + 1;
					String workout;
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getDisplayName().equalsIgnoreCase("eden_") || p.getDisplayName().equalsIgnoreCase("haranabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " squat";
							else workout = ChatColor.WHITE + "Do " + rand * 2 + " squats";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
						else if (p.getDisplayName().equalsIgnoreCase("jodabeats"))
						{
							if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " squat with dumbbells";
							else workout = ChatColor.WHITE + "Do " + rand + " squats with dumbbells";
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
						}
					}
				}
				if (rand == 11)
				{
					rand = (int) (Math.random() * 20) + 1;
					String workout;
					if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " calf raise each leg";
					else workout = ChatColor.WHITE + "Do " + rand + " calf raises each leg";
					for (Player p : Bukkit.getOnlinePlayers())
					{
						if (p.getDisplayName().equalsIgnoreCase("jeanTPM"))
						{
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "", "", 20, 80, 20);
						}
						else p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
					}
				}
				if (rand == 12)
				{
					rand = (int) (Math.random() * 4) + 1;
					String workout;
					if (rand == 1) workout = ChatColor.WHITE + "Do " + rand + " one leg squat each leg";
					else workout = ChatColor.WHITE + "Do " + rand + " one leg squats each leg";
					for (Player p : Bukkit.getOnlinePlayers()) 
					{
						if (p.getDisplayName().equalsIgnoreCase("jeanTPM"))
						{
							p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "", "", 20, 80, 20);
						}
						else p.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "Workout time!", workout, 20, 80, 20);
					}
				}
			}
		}, 6000L, 6000L);
	}

}