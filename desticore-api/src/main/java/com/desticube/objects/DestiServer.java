package com.desticube.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.desticube.API;
import com.desticube.annotations.Command;
import com.desticube.configs.ConfigYAML;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.handlers.VaultHandler;
import com.desticube.timers.TPSTimer;

import net.md_5.bungee.api.ChatColor;

public class DestiServer implements Listener {
	Plugin pl;
	Server server;
	HashMap<Command, String> cmds;
	static HashMap<Player, DestiPlayer> onlineplayers;
	static Location spawn;
	ArrayList<DestiWarp> warps;
	ArrayList<DestiPlayerWarp> playerwarps;
	ConfigYAML configs = ConfigYAML.a();
	ArrayList<DestiKit> kits;
	ArrayList<Page> playerpages;
	ArrayList<Page> serverpages;
	public DestiServer(Server server, Plugin pl) { 
		this.server = server;
		this.pl = pl;
		server.getPluginManager().registerEvents(this, pl);
		onlineplayers = new HashMap<Player, DestiPlayer>();
		warps = new ArrayList<DestiWarp>();
		playerwarps = new ArrayList<DestiPlayerWarp>();
		kits = new ArrayList<DestiKit>();
		cmds = new HashMap<Command, String>();
		spawn = null;
		playerpages = new ArrayList<Page>();
		serverpages = new ArrayList<Page>();
		if (configs.getLocations().getString("SpawnLocation.world") != null) {
		spawn = new Location(Bukkit.getWorld(configs.getLocations().getString("SpawnLocation.world")),
				configs.getLocations().getLong("SpawnLocation.x"),
				configs.getLocations().getLong("SpawnLocation.y"),
				configs.getLocations().getLong("SpawnLocation.z"),
				configs.getLocations().getLong("SpawnLocation.yaw"),
				configs.getLocations().getLong("SpawnLocation.pitch"));
		}
		if (configs.getLocations().getConfigurationSection("Warps") != null) {
			for (String name : configs.getLocations().getConfigurationSection("Warps").getKeys(false)) {
				Location loc = new Location(Bukkit.getWorld(configs.getLocations().getString("Warps." + name + ".world")),
						configs.getLocations().getLong("Warps." + name + ".x"),
						configs.getLocations().getLong("Warps." + name + ".y"),
						configs.getLocations().getLong("Warps." + name + ".z"),
						configs.getLocations().getLong("Warps." + name + ".yaw"),
						configs.getLocations().getLong("Warps." + name + ".pitch"));
				ItemStack[] displayItem = itemStackArrayFromBase64(configs.getLocations().getString("Warps." + name + ".displayitem"));
				warps.add(new DestiWarp(name, loc, displayItem[0]));
			}
		}
		if (configs.getLocations().getConfigurationSection("PlayerWarps") != null) {
			for (String name : configs.getLocations().getConfigurationSection("PlayerWarps").getKeys(false)) {
				Location loc = new Location(Bukkit.getWorld(configs.getLocations().getString("PlayerWarps." + name + ".world")),
						configs.getLocations().getLong("PlayerWarps." + name + ".x"),
						configs.getLocations().getLong("PlayerWarps." + name + ".y"),
						configs.getLocations().getLong("PlayerWarps." + name + ".z"),
						configs.getLocations().getLong("PlayerWarps." + name + ".yaw"),
						configs.getLocations().getLong("PlayerWarps." + name + ".pitch"));
				ItemStack[] displayItem = itemStackArrayFromBase64(configs.getLocations().getString("PlayerWarps." + name + ".displayitem"));
				OfflinePlayer owner = Bukkit.getOfflinePlayer(configs.getLocations().getString("PlayerWarps." + name + ".owner"));
				playerwarps.add(new DestiPlayerWarp(owner, name, loc, displayItem[0]));
			}
		}
		if (configs.getKits().getConfigurationSection("kits") != null) {
			for (String name : configs.getKits().getConfigurationSection("kits").getKeys(false)) {
				kits.add(new DestiKit(name, 
						itemStackArrayFromBase64(configs.getKits().getString("kits." + name + ".items")),
						configs.getKits().getInt("kits." + name + ".cooldown")));
			}
		}
		if (configs.getCustomText().getConfigurationSection("customtext") != null) {
			for (String command : configs.getCustomText().getConfigurationSection("customtext").getKeys(false)) {
				CustomText text = new CustomText(command, configs.getCustomText().getStringList("customtext." + command));
				text.register(command, "", "", "", null, "");
			}	
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
            @Override
            public void run() {
            	updateServerPages();
            	updatePlayerPages();
            }
        }, (20 * 3));
	}
	
	public void reload() {
		configs.reloadConfig();
		configs.saveConfig();
		config.a().setup();
		configs.reloadMessages();
		configs.saveMessages();
		msg.a().setup();
		configs.reloadLocations();
		configs.saveLocations();
		configs.reloadRules();
		configs.saveRules();
		configs.reloadCustomText();
		configs.saveCustomText();
		playerwarps = new ArrayList<DestiPlayerWarp>();
		warps = new ArrayList<DestiWarp>();
		kits = new ArrayList<DestiKit>();
		if (configs.getLocations().getString("SpawnLocation.world") != null) {
			spawn = new Location(Bukkit.getWorld(configs.getLocations().getString("SpawnLocation.world")),
					configs.getLocations().getLong("SpawnLocation.x"),
					configs.getLocations().getLong("SpawnLocation.y"),
					configs.getLocations().getLong("SpawnLocation.z"),
					configs.getLocations().getLong("SpawnLocation.yaw"),
					configs.getLocations().getLong("SpawnLocation.pitch"));
		}
		if (configs.getLocations().getConfigurationSection("Warps") != null) {
			for (String name : configs.getLocations().getConfigurationSection("Warps").getKeys(false)) {
				Location loc = new Location(Bukkit.getWorld(configs.getLocations().getString("Warps." + name + ".world")),
						configs.getLocations().getLong("Warps." + name + ".x"),
						configs.getLocations().getLong("Warps." + name + ".y"),
						configs.getLocations().getLong("Warps." + name + ".z"),
						configs.getLocations().getLong("Warps." + name + ".yaw"),
						configs.getLocations().getLong("Warps." + name + ".pitch"));
				ItemStack[] displayItem = itemStackArrayFromBase64(configs.getLocations().getString("Warps." + name + ".displayitem"));
				warps.add(new DestiWarp(name, loc, displayItem[0]));
			}
		}

		if (configs.getLocations().getConfigurationSection("PlayerWarps") != null) {
			for (String name : configs.getLocations().getConfigurationSection("PlayerWarps").getKeys(false)) {
				Location loc = new Location(Bukkit.getWorld(configs.getLocations().getString("PlayerWarps." + name + ".world")),
						configs.getLocations().getLong("PlayerWarps." + name + ".x"),
						configs.getLocations().getLong("PlayerWarps." + name + ".y"),
						configs.getLocations().getLong("PlayerWarps." + name + ".z"),
						configs.getLocations().getLong("PlayerWarps." + name + ".yaw"),
						configs.getLocations().getLong("PlayerWarps." + name + ".pitch"));
				ItemStack displayItem[] = itemStackArrayFromBase64(configs.getLocations().getString("PlayerWarps." + name + ".displayitem"));
				OfflinePlayer owner = Bukkit.getOfflinePlayer(configs.getLocations().getString("PlayerWarps." + name + ".owner"));
				playerwarps.add(new DestiPlayerWarp(owner, name, loc, displayItem[0]));
			}
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
            @Override
            public void run() {
            	updateServerPages();
            	updatePlayerPages();
            }
        }, (20 * 3));
	}
	
	public void shutdown() {
		configs.getKits().set("kits", null);
		configs.saveKits();
		configs.reloadKits();
		for (DestiKit kit : kits) {
			configs.getKits().set("kits." + kit.getName() + ".cooldown", kit.getCooldown());
			configs.getKits().set("kits." + kit.getName() + ".items", itemStackArrayToBase64(kit.getItems()));
		}
		configs.saveKits();
	}
	
	public ArrayList<String> getRules(Integer i) {
		ArrayList<String> rules = new ArrayList<String>();
		for (String s : configs.getRules().getStringList(String.valueOf(i))) {rules.add(s);}
		return rules;
	}
	
	public ArrayList<String> getOnlineList() {
		ArrayList<String> online = new ArrayList<String>();
		for (String s : config.LIST_FORMAT) {
			if (s.contains("%total%") || !s.contains("%")){
				online.add(s
						.replaceAll("%total%", "" + Bukkit.getOnlinePlayers().size()));
				continue;
			}
			for (String placeholder : config.LIST_PLACEHOLDERS.keySet()) {
					if (s.contains(placeholder)) {
						StringBuilder players = new StringBuilder();
						ArrayList<String> playername = new ArrayList<String>();
						for (String ranks : config.LIST_PLACEHOLDERS.get(placeholder)) {
							for (Player p : Bukkit.getOnlinePlayers()) {
								if (Arrays.asList(VaultHandler.a().getPerms().getPlayerGroups(p)).contains(ranks)) {
									players.append(p.getName() + " ");
									playername.add(p.getName());
								}
							}
						}
						String newplaceholder = placeholder.replaceAll("%", "");
						online.add(s
								.replaceAll("" + placeholder, "" + players)
								.replaceAll("%" + newplaceholder + "_amount%", "" + playername.size())
								.replaceAll("%total%", "" + Bukkit.getOnlinePlayers().size()));
						continue;
					}
			}
		}
		return online;
	}
	
	
	public static HashMap<Player, DestiPlayer> playerstorage() {return onlineplayers;}
	public HashMap<Player, DestiPlayer> getOnlineDestiPlayers() {return onlineplayers;}
	public DestiPlayer getDestiPlayer(Player p) {return onlineplayers.get(p);}
	
	public void setSpawn(Location loc) {
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		configs.getLocations().set("SpawnLocation.world", world);
		configs.getLocations().set("SpawnLocation.x", x);
		configs.getLocations().set("SpawnLocation.y", y);
		configs.getLocations().set("SpawnLocation.z", z);
		configs.getLocations().set("SpawnLocation.yaw", yaw);
		configs.getLocations().set("SpawnLocation.pitch", pitch);
		configs.saveLocations();
		configs.reloadLocations();
		spawn = loc;
	}
	public Location getSpawn() {return spawn;}
	public void setWarp(String name, Location loc, ItemStack displayItem) {
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		array.add(displayItem);
		ItemStack[] is = array.toArray(new ItemStack[0]);
		configs.getLocations().set("Warps." + name + ".displayitem", itemStackArrayToBase64(is));
		configs.getLocations().set("Warps." + name + ".world", world);
		configs.getLocations().set("Warps." + name + ".x", x);
		configs.getLocations().set("Warps." + name + ".y", y);
		configs.getLocations().set("Warps." + name + ".z", z);
		configs.getLocations().set("Warps." + name + ".yaw", yaw);
		configs.getLocations().set("Warps." + name + ".pitch", pitch);
		configs.saveLocations();
		configs.reloadLocations();
		warps.add(new DestiWarp(name, loc, displayItem));
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
            @Override
            public void run() {
            	updateServerPages();
            }
        }, (20 * 5));
	}
	public boolean delWarp(DestiWarp warp) {
		if (warps.contains(warp)) {
			configs.getLocations().set("Warps." + warp.getName(), null);
			configs.saveLocations();
			configs.reloadLocations();
			warps.remove(warp);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
	            @Override
	            public void run() {
	            	updateServerPages();
	            }
	        }, (20 * 5));
			return warps.contains(warp);
		} else return false;
	}
	public ArrayList<DestiWarp> warpList() {
		return warps;
	}
	public ArrayList<String> warpListAsString() {
		ArrayList<String> warplist = new ArrayList<String>();
		warps.forEach(s -> warplist.add(s.getName()));
		return warplist;
	}
	boolean warpexists;
	public boolean warpExists(String name) {
		warpexists = false;
		warps.forEach(warp -> {
			if (warp.getName().equalsIgnoreCase(name)) {
				warpexists = true;
			}
		});
		return warpexists;
	}
	DestiWarp warpget;
	public DestiWarp getWarp(String name) {
		warpget = null;
		warps.forEach(warp -> {
			if (warp.getName().equalsIgnoreCase(name)) {
				warpget = warp;
			}
		});
		return warpget;
	}
	
	
	
	
	public void setPlayerWarp(Player owner, String name, Location loc, ItemStack displayItem) {
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		ArrayList<ItemStack> array = new ArrayList<ItemStack>();
		array.add(displayItem);
		ItemStack[] is = array.toArray(new ItemStack[0]);
		configs.getLocations().set("PlayerWarps." + name + ".owner", owner.getUniqueId().toString());
		configs.getLocations().set("PlayerWarps." + name + ".displayitem", itemStackArrayToBase64(is));
		configs.getLocations().set("PlayerWarps." + name + ".world", world);
		configs.getLocations().set("PlayerWarps." + name + ".x", x);
		configs.getLocations().set("PlayerWarps." + name + ".y", y);
		configs.getLocations().set("PlayerWarps." + name + ".z", z);
		configs.getLocations().set("PlayerWarps." + name + ".yaw", yaw);
		configs.getLocations().set("PlayerWarps." + name + ".pitch", pitch);
		configs.saveLocations();
		configs.reloadLocations();
		playerwarps.add(new DestiPlayerWarp(owner, name, loc, displayItem));
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
            @Override
            public void run() {
            	updatePlayerPages();
            }
        }, (20 * 5));
	}
	public boolean delPlayerWarp(DestiPlayerWarp warp) {
		if (playerwarps.contains(warp)) {
			configs.getLocations().set("PlayerWarps." + warp.getName(), null);
			configs.saveLocations();
			configs.reloadLocations();
			playerwarps.remove(warp);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
	            @Override
	            public void run() {
	            	updatePlayerPages();
	            }
	        }, (20 * 5));
			return playerwarps.contains(warp);
		} else return false;
	}
	public ArrayList<DestiPlayerWarp> playerWarpList() {
		return playerwarps;
	}
	public ArrayList<String> playerWarpListAsString() {
		ArrayList<String> warplist = new ArrayList<String>();
		playerwarps.forEach(s -> warplist.add(s.getName()));
		return warplist;
	}
	boolean playerwarpexists;
	public boolean playerWarpExists(String name) {
		playerwarpexists = false;
		playerwarps.forEach(warp -> {
			if (warp.getName().equalsIgnoreCase(name)) {
				playerwarpexists = true;
			}
		});
		return playerwarpexists;
	}
	DestiPlayerWarp playerwarpget;
	public DestiPlayerWarp getPlayerWarp(String name) {
		playerwarpget = null;
		playerwarps.forEach(warp -> {
			if (warp.getName().equalsIgnoreCase(name)) {
				playerwarpget = warp;
			}
		});
		return playerwarpget;
	}
	String playerwarpowner;
	public String getPlayerWarpOwner(String name) {
		playerwarpowner = null;
		playerwarps.forEach(warp -> {
			if (warp.getName().equalsIgnoreCase(name)) {
				playerwarpowner = warp.getOwner().getUniqueId().toString();
			}
		});
		return playerwarpowner;
	}
	
	
	public void createKit(String name, Inventory inv, int cooldown) {
		kits.add(new DestiKit(name, inv.getContents(), cooldown));
//		for (ItemStack item : inv.getContents()) {
//			configs.getKits().set("kits." + name + "." + item.getType().toString().toUpperCase() + ".amount", item.getAmount());
//			configs.getKits().set("kits." + name + "." + item.getType().toString().toUpperCase() + ".displayname", item.getItemMeta().getDisplayName());
//			configs.getKits().set("kits." + name + "." + item.getType().toString().toUpperCase() + ".lore", item.getLore());
//			item.getEnchantments().keySet().forEach(enchant -> {
//				configs.getKits().set("kits." + name + "." + item.getType().toString().toUpperCase() + ".enchants." + enchant.getName(), item.getEnchantmentLevel(enchant));
//			});
//		}
	}
	public boolean deleteKit(String name) {
		DestiKit kit = null;
		for (DestiKit kits : kits) {
			if (kits.getName().equalsIgnoreCase(name)) {
				kit = kits;
				break;
			}
		}
		kits.remove(kit);
		return kits.contains(kit);
	}
	
	public DestiKit getKit(String name) {
		DestiKit kit = null;
		for (DestiKit kits : kits) {
			if (kits.getName().equalsIgnoreCase(name)) {
				kit = kits;
				break;
			}
		}
		return kit;
	}
	
	public ArrayList<String> getKits() {
		ArrayList<String> kitnames = new ArrayList<String>();
		for (DestiKit kits : kits) {
				kitnames.add(kits.getName());
			}
		return kitnames;
	}
	public boolean kitExists(String name) {
		boolean kitexists = false;
		for (DestiKit kit : kits) {
			if (kit.getName().equalsIgnoreCase(name)) {
				kitexists = true;
			}
		}
		return kitexists;
	}
	
	public String getRealName(String nickname) {
		for (DestiPlayer dp : getOnlineDestiPlayers().values()) {
			if (dp.getNickName().contains(nickname)
					|| dp.getName().contains(nickname)) { 
				return dp.getName();
			}
		}
		return null;
	}
	public Player getRealPlayer(String nickname) {
		for (DestiPlayer dp : getOnlineDestiPlayers().values()) {
			if (dp.getNickName().contains(nickname)
					|| dp.getName().contains(nickname)) { 
				return dp.getPlayer();
			}
		}
		return null;
	}
	public DestiPlayer getRealDestiPlayer(String nickname) {
		for (DestiPlayer dp : getOnlineDestiPlayers().values()) {
			if (dp.getNickName().contains(nickname)
					|| dp.getName().contains(nickname)) { 
				return dp;
			}
		}
		return null;
	}
	
	public boolean isOnline(String name) {
		if (getRealPlayer(name) == null) return false;
		else if (getRealPlayer(name).isOnline()) return true;
		else return false;
	}
	
	     
	    private String itemStackArrayToBase64(ItemStack[] items) {
	    	try {
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
	            
	            // Write the size of the inventory
	            dataOutput.writeInt(items.length);
	            
	            // Save every element in the list
	            for (int i = 0; i < items.length; i++) {
	                dataOutput.writeObject(items[i]);
	            }
	            
	            // Serialize that array
	            dataOutput.close();
	            return Base64Coder.encodeLines(outputStream.toByteArray());
	        } catch (Exception e) {
	            throw new IllegalStateException("Unable to save item stacks.", e);
	        }
	    }

	    private ItemStack[] itemStackArrayFromBase64(String data) {
	    	try {
	            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
	            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
	            ItemStack[] items = new ItemStack[dataInput.readInt()];
	    
	            // Read the serialized inventory
	            for (int i = 0; i < items.length; i++) {
	            	items[i] = (ItemStack) dataInput.readObject();
	            }
	            
	            dataInput.close();
	            return items;
	        } catch (ClassNotFoundException | IOException e) {
	            e.printStackTrace();
	        }
			return null;
	    }

	    public Double getTPS() {
	    	return TPSTimer.a().getTPS();
	    }
	    
	    public HashMap<Command, String> getCommands() {
	    	return cmds;
	    }
	    
	    public void updateServerPages() {
	    	serverpages = new ArrayList<Page>();
	    	int amount = 0;
	    	int page = 1;
	    	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	    	for (DestiWarp warp : API.a().server().warpList()) {
	    		ItemStack i = warp.getDisplayItem().clone();
	    		ItemMeta im = i.getItemMeta();
	    		NamespacedKey key = new NamespacedKey(API.a(), "warps");
	    		im.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, warp.getName());
	    		im.setDisplayName(color("&f" + warp.getName(), false));
	    		ArrayList<String> lore = new ArrayList<String>();
	    		lore.add(color("&fOwner: Server", false));
	    		im.setLore(lore);
	    		i.setItemMeta(im);
	    		amount++;
	    		items.add(i);
	    		if (amount >= 45) {
	    			serverpages.add(new Page(PageType.SERVER_WARP, page, items));
	    			amount = 0;
	    			page++;
	    			items = new ArrayList<ItemStack>();
	    		}
	    	}
	    	if (amount <= 45) {
    			serverpages.add(new Page(PageType.SERVER_WARP, page, items));
    		}
	    	
	    }
	    
	    public void updatePlayerPages() {
	    	playerpages = new ArrayList<Page>();
	    	int amount = 0;
	    	int page = 1;
	    	ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	    	for (DestiPlayerWarp warp : API.a().server().playerWarpList()) {
	    		ItemStack i = warp.getDisplayItem().clone();
	    		ItemMeta im = i.getItemMeta();
	    		NamespacedKey key = new NamespacedKey(API.a(), "playerwarps");
	    		im.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, warp.getName());
	    		im.setDisplayName(color("&f" + warp.getName(), false));
	    		ArrayList<String> lore = new ArrayList<String>();
	    		lore.add(color("&fOwner: " + warp.getOwner().getName(), false));
	    		im.setLore(lore);
	    		i.setItemMeta(im);
	    		amount++;
	    		items.add(i);
	    		if (amount >= 45) {
	    			playerpages.add(new Page(PageType.PLAYER_WARP, page, items));
	    			amount = 0;
	    			page++;
	    			items = new ArrayList<ItemStack>();
	    		}
	    	}
	    	if (amount <= 45) {
	    		playerpages.add(new Page(PageType.PLAYER_WARP, page, items));
    		}
	    	
	    }

    	String offlinenamefromuuid = "error";
	    private String getOfflineNameFromUUID(String uuid) {
	    	Bukkit.getScheduler().runTaskAsynchronously(pl, new Runnable() {
	    		@Override
	    		public void run() {
	    	         try {
	    	        	 URL url = new URL("https://api.covid19api.com/summary");

	    	        	 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    	        	 conn.setRequestMethod("GET");
	    	        	 conn.connect();

	    	        	 //Getting the response code
	    	        	 int responsecode = conn.getResponseCode();
	    	        	 if (responsecode != 200) {
	    	        		    throw new RuntimeException("HttpResponseCode: " + responsecode);
	    	        		} else {
	    	        		  
	    	        		    String inline = "";
	    	        		    Scanner scanner = new Scanner(url.openStream());
	    	        		  
	    	        		   //Write all the JSON data into a string using a scanner
	    	        		    while (scanner.hasNext()) {
	    	        		       inline += scanner.nextLine();
	    	        		    }
	    	        		    
	    	        		    //Close the scanner
	    	        		    scanner.close();

	    	        		    //Using the JSON simple library parse the string into a json object
	    	        		    JSONParser parse = new JSONParser();
	    	        		    JSONObject data_obj = (JSONObject) parse.parse(inline);

	    	        		    //Get the required object from the above created object
	    	        		    JSONObject obj = (JSONObject) data_obj.get("name");

	    	        		    //Get the required data using its key
	    	        		    offlinenamefromuuid = obj.toString();
	    	        		}
					} catch (IOException | ParseException e) {
						e.printStackTrace();
					}
	    	            
			        offlinenamefromuuid = "error";
	    		}
	    	});
	    	return offlinenamefromuuid;
	    }
	    
	    public void openWarpMenuGUI(DestiPlayer p) { 
	    	Inventory inv = Bukkit.createInventory(p, config.WARPMENU_SIZE, color(config.WARPMENU_TITLE, true));
	    	for (String s : config.WARPMENU_ITEMS.getKeys(false)) {
	    		int slot = Integer.valueOf(s);
	    		ItemStack item = new ItemStack(Material.valueOf(ConfigYAML.a().getConfig().getString("WarpMenu.Items." + s + ".Material")));
	    		String destination = ConfigYAML.a().getConfig().getString("WarpMenu.Items." + s + ".Destination");
	    		String displayName = ConfigYAML.a().getConfig().getString("WarpMenu.Items." + s + ".DisplayName");
	    		ItemMeta im = item.getItemMeta();
	    		NamespacedKey key = new NamespacedKey(API.a(), "warpmenu");
	    		im.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, destination);
	    		im.setDisplayName(color(displayName, true));
	    		ArrayList<String> newlore = new ArrayList<String>();
	    		ConfigYAML.a().getConfig().getStringList("WarpMenu.Items." + s + ".Lore").forEach(st -> newlore.add(color(st, true)));
	    		im.setLore(newlore);
	    		item.setItemMeta(im);
	    		inv.setItem(slot - 1, item);
	    	}
	    	p.openInventory(inv);
	    }
	    
	    public void openPlayerWarpGUI(DestiPlayer p, int pagenumber) { 
	    	Inventory inv = Bukkit.createInventory(p, 54, "Player Warps Page " + pagenumber);
	    	if ((playerpages.size()) > pagenumber) {fillInv(inv, true, false);}
	    	else if ((playerpages.size()) <= pagenumber && pagenumber != 1) {fillInv(inv, false, true);}
	    	else if ((playerpages.size()) > pagenumber && (playerpages.size()) <= pagenumber && pagenumber != 1) {fillInv(inv, true, true);}
	    	else {fillInv(inv, false, false);}
	    	for (Page page : playerpages) {
	    		if (page.getNumber() == pagenumber) {
	    			for (ItemStack items : page.getItems()) {
	    				inv.addItem(items);
	    			}
	    			break;
	    		}
	    	}
	    	p.openInventory(inv);
	    }

	    public void openServerWarpGUI(DestiPlayer p, int pagenumber) { 
	    	Inventory inv = Bukkit.createInventory(p, 54, "Server Warps Page " + pagenumber);
	    	if ((serverpages.size()) > pagenumber) {fillInv(inv, true, false);}
	    	else if ((serverpages.size()) <= pagenumber && pagenumber != 1) {fillInv(inv, false, true);}
	    	else if ((serverpages.size()) > pagenumber && (serverpages.size()) <= pagenumber && pagenumber != 1) {fillInv(inv, true, true);}
	    	else {fillInv(inv, false, false);}
	    	for (Page page : serverpages) {
	    		if (page.getNumber() == pagenumber) {
	    			for (ItemStack items : page.getItems()) {
	    				inv.addItem(items);
	    			}
	    			break;
	    		}
	    	}
	    	p.openInventory(inv);
	    }
	    
	    private void fillInv(Inventory inv, boolean nextpage, boolean previouspage) {
	    	for (int i = 45; i <= 53; i++)  inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
	    	if (nextpage) {
	    		ItemStack item = new ItemStack(Material.PAPER);
	    		ItemMeta itemmeta = item.getItemMeta();
	    		itemmeta.setDisplayName(color("&aNext Page", false));
	    		NamespacedKey key = new NamespacedKey(API.a(), "page");
	    		itemmeta.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, "next");
	    		item.setItemMeta(itemmeta);
	    		inv.setItem(50, item);
	    	}
	    	if (previouspage) {
	    		ItemStack item = new ItemStack(Material.PAPER);
	    		ItemMeta itemmeta = item.getItemMeta();
	    		itemmeta.setDisplayName(color("&aPrevious Page", false));
	    		NamespacedKey key = new NamespacedKey(API.a(), "page");
	    		itemmeta.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, "previous");
	    		item.setItemMeta(itemmeta);
	    		inv.setItem(48, item);
	    	}
	    }
	    
	    private String color(String message, boolean hex) {
	    	if (hex == true) {
		    	char COLOR_CHAR = ChatColor.COLOR_CHAR;
		    	Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
		        Matcher matcher = HEX_PATTERN.matcher(message);
		        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
		        while (matcher.find()) {
		            String group = matcher.group(1);
		            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
		                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
		                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
		                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
		            );
		        }
		        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	    	} else {
	    		return ChatColor.translateAlternateColorCodes('&', message);
	    	}
	    }
}
	
