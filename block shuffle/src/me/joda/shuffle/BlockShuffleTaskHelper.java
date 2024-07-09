package me.joda.shuffle;

import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import java.util.Objects;
import org.bukkit.entity.Player;
import java.util.Random;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

class BlockShuffleTaskHelper
{
    Main plugin;
    int currentRound;
    int counter;
    int task;
    int secondCount;
    int min;
    int sec;
    BlockShufflePlayer joda;
    BlockShufflePlayer jean;
    
    public BlockShuffleTaskHelper(final Main plugin, final int currentRound) {
        this.secondCount = 0;
        this.min = 0;
        this.sec = 0;
        this.plugin = plugin;
        this.currentRound = currentRound;
    }
    
    public void startRound(final int currentRound) {
        this.plugin.on = true;
        this.plugin.round = currentRound;
        new BukkitRunnable() {
            public void run() {
                if (!BlockShuffleTaskHelper.this.plugin.on) {
                    this.cancel();
                }
                BlockShuffleTaskHelper.this.timer(BlockShuffleTaskHelper.this.plugin.minutes * 60 + 1);
            }
        }.runTaskTimer((Plugin)this.plugin, 0L, this.plugin.minutes * 61L * 20L);
        this.currentRound = currentRound;
        for (final BlockShufflePlayer player : this.plugin.params.getAvailablePlayers()) {
            if (player.getName().equalsIgnoreCase("jodabeats")) {
                this.joda = player;
            }
            else if (player.getName().equalsIgnoreCase("jeantpm")) {
                this.jean = player;
            }
            player.setHasFoundBlock(false);
            if (this.plugin.round == 1) {
                player.setBlockToBeFound(Material.COBBLED_DEEPSLATE);
            }
            else {
                player.setBlockToBeFound(this.getRandomBlock());
            }
            Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + player.getName() + "'s block is " + ChatColor.GREEN + ChatColor.BOLD.toString() + player.getBlockToBeFound());
            Bukkit.getLogger().info("Assigned " + player.getName() + " with " + player.getBlockToBeFound());
        }
    }
    
    public Material getRandomBlock() {
        final Random rand = new Random();
        final int randomNumber = rand.nextInt(this.plugin.params.getAvailableBlocks().size());
        return this.plugin.params.getAvailableBlocks().get(randomNumber);
    }
    
    public boolean checkPlayer(final BlockShufflePlayer player) {
        final Material standingOn = Objects.requireNonNull(Bukkit.getPlayer(player.getName())).getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if (standingOn.equals((Object)player.getBlockToBeFound())) {
            player.setHasFoundBlock(true);
            player.setScore(player.getScore() + 1);
            for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 2.0f, 2.0f);
            }
            this.plugin.getServer().getScheduler().cancelTasks((Plugin)this.plugin);
            this.startRound(this.plugin.round + 1);
            return true;
        }
        return false;
    }
    
    public void broadcastSound(final Sound sound) {
        for (final BlockShufflePlayer player : this.plugin.params.getAvailablePlayers()) {
            player.player.playSound(player.player.getLocation(), sound, 1.0f, 1.0f);
        }
    }
    
    public void timer(final int seconds) {
        final int total = seconds * 20;
        this.min = 0;
        this.sec = 0;
        this.secondCount = 0;
        for (int i = 0; i < total; i += 20) {
            new BukkitRunnable() {
                public void run() {
                    if (BlockShuffleTaskHelper.this.jean != null || BlockShuffleTaskHelper.this.joda != null) {
                        if (BlockShuffleTaskHelper.this.jean != null && BlockShuffleTaskHelper.this.checkPlayer(BlockShuffleTaskHelper.this.jean)) {
                            this.cancel();
                        }
                        if (BlockShuffleTaskHelper.this.joda != null && BlockShuffleTaskHelper.this.checkPlayer(BlockShuffleTaskHelper.this.joda)) {
                            this.cancel();
                        }
                    }
                    if (!BlockShuffleTaskHelper.this.plugin.on) {
                        this.cancel();
                    }
                    else {
                        final BlockShuffleTaskHelper this$0 = BlockShuffleTaskHelper.this;
                        ++this$0.secondCount;
                        final int total = seconds - BlockShuffleTaskHelper.this.secondCount;
                        BlockShuffleTaskHelper.this.min = total / 60;
                        BlockShuffleTaskHelper.this.sec = total % 60;
                        String secon = String.valueOf(BlockShuffleTaskHelper.this.sec);
                        if (secon.length() == 1) {
                            secon = "0" + secon;
                        }
                        String color = "";
                        if ((BlockShuffleTaskHelper.this.plugin.minutes * 60 - BlockShuffleTaskHelper.this.secondCount) / (double)(BlockShuffleTaskHelper.this.plugin.minutes * 60) * 100.0 > 80.0) {
                            color = ChatColor.GREEN + ChatColor.BOLD.toString();
                        }
                        else if ((BlockShuffleTaskHelper.this.plugin.minutes * 60 - BlockShuffleTaskHelper.this.secondCount) / (double)(BlockShuffleTaskHelper.this.plugin.minutes * 60) * 100.0 > 60.0) {
                            color = ChatColor.YELLOW + ChatColor.BOLD.toString();
                        }
                        else if ((BlockShuffleTaskHelper.this.plugin.minutes * 60 - BlockShuffleTaskHelper.this.secondCount) / (double)(BlockShuffleTaskHelper.this.plugin.minutes * 60) * 100.0 > 40.0) {
                            color = ChatColor.GOLD + ChatColor.BOLD.toString();
                        }
                        else if ((BlockShuffleTaskHelper.this.plugin.minutes * 60 - BlockShuffleTaskHelper.this.secondCount) / (double)(BlockShuffleTaskHelper.this.plugin.minutes * 60) * 100.0 > 20.0) {
                            color = ChatColor.RED + ChatColor.BOLD.toString();
                        }
                        else {
                            color = ChatColor.DARK_RED + ChatColor.BOLD.toString();
                        }
                        final String message = String.valueOf(color) + BlockShuffleTaskHelper.this.min + ":" + secon;
                        for (final Player p : Bukkit.getOnlinePlayers()) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                            if (total == 0) {
                                final CommandSender sender = (CommandSender)p;
                                Bukkit.dispatchCommand(sender, "kill JeanTPM");
                                this.cancel();
                            }
                        }
                    }
                }
            }.runTaskLater((Plugin)this.plugin, (long)i);
        }
    }
}