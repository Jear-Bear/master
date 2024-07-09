package me.joda.torch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;

public class torchCommand implements Listener, CommandExecutor {

	private Main main;
	int round = 0;
	boolean respond = false;
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("torchy");
	public torchCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player)
		{
			main.p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("torch"))
			{
				if (args != null)
				{
					if (args[0].equalsIgnoreCase("off"))
					{
						main.on = false;
						sender.sendMessage(ChatColor.RED + "Plugin disabled");
				        return false;
					}
					if (args[0].equalsIgnoreCase("on"))
					{
						main.on = true;
						main.ask = false;
						main.name = false;
						main.playerName = "";
						main.fun = false;
						round = 0;
						main.askTake = false;
						main.accept = false;
						main.drop = false;
						sender.sendMessage(ChatColor.RED + "Plugin enabled");
				        return false;
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void clickTorch(PlayerInteractEvent e)
	{
		if (main.on && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND)
		{
			main.torchLoc = e.getClickedBlock().getLocation();
			Player p = e.getPlayer();
			Block b = e.getClickedBlock();
			if (b.getType().toString().contains("TORCH"))
			{
				main.on = true;
				main.ask = false;
				main.name = false;
				main.playerName = "";
				main.fun = false;
				round = 0;
				main.askTake = false;
				main.accept = false;
				main.drop = false;
				Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "*Smiles at you*");
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

		            @Override
		            public void run(){
		                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Hi! How're you?");
		            }
		        }, 20L);
		    }
				this.respond = false;
				
		}
	}
	
	@EventHandler
	public void clickTorch(BlockBreakEvent e)
	{
		if (main.torchLoc != null && main.on)
		{
			if (main.accept)
			{
				if (e.getBlock().getType().toString().toLowerCase().contains("torch"))
				{
					ItemStack torch = new ItemStack(Material.TORCH);
					ItemMeta itemStackMeta = torch.getItemMeta();
					itemStackMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Torchy");
					torch.setItemMeta(itemStackMeta);
					e.getBlock().setType(Material.AIR);
					e.getPlayer().getWorld().dropItemNaturally(main.torchLoc, torch);
				}
			}
		}
	}
	
	@EventHandler
	public void dropTorch(PlayerDropItemEvent e)
	{
		if (main.on && main.drop && main.playerName != null)
		{
			ItemStack torch = new ItemStack(Material.TORCH);
			ItemMeta itemStackMeta = torch.getItemMeta();
			itemStackMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Torchy");
			torch.setItemMeta(itemStackMeta);
			
			if (e.getItemDrop().getItemStack().getType() == Material.TORCH)
			{
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

		            @Override
		            public void run(){
		                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Thank you for making my dreams come true!");
		                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				            @Override
				            public void run(){
				                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I'll never forget you " + main.playerName + "!");
				                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

						            @Override
						            public void run(){
						                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I guess this is it :)");
						                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "torch off");
						            }
						        }, 40L);
				            }
				        }, 60L);
		            }
		        }, 30L);
			}
		}
	}
	
	@EventHandler
	public void biome(PlayerMoveEvent e)
	{
		if (main.on && main.accept)
		{
			ItemStack torch = new ItemStack(Material.TORCH);
			ItemMeta itemStackMeta = torch.getItemMeta();
			itemStackMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Torchy");
			torch.setItemMeta(itemStackMeta);
			if (e.getPlayer().getInventory().contains(torch) && main.playerName != null)
			{
				Player p = e.getPlayer();
				if (e.getPlayer().getLocation().getBlock().getBiome() == Biome.DEEP_FROZEN_OCEAN)
				{
					main.accept = false;
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			            @Override
			            public void run(){
			                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "We made it!");
			                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					            @Override
					            public void run(){
					                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Well " + main.playerName + "... ");
					                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							            @Override
							            public void run(){
							                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I guess this is it :)");
							                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

									            @Override
									            public void run(){
									                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Thank you so much!");
									                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

											            @Override
											            public void run(){
											                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Can you please drop me into the ocean?");
											                main.drop = true;
											            }
											        }, 50L);
									            }
									        }, 35L);
							            }
							        }, 40L);
					            }
					        }, 60L);
			            }
			        }, 30L);
				}
			}
		}
	}
	
	
	@EventHandler
	public void talkTorch(PlayerChatEvent e)
	{
		if (main.on && this.respond == false)
		{
			round += 1;
			Player p = e.getPlayer();
			main.messager = p;
			if (main.messager == p)
			{
				if (round == 1)
				{
					if (e.getMessage().toLowerCase().contains("how") || e.getMessage().toLowerCase().contains("you"))
					{
						if (e.getMessage().toLowerCase().contains("talking"))
						{
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					            @Override
					            public void run(){
					                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I'm an epic torch :D");
					                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							            @Override
							            public void run(){
							                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "What's your name?");
							                main.name = true;
							            }
							        }, 40L);
					            }
					        }, 20L);
							return;
						}
						if (e.getMessage().toLowerCase().contains("doing"))
						{
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					            @Override
					            public void run(){
					                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I'm doing great! Thanks for asking :)");
					                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							            @Override
							            public void run(){
							            	Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "What's your name?");
							                main.name = true;
							            }
							        }, 40L);
					                return;
					            }
					        }, 20L);
						}
						else
						{
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					            @Override
					            public void run(){
					                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I'm doing great! Thanks for asking :)");
					                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							            @Override
							            public void run(){
							                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "What's your name?");
							                main.name = true;
							            }
							        }, 20L);
					            }
					        }, 20L);
							return;
						}
					}
				}
				else if (round == 2)
				{
					if (main.name)
					{
						main.playerName = e.getMessage();
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				            @Override
				            public void run(){
				                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Well hello there " + main.playerName + "!");
				                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

						            @Override
						            public void run(){
						                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Are you having a fun gaming sesh?");
						                return;
						            }
						        }, 25L);
				            }
				        }, 40L);
						return;
					}
				}
				else if (round == 3)
				{
					if (e.getMessage().toLowerCase().contains("ye") || e.getMessage().toLowerCase().contains("yu") || e.getMessage().toLowerCase().contains("mhm") || e.getMessage().toLowerCase().contains("uhuh") || e.getMessage().toLowerCase().contains("i am") || !e.getMessage().toLowerCase().contains("not"))
					{
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				            @Override
				            public void run(){
				                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Yay! I'm glad :D");
				                main.fun = true;
				                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

						            @Override
						            public void run(){
						                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Hey " + main.playerName + "... ");
						                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

								            @Override
								            public void run(){
								                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I know we just met, but can I ask you something? ");
								                main.ask = true;
								            }
								        }, 30L);
						            }
						        }, 40L);
				            }
				        }, 25L);
						return;
					}
					else
					{
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				            @Override
				            public void run(){
				                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Oh no! What's wrong?");
				            }
				        }, 40L);
						return;
					}
				}
				else if (round == 4)
				{
					if (!main.fun)
					{
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				            @Override
				            public void run(){
				                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Awwww, I'm sorry to hear that!");
				                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

						            @Override
						            public void run(){
						                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Hey " + main.playerName + "... ");
						                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

								            @Override
								            public void run(){
								                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I know we just met, but can I ask you something? ");
								                main.ask = true;
								            }
								        }, 40L);
						            }
						        }, 30L);
				            }
				        }, 30L);
					}
					if (main.ask)
					{
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				            @Override
				            public void run(){
				                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Ever since I was born from coal and sticks,");
				                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

						            @Override
						            public void run(){
						                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I've wanted to see the ocean.");
						                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

								            @Override
								            public void run(){
								                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Specifically the deep frozen ocean :D");
								                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

										            @Override
										            public void run(){
										                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Do you think you could take me there?");
										                main.askTake = true;
										            }
										        }, 40L);
								            }
								        }, 35L);
						            }
						        }, 30L);
				            }
				        }, 30L);
					}
				}
				else if (round == 5)
				{
					if (main.ask && !main.askTake)
					{
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				            @Override
				            public void run(){
				                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Ever since I was born from coal and sticks,");
				                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

						            @Override
						            public void run(){
						                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + "I've wanted to see the ocean.");
						                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

								            @Override
								            public void run(){
								                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Specifically the deep frozen ocean :D");
								                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

										            @Override
										            public void run(){
										                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Do you think you could take me there?");
										                main.askTake = true;
										            }
										        }, 25L);
								            }
								        }, 25L);
						            }
						        }, 30L);
				            }
				        }, 30L);
					}
					else if (main.askTake)
					{
						if (e.getMessage().toLowerCase().contains("ye") || e.getMessage().toLowerCase().contains("yu") || e.getMessage().toLowerCase().contains("mhm") || e.getMessage().toLowerCase().contains("uhuh") || e.getMessage().toLowerCase().contains("i am") || e.getMessage().toLowerCase().contains("why not") || e.getMessage().toLowerCase().contains("sure"))
						{
							main.accept = true;
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					            @Override
					            public void run(){
					                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Yay!!!");
					                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							            @Override
							            public void run(){
							                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Ok just pick me up and find a " + ChatColor.BLUE + ChatColor.BOLD.toString() + "Deep Frozen Ocean Biome");
							                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

									            @Override
									            public void run(){
									                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I'll tell you more once you find the biome");
									                return;
									            }
									        }, 35L);
							            }
							        }, 35L);
					            }
					        }, 25L);
						}
						else
						{
							main.on = true;
							main.ask = false;
							main.name = false;
							main.playerName = "";
							main.fun = false;
							round = 0;
							main.askTake = false;
							main.accept = false;
							main.drop = false;
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					            @Override
					            public void run(){
					                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Oh.... okay");
					                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							            @Override
							            public void run(){
							                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Well I guess I'll see you around " + main.playerName + "... ");
							                return;
							            }
							        }, 25L);
					            }
					        }, 25L);
						}
					}
				}
				else if (round == 6)
				{
					if (main.askTake)
					{
						if (e.getMessage().toLowerCase().contains("ye") || e.getMessage().toLowerCase().contains("yu") || e.getMessage().toLowerCase().contains("mhm") || e.getMessage().toLowerCase().contains("uhuh") || e.getMessage().toLowerCase().contains("i am") || e.getMessage().toLowerCase().contains("why not") || e.getMessage().toLowerCase().contains("sure"))
						{
							main.accept = true;
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					            @Override
					            public void run(){
					                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Yay!!!");
					                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							            @Override
							            public void run(){
							                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Ok just pick me up and find a " + ChatColor.BLUE + ChatColor.BOLD.toString() + "Deep Frozen Ocean Biome");
							                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

									            @Override
									            public void run(){
									                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "I'll tell you more once you find the biome");
									                return;
									            }
									        }, 35L);
							            }
							        }, 35L);
					            }
					        }, 25L);
						}
						else
						{
							main.on = true;
							main.ask = false;
							main.name = false;
							main.playerName = "";
							main.fun = false;
							round = 0;
							main.askTake = false;
							main.accept = false;
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					            @Override
					            public void run(){
					                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Oh.... okay");
					                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							            @Override
							            public void run(){
							                Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "<Torchy> " + ChatColor.WHITE + "Well I guess I'll see you around " + main.playerName + "... ");
							                return;
							            }
							        }, 25L);
					            }
					        }, 25L);
						}
					}
				}
			}
		}
	}
	

}
