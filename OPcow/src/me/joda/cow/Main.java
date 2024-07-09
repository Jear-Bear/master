package me.joda.cow;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener
{
	boolean on = false;
	opList list = new opList(this);
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new cowCommand(this), this);
		getCommand("cow").setExecutor(new cowCommand(this));
	}
	
	public ArrayList<ItemStack> makeList()
	{
		return list.makeList();
	}
}
