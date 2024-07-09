package me.joda.shuffle;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import java.util.Iterator;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandSender;

class BlockShuffleCommandsHelper
{
    Main plugin;
    CommandSender sender;
    
    public BlockShuffleCommandsHelper(final Main plugin, final CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }
    
    public boolean startGame() {
        if (this.plugin.params.getIsGameRunning()) {
            this.sender.sendMessage("A Game is already running!");
            return true;
        }
        if (this.plugin.params.getAvailablePlayers().size() < 1) {
            this.sender.sendMessage("No players added!");
            return true;
        }
        final Iterator var1 = this.plugin.params.getAvailablePlayers().iterator();
        final BukkitTask task = new BlockShuffleTask(this.plugin).runTaskTimer((Plugin)this.plugin, 0L, 10L);
        this.plugin.params.setTask(task);
        this.plugin.params.setGameRunning(true);
        return true;
    }
    
    public boolean stopGame() {
        if (this.plugin.params.getIsGameRunning()) {
            Bukkit.getScheduler().cancelTask(this.plugin.params.getTask().getTaskId());
            final Iterator var1 = this.plugin.params.getAvailablePlayers().iterator();
            this.plugin.stopTimer((Plugin)this.plugin);
            while (var1.hasNext()) {
                final BlockShufflePlayer player = (BlockShufflePlayer) var1.next();
                final Player ply = Bukkit.getPlayer(player.getName());
                ply.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
            this.plugin.params.setGameRunning(false);
        }
        else {
            this.sender.sendMessage("No Games Running!");
        }
        return true;
    }
    
    public boolean addPlayer(final String playerString) {
        if (this.plugin.params.getIsGameRunning()) {
            this.sender.sendMessage("Cannot Add Players during game!");
            return true;
        }
        final Player player = Bukkit.getPlayer(playerString);
        if (player == null) {
            this.sender.sendMessage("No such player found");
        }
        else {
            final boolean wasAdded = this.plugin.params.addAvailablePlayer(playerString);
            if (wasAdded) {
                this.sender.sendMessage("Added player : " + playerString);
            }
            else {
                this.sender.sendMessage("Player is already added!");
            }
        }
        return true;
    }
    
    public boolean removePlayer(final String playerString) {
        if (this.plugin.params.getIsGameRunning()) {
            this.sender.sendMessage("Cannot Remove Players during game!");
            return true;
        }
        final boolean wasRemoved = this.plugin.params.removeAvailablePlayer(playerString);
        if (wasRemoved) {
            this.sender.sendMessage("Removed player : " + playerString);
        }
        else {
            this.sender.sendMessage("Player was not in player list!");
        }
        return true;
    }
    
    public boolean playerList() {
        if (this.plugin.params.getAvailablePlayers().size() == 0) {
            this.sender.sendMessage("No added players");
        }
        else {
            this.sender.sendMessage("The list of added players are : ");
            for (final BlockShufflePlayer player : this.plugin.params.getAvailablePlayers()) {
                this.sender.sendMessage(player.getName());
            }
        }
        return true;
    }
    
    public boolean skip() {
        if (this.plugin.params.getIsGameRunning()) {
            this.stopGame();
            this.plugin.startRound(this.plugin.round + 1);
        }
        else {
            this.sender.sendMessage("No Games Running!");
        }
        return true;
    }
}