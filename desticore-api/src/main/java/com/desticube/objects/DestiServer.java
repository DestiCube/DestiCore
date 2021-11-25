package com.desticube.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.desticube.annotations.Command;
import com.desticube.configs.ConfigYAML;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.handlers.VaultHandler;
import com.desticube.timers.TPSTimer;

public class DestiServer implements Listener {
	Server server;
	HashMap<Command, String> cmds;
	static HashMap<Player, DestiPlayer> onlineplayers;
	static Location spawn;
	ArrayList<DestiWarp> warps;
	ArrayList<DestiPlayerWarp> playerwarps;
	ConfigYAML configs = ConfigYAML.a();
	ArrayList<DestiKit> kits;
	public DestiServer(Server server, Plugin pl) {
		this.server = server;
		server.getPluginManager().registerEvents(this, pl);
		onlineplayers = new HashMap<Player, DestiPlayer>();
		warps = new ArrayList<DestiWarp>();
		playerwarps = new ArrayList<DestiPlayerWarp>();
		kits = new ArrayList<DestiKit>();
		cmds = new HashMap<Command, String>();
		spawn = null;
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
				ItemStack displayItem = itemStackFromBase64(configs.getLocations().getString("Warps." + name + ".displayitem"));
				warps.add(new DestiWarp(name, loc, displayItem));
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
				ItemStack displayItem = itemStackFromBase64(configs.getLocations().getString("PlayerWarps." + name + ".displayitem"));
				Player owner = Bukkit.getPlayer(configs.getLocations().getString("PlayerWarps." + name + ".owner"));
				playerwarps.add(new DestiPlayerWarp(owner, name, loc, displayItem));
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
				ItemStack displayItem = itemStackFromBase64(configs.getLocations().getString("Warps." + name + ".displayitem"));
				warps.add(new DestiWarp(name, loc, displayItem));
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
				ItemStack displayItem = itemStackFromBase64(configs.getLocations().getString("PlayerWarps." + name + ".displayitem"));
				Player owner = Bukkit.getPlayer(configs.getLocations().getString("PlayerWarps." + name + ".owner"));
				playerwarps.add(new DestiPlayerWarp(owner, name, loc, displayItem));
			}
		}
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
		configs.getLocations().set("Warps." + name + ".displayitem", itemStackToBase64(displayItem));
		configs.getLocations().set("Warps." + name + ".world", world);
		configs.getLocations().set("Warps." + name + ".x", x);
		configs.getLocations().set("Warps." + name + ".y", y);
		configs.getLocations().set("Warps." + name + ".z", z);
		configs.getLocations().set("Warps." + name + ".yaw", yaw);
		configs.getLocations().set("Warps." + name + ".pitch", pitch);
		configs.saveLocations();
		configs.reloadLocations();
		warps.add(new DestiWarp(name, loc, displayItem));
	}
	public boolean delWarp(DestiWarp warp) {
		if (warps.contains(warp)) {
			configs.getLocations().set("Warps." + warp.getName(), null);
			configs.saveLocations();
			configs.reloadLocations();
			warps.remove(warp);
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
		configs.getLocations().set("PlayerWarps." + name + ".owner", owner.getUniqueId());
		configs.getLocations().set("PlayerWarps." + name + ".displayitem", itemStackToBase64(displayItem));
		configs.getLocations().set("PlayerWarps." + name + ".world", world);
		configs.getLocations().set("PlayerWarps." + name + ".x", x);
		configs.getLocations().set("PlayerWarps." + name + ".y", y);
		configs.getLocations().set("PlayerWarps." + name + ".z", z);
		configs.getLocations().set("PlayerWarps." + name + ".yaw", yaw);
		configs.getLocations().set("PlayerWarps." + name + ".pitch", pitch);
		configs.saveLocations();
		configs.reloadLocations();
		playerwarps.add(new DestiPlayerWarp(owner, name, loc, displayItem));
	}
	public boolean delPlayerWarp(DestiPlayerWarp warp) {
		if (playerwarps.contains(warp)) {
			configs.getLocations().set("PlayerWarps." + warp.getName(), null);
			configs.saveLocations();
			configs.reloadLocations();
			playerwarps.remove(warp);
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
	     
	    private String itemStackToBase64(ItemStack item) {
	    	try {
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
	            
//	            // Write the size of the inventory
//	            dataOutput.writeInt(items.length);
	            
	            // Save every element in the list
	            dataOutput.writeObject(item);
	            
	            // Serialize that array
	            dataOutput.close();
	            return Base64Coder.encodeLines(outputStream.toByteArray());
	        } catch (Exception e) {
	            throw new IllegalStateException("Unable to save item stacks.", e);
	        }
	    }

	    private ItemStack itemStackFromBase64(String data) {
	    	try {
	            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
	            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
	            ItemStack[] items = new ItemStack[dataInput.readInt()];
	    
	            // Read the serialized inventory
	            for (int i = 0; i < items.length; i++) {
	            	items[i] = (ItemStack) dataInput.readObject();
	            }
	            
	            dataInput.close();
	            return items[0];
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
}
	
