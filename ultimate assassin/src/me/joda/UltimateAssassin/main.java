package me.joda.UltimateAssassin;

import org.bukkit.block.Block;
import java.util.Random;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.block.Biome;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener
{
    public boolean won;
    public double wbSize;
    public Player hunter;
    public Player not;
    public boolean inGame = false;
    public ItemStack[] Inv;
    public UUID hunterNum;
    public UUID notNum;
    public int x;
    public int z;
    public FileConfiguration config;
    
    public main() {
        this.won = false;
        this.wbSize = 160.0;
    }
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.inGame = false;
        this.makeInvs();
        this.getCommand("assassin").setExecutor((CommandExecutor)new CommandExe(this));
        this.getCommand("assassin").setTabCompleter((TabCompleter)new TabCommand());
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
    }
    
    @EventHandler
    public void NoPearlDamage(final PlayerTeleportEvent event) {
        final Player p = event.getPlayer();
        final EntityDamageEvent e = p.getLastDamageCause();
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.setCancelled(true);
            p.setNoDamageTicks(60);
            p.teleport(event.getTo());
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                p.setFallDistance(0.0f);
                e.setCancelled(true);
            }
        }
    }
    
    public void nextGame(final Player p) {
        p.getLocation().getWorld().setTime(1000L);
        if (this.inGame && (p.getUniqueId() == this.hunter.getUniqueId() || p.getUniqueId() == this.not.getUniqueId())) {
            this.won = false;
            this.hunter.setHealth(20.0);
            this.not.setHealth(20.0);
            this.hunter.setFoodLevel(20);
            this.not.setFoodLevel(20);
            this.getServer().getScheduler().cancelTasks((Plugin)this);
            this.hunterNum = this.hunter.getUniqueId();
            this.notNum = this.not.getUniqueId();
            this.hunter = this.getServer().getPlayer(this.notNum);
            this.not = this.getServer().getPlayer(this.hunterNum);
            this.hunter.removePotionEffect(PotionEffectType.BLINDNESS);
            this.not.removePotionEffect(PotionEffectType.BLINDNESS);
            this.x = this.getRand();
            this.z = this.getRand();
            this.wbSize = 160.0;
            p.getWorld().getWorldBorder().setCenter((double)this.x, (double)this.z);
            p.getWorld().getWorldBorder().setSize(this.wbSize);
            final Location spawn = p.getWorld().getHighestBlockAt(this.x, this.z).getLocation();
            final Location adjusted = new Location(p.getWorld(), (double)spawn.getBlockX(), (double)(spawn.getBlockY() + 2), (double)spawn.getBlockZ());
            this.hunter.teleport(adjusted);
            this.not.teleport(adjusted);
            for(Player play : this.getServer().getOnlinePlayers()) {
    			play.setGameMode(GameMode.SURVIVAL);
            	play.getInventory().setContents(Inv);
            }
            this.hunter.setWalkSpeed(0.2f);
            this.not.setWalkSpeed(0.2f);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("Loading map...").toString());
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("You are the assassin").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("Loading map...").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.BLUE).append(ChatColor.BOLD).append("You are the target").toString());
                    main.this.hunter.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 159, 10));
                    main.this.hunter.setNoDamageTicks(159);
                    main.this.not.setWalkSpeed(0.25f);
                }
            }, 1L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.teleport(adjusted);
                }
            }, 20L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.teleport(adjusted);
                }
            }, 40L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.teleport(adjusted);
                    main.this.hunter.sendMessage(ChatColor.WHITE + "5");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.hunter.getInventory().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
                    main.this.hunter.getInventory().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
                    main.this.hunter.getInventory().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
                    main.this.hunter.getInventory().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
                    main.this.not.sendMessage(ChatColor.WHITE + "5");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                    main.this.not.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                    main.this.not.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                    main.this.not.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                }
            }, 60L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.teleport(adjusted);
                    main.this.hunter.sendMessage(ChatColor.WHITE + "4");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "4");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 80L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.teleport(adjusted);
                    main.this.hunter.sendMessage(ChatColor.WHITE + "3");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "3");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 100L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.teleport(adjusted);
                    main.this.hunter.sendMessage(ChatColor.WHITE + "2");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "2");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 120L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.teleport(adjusted);
                    main.this.hunter.sendMessage(ChatColor.WHITE + "1");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "1");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 140L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("GO!!").toString());
                     
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("GO!!").toString());
                    main.this.not.playSound(main.this.not.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                }
            }, 160L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.not.setWalkSpeed(0.2f);
                }
            }, 500L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Border shrinking in:").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Border shrinking in:").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                }
            }, 680L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(ChatColor.WHITE + "3");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "3");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 700L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(ChatColor.WHITE + "2");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "2");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 720L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(ChatColor.WHITE + "1");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "1");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 740L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.DARK_AQUA).append(ChatColor.BOLD).append("Shrinking the border!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.DARK_AQUA).append(ChatColor.BOLD).append("Shrinking the border!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.0f);
                }
            }, 760L);
            for (int i = 780; i < 1980; ++i) {
                this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                    @Override
                    public void run() {
                        p.getWorld().getWorldBorder().setSize(main.this.wbSize);
                        wbSize -= 0.05;
                    }
                }, i);
            }
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    p.getWorld().getWorldBorder().setSize(100.0);
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("3 Minutes Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("3 Minutes Left!").toString());
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("The border  has stopped shrinking!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("The border has stopped shrinking!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
            }, 1980L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Border shrinking in:").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("Border shrinking in:").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                }
            }, 2600L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(ChatColor.WHITE + "3");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "3");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 2620L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(ChatColor.WHITE + "2");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "2");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 2640L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(ChatColor.WHITE + "1");
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.sendMessage(ChatColor.WHITE + "1");
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 2660L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.DARK_AQUA).append(ChatColor.BOLD).append("Shrinking the border!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.DARK_AQUA).append(ChatColor.BOLD).append("Shrinking the border!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.0f);
                }
            }, 2680L);
            for (int i = 2700; i < 3900; ++i) {
                if (i == 3280) {
                    this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                        @Override
                        public void run() {
                            p.getWorld().getWorldBorder().setSize(main.this.wbSize);
                            main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("2 Minutes Left!").toString());
                            main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("2 Minutes Left!").toString());
                            main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                            main.this.not.playSound(main.this.not.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                            wbSize -= 0.05;
                        }
                    }, (long)i);
                }
                else {
                    this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                        @Override
                        public void run() {
                            p.getWorld().getWorldBorder().setSize(main.this.wbSize);
                            wbSize -= 0.05;
                        }
                    }, (long)i);
                }
            }
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    p.getWorld().getWorldBorder().setSize(40.0);
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("The border has stopped shrinking!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("The border has stopped shrinking!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
            }, 3900L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("1 Minute Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("1 Minute Left!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                }
            }, 4480L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("30 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("30 Seconds Left!").toString());
                }
            }, 5080L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("15 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("15 Seconds Left!").toString());
                }
            }, 5380L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("10 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("10 Seconds Left!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                }
            }, 5480L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("9 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("9 Seconds Left!").toString());
                }
            }, 5500L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("8 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("8 Seconds Left!").toString());
                }
            }, 5520L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("7 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("7 Seconds Left!").toString());
                }
            }, 5540L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("6 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("6 Seconds Left!").toString());
                }
            }, 5560L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("5 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("5 Seconds Left!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 5580L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("4 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("4 Seconds Left!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 5600L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("3 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("3 Seconds Left!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 5620L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("2 Seconds Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("2 Seconds Left!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 5640L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("1 Second Left!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("1 Second Left!").toString());
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    main.this.not.playSound(main.this.not.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }
            }, 5660L);
            this.getServer().getScheduler().runTaskLater((Plugin)this, (Runnable)new Runnable() {
                @Override
                public void run() {
                    p.getWorld().getWorldBorder().reset();
                    if (main.this.won) {
                        return;
                    }
                    main.this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(main.this.not.getDisplayName()).append(" has won this round!").toString());
                    main.this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(main.this.not.getDisplayName()).append(" has won this round!").toString());
                    main.this.not.playSound(main.this.not.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    main.this.hunter.playSound(main.this.hunter.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1.0f, 1.0f);
                    main.this.won = true;
                    inGame = false;
                }
            }, 5680L);
        }
        else {
            p.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append("You are not in a game!\nUse /assassin start <hunter> <target> to start a game!").toString());
        }
    }
    
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        e.setDeathMessage("");
        Player p = e.getEntity();
        if (p.getUniqueId() == this.not.getUniqueId()) {
            if (this.won) {
                return;
            }
            this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(this.hunter.getDisplayName()).append(" has won this round!").toString());
            this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(this.hunter.getDisplayName()).append(" has won this round!").toString());
            this.not.playSound(this.hunter.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            this.hunter.playSound(this.not.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1.0f, 1.0f);
            this.won = true;
        }
        if (p.getUniqueId() == this.hunter.getUniqueId()) {
            if (this.won) {
                return;
            }
            this.hunter.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(this.not.getDisplayName()).append(" has won this round!").toString());
            this.not.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(this.not.getDisplayName()).append(" has won this round!").toString());
            this.not.playSound(this.not.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            this.hunter.playSound(this.hunter.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1.0f, 1.0f);
            this.won = true;
        }
        this.getServer().getScheduler().cancelTasks((Plugin)this);
        p.getWorld().getWorldBorder().reset();
        inGame = false;
    }
    
    public void helpMenu(final Player p) {
        Bukkit.broadcastMessage(new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append("Ultimate Assassin Commands: \n").append(ChatColor.RED).append("/assassin help").append(ChatColor.GRAY).append(" - shows this\n").append(ChatColor.RED).append("/assassin start <assassin> <target>").append(ChatColor.GRAY).append(" - starts new game\n").append(ChatColor.RED).append("/assassin nextRound").append(ChatColor.GRAY).append(" - starts next round").toString());
    }
    
    public void startGame(final String h, final String n, final Player p) {
        if (this.getServer().getPlayer(h) == null) {
            p.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(h).append("is not a player!").toString());
            if (this.getServer().getPlayer(n) == null) {
                p.sendMessage(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(n).append("is not a player!").toString());
            }
            return;
        }
        this.nextGame(p);
        Biome b = p.getLocation().getBlock().getBiome();
        while (!((b == Biome.PLAINS) || (b == Biome.FOREST) || (b == Biome.DARK_FOREST) || (b == Biome.SWAMP) || (b == Biome.JUNGLE) || (b == Biome.SNOWY_TUNDRA) || (b == Biome.BADLANDS) || (b == Biome.DESERT) || (b == Biome.SAVANNA) || (b == Biome.MUSHROOM_FIELDS) || (b == Biome.MOUNTAINS))) 
    	{
    		b = p.getLocation().getBlock().getBiome();
    		this.nextGame(p);
    	}
        this.won = false;
        this.hunter = this.getServer().getPlayer(n);
        this.not = this.getServer().getPlayer(h);
        this.inGame = true; 
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
        if (Math.random() > 0.5) return (int)(Math.random() * 8000.0);
        return (int)(Math.random() * -8000.0);
    }
    
    public void makeInvs() {
        final ItemStack Axe = new ItemStack(Material.DIAMOND_AXE);
        Axe.addEnchantment(Enchantment.DIG_SPEED, 2);
        final ItemStack Shovel = new ItemStack(Material.DIAMOND_SHOVEL);
        Shovel.addEnchantment(Enchantment.DIG_SPEED, 2);
        final ItemStack pickAxe = new ItemStack(Material.DIAMOND_PICKAXE);
        pickAxe.addEnchantment (Enchantment.DIG_SPEED, 2);
        Inv = new ItemStack[5];
        Inv[0] = pickAxe;
        Inv[1] = Axe;
        Inv[2] = Shovel;
        Inv[3] = new ItemStack(Material.COBBLESTONE, 32);
        Inv[4] = new ItemStack(Material.COOKED_BEEF, 32);
    }
    
    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
    	/*
        if (this.hunter == null) {
            return;
        }
        if (this.not == null) {
            return;
        }
        if (e.getPlayer().getUniqueId() != this.hunter.getUniqueId() && e.getPlayer().getUniqueId() != this.not.getUniqueId()) {
            return;
        }
        */
        e.setRespawnLocation(e.getPlayer().getWorld().getHighestBlockAt(this.x, this.z).getLocation());
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Block b = event.getBlock();
        final Material t = b.getType();
        if ((inGame == true) && t == Material.ACACIA_LOG || t == Material.BIRCH_LOG || t == Material.DARK_OAK_LOG || t == Material.JUNGLE_LOG || t == Material.OAK_LOG || t == Material.SPRUCE_LOG) {
            final Random r = new Random();
            final int randomInt = r.nextInt(100) + 1;
            if (randomInt > 0 && randomInt < 33 && inGame == true) {
                if (randomInt <= 2) {
                    b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.WATER_BUCKET, 1));
                }
                else if (randomInt <= 8) {
                    b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.HAY_BLOCK, 1));
                }
                else if (randomInt <= 18) {
                    b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.CRAFTING_TABLE, 1));
                }
                else {
                    b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.LADDER, 3));
                }
            }
        }
    }
}