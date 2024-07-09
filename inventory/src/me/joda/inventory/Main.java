package me.joda.inventory;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.block.Chest;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.codingforcookies.armorequip.ArmorEquipEvent;

public class Main extends JavaPlugin implements Listener
{
	int a;
    public boolean inGame = false;
    public boolean shiftClickItem = false;
    public FileConfiguration config;
    boolean crafted = false;
    Player crafter;
    int beforeAmount;
    int after;
    ItemStack crafting = null;
    int craftedAmount;
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("invSwap").setExecutor((CommandExecutor)new CommandExe(this));
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
    }
    
    public void start(final Player p) {
    	inGame = true;
        if (inGame) 
        {
        	Bukkit.broadcastMessage("Started!");
        	this.getServer().getScheduler().cancelTasks((Plugin)this);;
            p.getLocation().getWorld().setTime(1000);
            for(Player play : this.getServer().getOnlinePlayers()) {
            	play.getInventory().clear();
            	play.setGameMode(GameMode.SURVIVAL);
            	play.setHealth(20.0);
                play.setFoodLevel(20);
            }
        }
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {	
    	if (inGame && e.getEntity() instanceof Player)
    	{  		
            for(Player play : this.getServer().getOnlinePlayers()) {
            	play.getInventory().clear();
            }       	
        }
    }
    
    @EventHandler
    public void armor(ArmorEquipEvent e)
    {	
    	if (inGame)
    	{  		
            for(Player play : this.getServer().getOnlinePlayers()) {
            	if (play != e.getPlayer()) 
            	{
            		if (e.getNewArmorPiece().getType().toString().toLowerCase().contains("helmet"))
            		{
            			play.getInventory().setHelmet(e.getNewArmorPiece());
                		for(int i = 0; i < play.getInventory().getSize(); i++) {
            				ItemStack is = play.getInventory().getItem(i);
                		    if(is != null) {
                		    	if(is.getType() == e.getNewArmorPiece().getType()) Bukkit.broadcastMessage("helm at slot: " + i);
                		        if(is.getType() == e.getNewArmorPiece().getType() && i != 39) {
                		        	play.getInventory().getItem(i).setAmount(is.getAmount() - 1);
                		        	break;
                		         }
                		    }
                		}
            		}
            		if (e.getNewArmorPiece().getType().toString().toLowerCase().contains("chestplate")) 
            		{
            			play.getInventory().setChestplate(e.getNewArmorPiece());
                		for(int i = 0; i < play.getInventory().getSize(); i++) {
            				ItemStack is = play.getInventory().getItem(i);
                		    if(is != null) {
                		    	if(is.getType() == e.getNewArmorPiece().getType()) Bukkit.broadcastMessage("chest at slot: " + i);
                		        if(is.getType() == e.getNewArmorPiece().getType() && i != 38) {
                		        	play.getInventory().getItem(i).setAmount(is.getAmount() - 1);
                		        	break;
                		        }
                		    }
                		}
            		}
            		if (e.getNewArmorPiece().getType().toString().toLowerCase().contains("leggings")) 
            		{
            			play.getInventory().setLeggings(e.getNewArmorPiece());
                		for(int i = 0; i < play.getInventory().getSize(); i++) {
            				ItemStack is = play.getInventory().getItem(i);
            				
                		    if(is != null) {
                		    	if(is.getType() == e.getNewArmorPiece().getType()) Bukkit.broadcastMessage("leggings at slot: " + i);
                		        if(is.getType() == e.getNewArmorPiece().getType() && i != 37) {
                		        	play.getInventory().getItem(i).setAmount(is.getAmount() - 1);
                		        	break;
                		        }
                		    }
                		}
            		}
            		if (e.getNewArmorPiece().getType().toString().toLowerCase().contains("boots")) 
            		{
            			play.getInventory().setBoots(e.getNewArmorPiece());
                		for(int i = 0; i < play.getInventory().getSize(); i++) {
            				ItemStack is = play.getInventory().getItem(i);
                		    if(is != null) {
                		    	if(is.getType() == e.getNewArmorPiece().getType()) Bukkit.broadcastMessage("lboots at slot: " + i);
                		        if(is.getType() == e.getNewArmorPiece().getType() && i != 36) {
                		        	play.getInventory().getItem(i).setAmount(is.getAmount() - 1);
                		        	break;
                		         }
                		    }
                		}
            		}
            		if(e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR)
            		{
            			Bukkit.broadcastMessage(e.getPlayer() + " has removed their " + e.getOldArmorPiece() + " SLOT: ");
            			
            			for(Player p : this.getServer().getOnlinePlayers()) {
            				if (p != e.getPlayer())
            				{
            					if (e.getOldArmorPiece().getType().toString().toLowerCase().contains("helmet")) p.getInventory().setHelmet(new ItemStack(Material.AIR));
                    			if (e.getOldArmorPiece().getType().toString().toLowerCase().contains("chestplate")) p.getInventory().setChestplate(new ItemStack(Material.AIR));
                    			if (e.getOldArmorPiece().getType().toString().toLowerCase().contains("leggings")) p.getInventory().setLeggings(new ItemStack(Material.AIR));
                    			if (e.getOldArmorPiece().getType().toString().toLowerCase().contains("boots")) p.getInventory().setBoots(new ItemStack(Material.AIR));
                    			p.getInventory().addItem(e.getOldArmorPiece());
            				}
            				
                        } 
            		}
            	}
            }       	
        }
    }
    
    
    @EventHandler
    public void drop(PlayerDropItemEvent event)
    {
    	if(inGame)
    	{
    		Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " dropped " + event.getItemDrop().getName());
    		for(Player play : this.getServer().getOnlinePlayers()) {
    			
            	if (play != event.getPlayer())
            	{
            		for(int i = 0; i < play.getInventory().getSize(); i++) {
            		    ItemStack is = play.getInventory().getItem(i);
            		      
            		    if(is != null) {
            		        if(is.getType() == event.getItemDrop().getItemStack().getType()) {
            		        	play.getInventory().getItem(i).setAmount(is.getAmount() - 1);
            		        	return;
            		         }
            		    }
            		}
            	}
            }    
    	}
    }
    
    public int getItemAmount(Player p, ItemStack it)
    {
    	int after = 0;
    	for(int i = 0; i < p.getInventory().getSize(); i++) {
		    ItemStack is = p.getInventory().getItem(i);
		    if(is != null) {
		        if(is.getType() == it.getType()) {
		        	after += is.getAmount();
		         }
		    }
		}
    	return after;
    }
   
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        final ItemStack before = e.getCurrentItem();
        final int beforeAmt = before.getAmount();
        Player clicker = (Player) e.getWhoClicked();
    	if (e.getClickedInventory().getType() == InventoryType.PLAYER)
    	{
    		if (e.getClick() == ClickType.RIGHT) 
        	{
        		for (Player play : Bukkit.getServer().getOnlinePlayers())
            	{
            		if (play != clicker && (clicker.getItemOnCursor() == null || clicker.getItemOnCursor().getType() == Material.AIR))
                	{
                		for(int i = 0; i < play.getInventory().getSize(); i++) {
                		    ItemStack is = play.getInventory().getItem(i);
                		      
                		    if(is != null) {
                		        if(is.getType() == before.getType()) {
                		        	play.getInventory().getItem(i).setAmount(is.getAmount() - (e.getCurrentItem().getAmount() / 2));
                		        	return;
                		         }
                		    }
                		}
                	}
            		else if (play != clicker && clicker.getItemOnCursor() != null && clicker.getItemOnCursor().getType() != Material.AIR)
                	{
            			ItemStack add = new ItemStack(clicker.getItemOnCursor().getType(), 1);
                		play.getInventory().addItem(add);
                	}
            	}
	    	}
			if (e.getClick() == ClickType.LEFT) 
	    	{
	    		for (Player play : Bukkit.getServer().getOnlinePlayers())
	        	{
	        		if (play != clicker && (clicker.getItemOnCursor() == null || clicker.getItemOnCursor().getType() == Material.AIR))
	            	{
	            		for(int i = 0; i < play.getInventory().getSize(); i++) {
	            		    ItemStack is = play.getInventory().getItem(i);
	            		      
	            		    if(is != null) {
	            		        if(is.getType() == before.getType()) {
	            		        	play.getInventory().getItem(i).setAmount(is.getAmount() - beforeAmt);
	            		        	return;
	            		         }
	            		    }
	            		}
	            	}
	        		else if (play != clicker && clicker.getItemOnCursor() != null && clicker.getItemOnCursor().getType() != Material.AIR)
	            	{
	        			ItemStack add = new ItemStack(clicker.getItemOnCursor().getType(), clicker.getItemOnCursor().getAmount());
	            		play.getInventory().addItem(add);
	            	}
	        	}
	    	}
    	}
    	if (e.getClick() == ClickType.NUMBER_KEY) 
		{
			for (Player play : Bukkit.getServer().getOnlinePlayers())
        	{
        		if (play != clicker && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR)
            	{
        			ItemStack add = new ItemStack(e.getCurrentItem());
        			if (e.getClickedInventory().getType() != InventoryType.PLAYER) play.getInventory().addItem(add);
            	}
        	}
		}
    	if (e.getClick() == ClickType.SHIFT_LEFT) 
    	{
    		Bukkit.broadcastMessage("LEFT SHIFTING" + beforeAmt + " " + e.getClickedInventory());
    		ItemStack remove = new ItemStack(e.getCurrentItem().getType(), e.getCurrentItem().getAmount());
    		for (Player play : Bukkit.getServer().getOnlinePlayers())
        	{
        		if (play != clicker && remove != null && remove.getType() != Material.AIR)
            	{
        			if (e.getClickedInventory().getType() == InventoryType.PLAYER && !(remove.getType().toString().toLowerCase().contains("chestplate") || remove.getType().toString().toLowerCase().contains("helmet") || remove.getType().toString().toLowerCase().contains("leggings") || remove.getType().toString().toLowerCase().contains("boots")))
        			{
        				play.getInventory().remove(remove);
        			}
        			else if (e.getClickedInventory().getType() != InventoryType.PLAYER)
        			{
        				play.getInventory().addItem(remove);
            		}
            	}
        	}
    	}
    }
    
    @EventHandler
    public void drop(BlockPlaceEvent event)
    {
    	if(inGame)
    	{
    		ItemStack block = new ItemStack(event.getBlock().getType());
    		Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " placed " + block);
    		for(Player play : this.getServer().getOnlinePlayers()) {
            	if (play != event.getPlayer())
            	{
            		for(int i = 0; i < play.getInventory().getSize(); i++) {
            		    ItemStack is = play.getInventory().getItem(i);
            		      
            		    if(is != null) {
            		        if(is.getType() == block.getType()) {
            		        	play.getInventory().remove(block);
            		        	break;
            		         }
            		    }
            		}
            	}
            }    
    	}
    }
    
    @EventHandler
    public void onBoatPlace(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) 
		{
			if (event.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("boat") || event.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("snowball") || event.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("charge") || event.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("pearl") || !event.getMaterial().isOccluding()) 
			{
				for(Player play : this.getServer().getOnlinePlayers()) {
	            	if (play != event.getPlayer())
	            	{
	            		for(int i = 0; i < play.getInventory().getSize(); i++) {
	            		    ItemStack is = play.getInventory().getItem(i);
	            		      
	            		    if(is != null) {
	            		        if(is.getType() == event.getItem().getType()) {
	            		        	play.getInventory().getItem(i).setAmount(is.getAmount() - 1);
	            		        	break;
	            		         }
	            		    }
	            		}
	            	}
	            }   
			}
		}
    }
    
    @EventHandler 
    public void bucketFill(PlayerBucketFillEvent event)
    {
    	if (event.getBlockClicked().getType().toString().toLowerCase().contains("water"))
    	{
    		for(Player play : this.getServer().getOnlinePlayers()) {
            	if (play != event.getPlayer())
            	{
            		play.getInventory().remove(Material.BUCKET);
            		play.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
            	}
            }   
    	}
    	if (event.getBlockClicked().getType().toString().toLowerCase().contains("lava"))
    	{
    		for(Player play : this.getServer().getOnlinePlayers()) {
            	if (play != event.getPlayer())
            	{
            		play.getInventory().remove(Material.BUCKET);
            		play.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
            	}
            }  
    	}
    }
    
    @EventHandler 
    public void bucketEmpty(PlayerBucketEmptyEvent event)
    {
    	if (event.getBucket().toString().toLowerCase().contains("water"))
    	{
    		for(Player play : this.getServer().getOnlinePlayers()) {
            	if (play != event.getPlayer())
            	{
            		play.getInventory().remove(Material.WATER_BUCKET);
            		play.getInventory().addItem(new ItemStack(Material.BUCKET));
            	}
            }   
    	}
    	if (event.getBucket().toString().toLowerCase().contains("lava"))
    	{
    		for(Player play : this.getServer().getOnlinePlayers()) {
            	if (play != event.getPlayer())
            	{
            		play.getInventory().remove(Material.LAVA_BUCKET);
            		play.getInventory().addItem(new ItemStack(Material.BUCKET));
            	}
            }  
    	}
    }
    
    @EventHandler
    public void craft(CraftItemEvent event)
    {
    	beforeAmount = 0;
    	after = 0;
    	crafter = (Player) event.getWhoClicked();
    	craftedAmount = event.getRecipe().getResult().getAmount();
    	crafting = event.getRecipe().getResult();
    	for(int i = 0; i < crafter.getInventory().getSize(); i++) {
		    ItemStack is = crafter.getInventory().getItem(i);
		    if(is != null) {
		        if(is.getType() == event.getRecipe().getResult().getType()) {
		        	beforeAmount += is.getAmount();
		         }
		    }
		}
		new BukkitRunnable() { public void run() 
		{
			if (event.getClick() == ClickType.SHIFT_LEFT)
			{
				for(int i = 0; i < crafter.getInventory().getSize(); i++) {
				    ItemStack is = crafter.getInventory().getItem(i);
				    if(is != null) {
				        if(is.getType() == event.getRecipe().getResult().getType()) {
				        	after += is.getAmount();
				         }
				    }
				}
				for(Player play : Bukkit.getServer().getOnlinePlayers()) {
					ItemStack add = new ItemStack(crafting.getType(), crafting.getAmount());
	    			if (play != crafter) 
	    			{
	    				for (int z = 0; z < ((after - beforeAmount - crafting.getAmount())/crafting.getAmount()); z++)
	    				{
	    					play.getInventory().addItem(add);
	    				}
	    						
	    			}
	    			Bukkit.broadcastMessage("added stuff from shift craft table hting");
	            } 
				cancel();
			}
		}}.runTaskTimer((Plugin) this, 10, 20);
    	crafted = true;
    	Bukkit.broadcastMessage("before: " + beforeAmount);
    }
    
	@EventHandler
    public void damageItem(PlayerItemDamageEvent event)
    {
    	Bukkit.broadcastMessage("oof");
		Player p = (Player) event.getPlayer();
		ItemStack item = new ItemStack(event.getItem());
		Damageable meta = (Damageable) item.getItemMeta();
		meta.setDamage(item.getType().getMaxDurability() / 2);
		item.setItemMeta((ItemMeta) meta);
		for(Player play : this.getServer().getOnlinePlayers()) {
			if (play != p)
			{
				for(int i = 0; i < play.getInventory().getSize(); i++) {
				    ItemStack is = play.getInventory().getItem(i);
				    if(is != null) {
				        if(is.getType() == event.getItem().getType()) 
				        {
				        	Bukkit.broadcastMessage("yes");
				        	play.getInventory().setItem(i, item);
				        }
				    }
				}
			}
        }    
    }
    
    @EventHandler
    public void levels(PlayerExpChangeEvent event)
    {
    	for (Player play : this.getServer().getOnlinePlayers())
    	{
    		if (play != event.getPlayer())
    		{
    			play.giveExp(event.getAmount());
    		}
    	}
    }
    
    @EventHandler
    public void enchant(EnchantItemEvent event)
    {
    	Bukkit.broadcastMessage("booga" + event.getEnchantsToAdd() + event.getItem());
    	ItemStack item = event.getItem();
    	item.addEnchantments(event.getEnchantsToAdd());
    	for (Player play : this.getServer().getOnlinePlayers())
    	{
    		if (play != event.getEnchanter())
    		{
    			for(int i = 0; i < play.getInventory().getSize(); i++) 
    			{
        		    ItemStack is = play.getInventory().getItem(i);
        		    if(is != null) {
        		        if(is.getType() == event.getItem().getType()) {
        		        	play.getInventory().getItem(i).setAmount(is.getAmount() - 1);
        		        	
        		         }
        		    }
        		}
    			play.getInventory().addItem(item);
    			play.setLevel(play.getLevel() - event.getExpLevelCost());
    			break;
    		}
    	}
    }
    
    @EventHandler
    public void eat(PlayerItemConsumeEvent event)
    {
    	if(inGame)
    	{
    		Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " ate " + event.getItem());
    		for(Player play : this.getServer().getOnlinePlayers()) {
    			
            	if (play != event.getPlayer())
            	{
            		for(int i = 0; i < play.getInventory().getSize(); i++) {
            		    ItemStack is = play.getInventory().getItem(i);
            		      
            		    if(is != null) {
            		        if(is.getType() == event.getItem().getType()) {
            		        	play.getInventory().getItem(i).setAmount(is.getAmount() - 1);
            		         }
            		    }
            		}
            	}
            }    
    	}
    }

    @EventHandler
    public void pickup(EntityPickupItemEvent event)
    {
    	if(inGame && event.getEntity() instanceof Player)
    	{
    		Player p = (Player) event.getEntity();
    		Bukkit.broadcastMessage(p.getDisplayName() + " picked up " + event.getItem().getName());
    		for(Player play : this.getServer().getOnlinePlayers()) {
    			if (play != p) play.getInventory().addItem(event.getItem().getItemStack());
            }    
    	}
    }
    
    @EventHandler
    public void pickup(FurnaceExtractEvent event)
    {
    	if(inGame)
    	{
    		Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " took " + event.getItemType() + " from a furnace");
    		for(Player play : this.getServer().getOnlinePlayers()) {
    			if (play != event.getPlayer())
    			{
    				play.getInventory().addItem(new ItemStack(event.getItemType(), event.getItemAmount()));
    			}
            }    
    	}
    }
}