package me.joda.shuffle;

import java.util.Iterator;
import org.bukkit.Bukkit;
import java.util.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockShuffleTask extends BukkitRunnable
{
    Logger logger;
    boolean hasRoundEnded;
    Main plugin;
    BlockShuffleTaskHelper helper;
    int currentRoundTime;
    int currentRound;
    int successfulPlayers;
    int counter;
    int task;
    int secondCount;
    int min;
    int sec;
    
    public BlockShuffleTask(final Main plugin) {
        this.logger = Bukkit.getLogger();
        this.secondCount = 0;
        this.min = 0;
        this.sec = 0;
        this.plugin = plugin;
        this.hasRoundEnded = true;
        this.currentRoundTime = 6000;
        this.currentRound = 0;
        this.successfulPlayers = 0;
        this.counter = 0;
        this.helper = new BlockShuffleTaskHelper(this.plugin, this.currentRound);
    }
    
    public void run() {
        this.plugin.on = true;
        if (this.counter > 0) {
            if (this.counter % 20 == 0) {
                for (BlockShufflePlayer blockShufflePlayer : this.plugin.params.getAvailablePlayers()) {}
            }
            this.counter -= 10;
        }
        else if (this.hasRoundEnded) {
            ++this.currentRound;
            this.currentRoundTime = 0;
            this.hasRoundEnded = false;
            this.successfulPlayers = 0;
            this.helper.startRound(this.currentRound);
        }
        else {
            for (final BlockShufflePlayer player : this.plugin.params.getAvailablePlayers()) {
                if (!player.getHasFoundBlock()) {
                    final boolean hasFound = this.helper.checkPlayer(player);
                    if (!hasFound) {
                        continue;
                    }
                    ++this.successfulPlayers;
                }
            }
        }
    }
    
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
    }
}