package me.joda.ultimate;

import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener
{
    public Player hunter;
    public Player not;
    public boolean inGame;
    public ItemStack[] notInv;
    public ItemStack[] hunterInv;
    public UUID hunterNum;
    public UUID notNum;
    public int x;
    public int z;
    public FileConfiguration config;
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.inGame = false;
        this.makeInvs();
        getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new tagCommand(this), this);
		getCommand("tag").setExecutor(new tagCommand(this));
		getServer().getPluginManager().registerEvents(new nextCommand(this), this);
		getCommand("tag").setExecutor(new nextCommand(this));
        this.hunter = null;
        this.not = null;
        this.hunterNum = null;
        this.notNum = null;
        this.x = 0;
        this.z = 0;
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
        System.out.println("Goodbye!");
    }
    
    public void nextGame(final Player p) {
        if (this.inGame && (p.getUniqueId() == this.hunter.getUniqueId() || p.getUniqueId() == this.not.getUniqueId())) {
            this.getServer().getScheduler().cancelTasks((Plugin)this);
            this.hunterNum = this.hunter.getUniqueId();
            this.notNum = this.not.getUniqueId();
            this.hunter = this.getServer().getPlayer(this.notNum);
            this.not = this.getServer().getPlayer(this.hunterNum);
            this.not.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2400, 1));
            this.hunter.setHealth(20.0);
            this.not.setHealth(20.0);
            this.hunter.setFoodLevel(20);
            this.not.setFoodLevel(20);
            this.x = this.getRand();
            this.z = this.getRand();
            p.getWorld().getWorldBorder().setCenter((double)this.x, (double)this.z);
            p.getWorld().getWorldBorder().setSize(160.0);
            this.hunter.teleport(p.getWorld().getHighestBlockAt(this.x + 80 - 1, this.z + 80 - 1).getLocation());
            this.not.teleport(p.getWorld().getHighestBlockAt(this.x - 80 + 1, this.z - 80 + 1).getLocation());
            this.hunter.getInventory().setContents(this.hunterInv);
            this.not.getInventory().setContents(this.notInv);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    Main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Start!").toString());
                    Main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Start!").toString());
                }
            }, 0L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "1 minute Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 1200L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "30 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 1800L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "15 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2100L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "10 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2200L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "9 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2220L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "8 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2240L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "7 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2260L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "6 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2280L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "5 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2300L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "4 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2320L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "3 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2340L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                	Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "2 Seconds Left!");
                	for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2360L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "1 Second Left!");
                    for (Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }
            }, 2380L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    p.getWorld().getWorldBorder().reset();
                    Bukkit.broadcastMessage(ChatColor.BOLD.toString() + ChatColor.GOLD + Main.this.not.getDisplayName() + " has won this round!");
                    for (PotionEffect e : Main.this.not.getActivePotionEffects()) Main.this.not.removePotionEffect(e.getType());
                    Main.this.hunter.playSound(hunter.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1f, 1f);
                    Main.this.not.playSound(hunter.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                }
            }, 2400L);
        }
        else {
            p.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("You are not in a game!\nUse /tag start <hunter> <target> to start a game!").toString());
        }
    }
    
    @EventHandler
    public void onHit(final EntityDamageByEntityEvent e) {
        if (this.hunter == null) {
            return;
        }
        if (this.not == null) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        final Player p = (Player)e.getEntity();
        final Player d = (Player)e.getDamager();
        if (p.getUniqueId() != this.not.getUniqueId()) {
            return;
        }
        if (d.getUniqueId() != this.hunter.getUniqueId()) {
            return;
        }
        this.getServer().getScheduler().cancelTasks((Plugin)this);
        p.getWorld().getWorldBorder().reset();
        Bukkit.broadcastMessage(ChatColor.BOLD.toString() + ChatColor.GOLD + Main.this.hunter.getDisplayName() + " has won this round!");
        for (PotionEffect eff : Main.this.not.getActivePotionEffects()) Main.this.not.removePotionEffect(eff.getType());
    }
    
    public void helpMenu(final Player p) {
        p.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Minecraft Ultimate Tag Commands: \n").append(ChatColor.RED).append("-- /tag help").append(ChatColor.WHITE).append(" - shows this\n").append(ChatColor.RED).append("-- /tag start <hunter> <target>").append(ChatColor.WHITE).append(" - starts new game\n").append(ChatColor.RED).append("-- /tag nextRound").append(ChatColor.WHITE).append(" - starts next round").toString());
    }
    
    public void startGame(final String h, final String n, final Player p) {
        if (this.getServer().getPlayer(h) == null) {
            p.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(h).append("is not a player!").toString());
            if (this.getServer().getPlayer(n) == null) {
                p.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(n).append("is not a player!").toString());
            }
            return;
        }
        this.hunter = this.getServer().getPlayer(n);
        this.not = this.getServer().getPlayer(h);
        this.inGame = true;
        this.nextGame(p);
    }
    
    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        if (this.inGame) {
            if (e.getPlayer().getUniqueId() == this.hunter.getUniqueId()) {
                this.inGame = false;
                return;
            }
            if (e.getPlayer().getUniqueId() == this.not.getUniqueId()) {
                this.inGame = false;
            }
        }
    }
    
    public int getRand() {
        if (Math.random() > 0.5) {
            return (int)(Math.random() * 8000.0);
        }
        return (int)(Math.random() * -8000.0);
    }
    
    public void makeInvs() {
        (this.notInv = new ItemStack[4])[0] = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        this.notInv[1] = new ItemStack(Material.DIAMOND_AXE, 1);
        this.notInv[2] = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        this.notInv[3] = new ItemStack(Material.COBBLESTONE, 16);
        (this.hunterInv = new ItemStack[5])[1] = new ItemStack(Material.COMPASS, 1);
        this.hunterInv[1] = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        this.hunterInv[2] = new ItemStack(Material.DIAMOND_AXE, 1);
        this.hunterInv[3] = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        this.hunterInv[4] = new ItemStack(Material.COBBLESTONE, 16);
    }
    
    @EventHandler
    public void onDeath(final EntityDeathEvent e) {
        if (this.hunter == null) {
            return;
        }
        if (this.not == null) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player)e.getEntity();
        if (p.getUniqueId() == this.hunter.getUniqueId()) {
            this.getServer().getScheduler().cancelTasks((Plugin)this);
            p.getWorld().getWorldBorder().reset();
            Bukkit.broadcastMessage(ChatColor.BOLD.toString() + ChatColor.GOLD + Main.this.hunter.getDisplayName() + " has won this round!");
            for (PotionEffect eff : Main.this.not.getActivePotionEffects()) Main.this.not.removePotionEffect(eff.getType());
            return;
        }
        if (p.getUniqueId() == this.not.getUniqueId()) {
            this.getServer().getScheduler().cancelTasks((Plugin)this);
            p.getWorld().getWorldBorder().reset();
            Bukkit.broadcastMessage(ChatColor.BOLD.toString() + ChatColor.GOLD + Main.this.hunter.getDisplayName() + " has won this round!");
            for (PotionEffect eff : Main.this.not.getActivePotionEffects()) Main.this.not.removePotionEffect(eff.getType());
        }
    }
    
    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
        if (this.hunter == null) {
            return;
        }
        if (this.not == null) {
            return;
        }
        if (e.getPlayer().getUniqueId() != this.hunter.getUniqueId() && e.getPlayer().getUniqueId() != this.not.getUniqueId()) {
            return;
        }
        e.setRespawnLocation(e.getPlayer().getWorld().getHighestBlockAt(this.x, this.z).getLocation());
    }
}

