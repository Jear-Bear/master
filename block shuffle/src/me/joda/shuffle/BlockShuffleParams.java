package me.joda.shuffle;

import java.util.Iterator;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import org.bukkit.Material;
import java.util.List;
import org.bukkit.scheduler.BukkitTask;

public class BlockShuffleParams
{
    private BukkitTask task;
    private boolean isGameRunning;
    private List<Material> availableBlocks;
    private List<BlockShufflePlayer> availablePlayers;
    
    public BlockShuffleParams() {
        this.isGameRunning = false;
        this.availablePlayers = new ArrayList<BlockShufflePlayer>();
    }
    
    public List<Material> getAvailableBlocks() {
        return this.availableBlocks;
    }
    
    public void setAvailableBlocks(final List<Material> availableBlocks) {
        this.availableBlocks = availableBlocks;
    }
    
    public void setTask(final BukkitTask task) {
        this.task = task;
    }
    
    public BukkitTask getTask() {
        return this.task;
    }
    
    public boolean getIsGameRunning() {
        return this.isGameRunning;
    }
    
    public void setGameRunning(final boolean isGameRunning) {
        this.isGameRunning = isGameRunning;
    }
    
    public List<BlockShufflePlayer> getAvailablePlayers() {
        return this.availablePlayers;
    }
    
    public boolean addAvailablePlayer(final String playerString) {
        for (final BlockShufflePlayer player : this.availablePlayers) {
            if (player.getName().equalsIgnoreCase(playerString)) {
                return false;
            }
        }
        final BlockShufflePlayer player2 = new BlockShufflePlayer(Bukkit.getPlayer(playerString));
        this.availablePlayers.add(player2);
        return true;
    }
    
    public boolean removeAvailablePlayer(final String playerString) {
        int indexToBeRemoved = -1;
        for (final BlockShufflePlayer player : this.availablePlayers) {
            if (player.getName().equalsIgnoreCase(playerString)) {
                indexToBeRemoved = this.availablePlayers.indexOf(player);
                break;
            }
        }
        if (indexToBeRemoved >= 0) {
            this.availablePlayers.remove(indexToBeRemoved);
            return true;
        }
        return false;
    }
}