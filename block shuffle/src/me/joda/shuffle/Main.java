package me.joda.shuffle;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import java.util.Objects;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    BlockShuffleParams params;
    boolean on;
    int minutes;
    int round;
    Player p;
    BlockShuffleTaskHelper b;
    
    public Main() {
        this.on = false;
        this.minutes = 8;
        this.round = 1;
        this.b = new BlockShuffleTaskHelper(this, this.round);
    }
    
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("blockshuffle")).setExecutor((CommandExecutor)new BlockShuffleCommands(this));
        Objects.requireNonNull(this.getCommand("blockshuffle")).setTabCompleter((TabCompleter)new BlockShuffleTabCompleter(this));
        this.saveDefaultConfig();
        this.params = new BlockShuffleParams();
        this.loadConfigFile();
    }
    
    public void onDisable() {
    }
    
    public void stopTimer(final Plugin plug) {
        plug.getServer().getScheduler().cancelTasks(plug);
    }
    
    public void loadConfigFile() {
        final List<Material> validBlocks = new ArrayList<Material>();
        final List<String> configBlocks = (List<String>)this.getConfig().getStringList("block-list");
        for (final String block : configBlocks) {
            if (this.checkMaterialValidity(block)) {
                validBlocks.add(Material.getMaterial(block));
                Bukkit.getLogger().info("Loaded " + block);
            }
            else {
                Bukkit.getLogger().info("Material " + block + " is not valid. Skipping");
            }
        }
        if (validBlocks.isEmpty()) {
            Bukkit.getLogger().info("No blocks were added from the config.yml file. Game cannot start");
        }
        this.params.setAvailableBlocks(validBlocks);
        Bukkit.getLogger().info("Total of " + validBlocks.size() + " blocks were added");
    }
    
    public boolean checkMaterialValidity(final String material) {
        final Material m = Material.getMaterial(material);
        return m != null;
    }
    
    public void startRound(final int round) {
        this.b.startRound(round);
        this.params.setGameRunning(true);
    }
}
