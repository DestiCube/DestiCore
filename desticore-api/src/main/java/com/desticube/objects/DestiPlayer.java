package com.desticube.objects;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;

import com.desticube.API;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.economy.EconomyHandler;
import com.desticube.storage.PlayerDB;
import com.desticube.storage.PlayerData;
import com.desticube.timers.TeleportWarmup;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class DestiPlayer implements InventoryHolder {
	Player p;
	UUID uuid;
	LocalDateTime logintime;
	DestiPlayer previousmessage;
	boolean godmode;
	boolean tptoggled;
	boolean socialspy;
	String nickname;
//	String badge;
	ArrayList<String> ignored;
	Location backloc;
	ArrayList<DestiHome> homes;
	HashMap<DestiKit, LocalDateTime> usedkits;
	public DestiPlayer(Player p) {
		this.p = p;
		uuid = p.getUniqueId();
		logintime = LocalDateTime.now();
		previousmessage = null;
		if (PlayerDB.a().hasData(uuid.toString(), "onjoindata")) {
			godmode = PlayerDB.a().getOnJoinGodMode(uuid.toString());
			tptoggled = PlayerDB.a().getOnJoinTpToggled(uuid.toString());
			socialspy = PlayerDB.a().getOnJoinSocialSpy(uuid.toString());
		} else {
			godmode = false;
			tptoggled = true;
			socialspy = false;
		}
		if (PlayerDB.a().hasData(uuid.toString(), "nicknames")) {
			nickname = PlayerDB.a().getNickName(uuid.toString());
		} else {
			nickname = p.getName();
		}
		if (nickname == null) nickname = p.getName();
//		if (Bukkit.getServer().getPluginManager().getPlugin("DestiBadges") != null) {
//			if (PlayerDB.a().hasData(uuid.toString(), "badges")) {
//				badge = PlayerDB.a().getBadge(uuid.toString());
//			} else {
//				badge = null;
//			}
//		}
		backloc = null;
		if (PlayerDB.a().hasData(uuid.toString(), "ignoredplayers")) {
			ignored = PlayerDB.a().getIgnoredPlayers(uuid.toString());
		} else {
			ignored = new ArrayList<String>();
		}
		if (PlayerDB.a().hasData(uuid.toString(), "homes")) {
			homes = new ArrayList<DestiHome>();
			PlayerDB.a().getHomes(uuid.toString()).forEach(home -> {
				DestiHome newhome = new DestiHome(home, PlayerDB.a().getHomeLocation(uuid.toString(), home));
				homes.add(newhome);
			});;
		} else {
			homes = new ArrayList<DestiHome>();
		}
		if (PlayerDB.a().hasData(uuid.toString(), "kitdata")) {
			usedkits = new HashMap<DestiKit, LocalDateTime>();
			PlayerDB.a().getUsedKits(uuid.toString()).forEach(kit -> {
				if (API.a().server().kitExists(kit)) {
					usedkits.put(API.a().server().getKit(kit), PlayerDB.a().getKitUse(uuid.toString(), kit));
				}
			});
		} else {
			usedkits = new HashMap<DestiKit, LocalDateTime>();
		}
	}
	
	public HashMap<DestiKit, LocalDateTime> getUsedKitsWtihTime() {return usedkits;}
	public Set<DestiKit> getUsedKits() {return usedkits.keySet();}
	public LocalDateTime getTimeSinceKitUsed(DestiKit kit) {return usedkits.get(kit);}
	public void setKitUsed(DestiKit kit, LocalDateTime value) {usedkits.put(kit, value);}
	public boolean canUseKit(DestiKit kit) {
		if (!usedkits.containsKey(kit)) { return true; } else {
			LocalDateTime usedat = usedkits.get(kit);
			if (ChronoUnit.SECONDS.between(usedat, LocalDateTime.now()) >= kit.getCooldown()) {
				return true;
			} else {
				return false;
			}
		}
	}
	public String getTimeTillNextKit(DestiKit kit) {
		if (!usedkits.containsKey(kit)) { return "null"; }
		StringBuilder builder = new StringBuilder();
		long uptime = (kit.getCooldown() - ChronoUnit.SECONDS.between(usedkits.get(kit), LocalDateTime.now())) * 1000;
		builder.append((TimeUnit.MILLISECONDS.toDays(uptime) > 0) ? TimeUnit.MILLISECONDS.toDays(uptime) + "D " : "");
		uptime -= TimeUnit.DAYS.toMillis(TimeUnit.MILLISECONDS.toDays(uptime));
		
		builder.append((TimeUnit.MILLISECONDS.toHours(uptime) > 0) ? TimeUnit.MILLISECONDS.toHours(uptime) + "H ": "");
		uptime -= TimeUnit.HOURS.toMillis(TimeUnit.MILLISECONDS.toHours(uptime));

		builder.append((TimeUnit.MILLISECONDS.toMinutes(uptime) > 0) ? TimeUnit.MILLISECONDS.toMinutes(uptime) + "M " : "");
		uptime -= TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(uptime));

		builder.append((TimeUnit.MILLISECONDS.toSeconds(uptime) > 0) ? TimeUnit.MILLISECONDS.toSeconds(uptime) + "S ": "");
		return builder.toString();
	}
	
	public boolean getGodMode() {return godmode;}
	public void setGodMode(Boolean b) {godmode = b;}
	public boolean getTpToggled() {return tptoggled;}
	public void setTpToggled(Boolean b) {tptoggled = b;}
	public boolean getSocialSpy() {return socialspy;}
	public void setSocialspy(Boolean b) {socialspy = b;}
	
	public boolean isOnline() {return p.isOnline();}
	
	public UUID getUUID() {return uuid;}
	public Player getPlayer() {return p;}
	public String getName() {
		return nickname;	
	}
	public String getRealName() {
		return p.getName();
	}

	public double getBalance() {return EconomyHandler.a().getAccounts().getAccount(p).getBalance();}
	public void addBalance(double amount) {EconomyHandler.a().getAccounts().getAccount(p).setBalance(this.getBalance() + amount);}
	public void subBalance(double amount) {EconomyHandler.a().getAccounts().getAccount(p).setBalance(this.getBalance() - amount);}
	public void setBalance(double amount) {EconomyHandler.a().getAccounts().getAccount(p).setBalance(amount);}
	public void updateAccount() {EconomyHandler.a().updateAccountAsync(EconomyHandler.a().getAccounts().getAccount(p));}
	
	public boolean sendMessage(String msg) {p.sendMessage(translateHexColorCodes(msg)); return true;}
	public boolean sendMessage(String msg, String... replacements) {//TODO
		 return false;
	}
	public boolean sendMessage(ArrayList<String> msgs) {
		msgs.forEach(s -> p.sendMessage(translateHexColorCodes(s)));
		return true;
	}
	public boolean sendMessage(List<String> msgs) {
		msgs.forEach(s -> p.sendMessage(translateHexColorCodes(s)));
		return true;
	}
	public boolean sendMessage(TextComponent text) {p.spigot().sendMessage(text); return true;}
	
	public boolean hasPermission(Permission perm) {return p.hasPermission(perm);}
	public boolean hasPermission(String perm) {return p.hasPermission(perm);}
	public void setGameMode(GameMode gamemode) {p.setGameMode(gamemode);}
	public GameMode getGameMode() {return p.getGameMode();}
	public void setFlying(boolean fly) {p.setFlying(fly);}
	public boolean isFlying() {return p.isFlying();}
	public boolean isAllowFlight() {return p.getAllowFlight();}
	public void setAllowFlight(boolean flight) {p.setAllowFlight(flight);}
	public boolean isOp() {return p.isOp();}
	public boolean isOnGround() {return p.isOnGround();}
	public void setWalkSpeed(float value) {p.setWalkSpeed(value);}
	public void setFlySpeed(float value) {p.setFlySpeed(value);}
	
	public LocalDateTime getLastLogin() {return logintime;}
	public Block getTargetBlock(Set<Material> transparent, int distance) {return p.getTargetBlock(transparent, distance);}
	
	public ItemStack getItemInMainHand() {return p.getInventory().getItemInMainHand();}
	public PlayerInventory getInventory() {return p.getInventory();}
	public ItemStack[] getArmorContents() {return p.getInventory().getArmorContents();}
	public ItemStack[] getInventoryContents() {return p.getInventory().getContents();}
	public Inventory getEnderChest() {return p.getEnderChest();}
	public void openInventory(Inventory inventory) {p.openInventory(inventory);}
	
	public void openEnderChest() {p.openInventory(p.getEnderChest());}
	public void openWorkBench(Location location, boolean force) {p.openWorkbench(location, force);}
	public void openVirtualWorkBench() {p.openWorkbench(null, true);}
	public void openVirtualStoneCutter() {p.openInventory(Bukkit.createInventory(null, InventoryType.STONECUTTER));}
	public void openVirtualSmithingTable() {p.openInventory(Bukkit.createInventory(null, InventoryType.SMITHING));}
	public void openVirtualCartTable() {p.openInventory(Bukkit.createInventory(null, InventoryType.CARTOGRAPHY));}
	public void openVirtualGrindStone() {p.openInventory(Bukkit.createInventory(null, InventoryType.GRINDSTONE));}
	public void openVirtualLoom() {p.openInventory(Bukkit.createInventory(null, InventoryType.LOOM));}
	public void openVirtualAnvil() {p.openInventory(Bukkit.createInventory(null, InventoryType.ANVIL));}
	
	public void setPlayerWeather(WeatherType weather) {p.setPlayerWeather(weather);}
	public void resetPlayerWeather() {p.resetPlayerWeather();}
	public void setPlayerTime(long time, boolean relative) {p.setPlayerTime(time, relative);}
	public void resetPlayerTime() {p.resetPlayerTime();}
	
	public void setStatistic(Statistic stat, int value) {p.setStatistic(stat, value);}
	public Integer getStatistic(Statistic stat) {return p.getStatistic(stat);}
	
	public Location getLocation() {return p.getLocation();}
	public void teleport(Location loc) {
			if (p.hasPermission("desticore.teleport.instant")) { 
				p.teleport(loc);
				sendMessage(msg.GENERAL_TELEPORTING);
			}
			else TeleportWarmup.a().startWarmUp(p, loc, config.GENERAL_TELEPORT_WARMUP);
		}
	public void teleport(Entity ent) {
		if (p.hasPermission("desticore.teleport.instant")) { 
			p.teleport(ent, TeleportCause.PLUGIN);
			sendMessage(msg.GENERAL_TELEPORTING);
		}
		else TeleportWarmup.a().startWarmUp(p, ent.getLocation(), config.GENERAL_TELEPORT_WARMUP);
	}
	public void teleportWithNoDelay(Location loc) {p.teleport(loc);
	}
	public World getWorld() {return p.getWorld();}
	public Block getTopBlockAtLoc() {return p.getWorld().getHighestBlockAt(p.getLocation());}
	public Vector getDirection() {return p.getLocation().getDirection();}
	public List<Entity> getNearyByEntities(double x, double y, double z) {return p.getNearbyEntities(x, y, z);}
	public List<Player> getNearyByPlayers(double x, double y, double z) {
		ArrayList<Player> players = new ArrayList<Player>();
		p.getNearbyEntities(x, y, z).forEach(ent -> {
			if (ent instanceof Player) players.add((Player) ent);
		});
		return players;
	}
	
	public void heal() {p.setHealth(p.getMaxHealth());}
	public void feed() { p.setFoodLevel(20);}
	public void healAll() {
		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(20);
	}
	public void kill() {p.setHealth(0);}
	
	public Integer homeAmount() {return homes.size();}
	public boolean isHomeSet(String name) {
		for (DestiHome home : homes) {
			if (home.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	public Location getHomeLocation(String name) {
		for (DestiHome home :homes) {
			if (home.getName().equalsIgnoreCase(name)) {
				return home.getLocation();
			}
		}
		return null;
	}
	public ArrayList<DestiHome> getHomes() {return homes;}
	public boolean setHome(String name) {
		DestiHome home = new DestiHome(name, p.getLocation());
		homes.add(home);
		return homes.contains(home);
	}
	public boolean removeHome(String name) {
		for (DestiHome destihome : homes) {
			if (destihome.getName().equalsIgnoreCase(name)) {
				homes.remove(destihome);
				return !homes.contains(destihome);
			}
		}
		return false;
	}
	
	public DestiPlayer getPreviousMessage() {return previousmessage;}
	public void setPreviousMessage(DestiPlayer p) {previousmessage = p;}
	public int getPing() {
		return p.getPing();
	}
	public Location getBackLocation() {return backloc;}
	public void setBackLocation(Location value) {backloc = value;}
	
	public boolean hasSentTpRequest() {return PlayerData.a().senttprequests().get(p) != null;}
	public Player getSentTpRequest() {return PlayerData.a().senttprequests().get(p);}
	public void setSentTpRequest(Player pl) {PlayerData.a().senttprequests().put(p, pl);}
	public void removeSentTpRequest(Player pl) {PlayerData.a().senttprequests().remove(p);}

	public boolean hasRecievedTpRequest() {return PlayerData.a().recievedtprequests().get(p) != null;}
	public Player getRecievedTpRequest() {return PlayerData.a().recievedtprequests().get(p);}
	public void setRecievedTpRequest(Player pl) {PlayerData.a().recievedtprequests().put(p, pl);}
	public void removeRecievedTpRequest(Player pl) {PlayerData.a().recievedtprequests().remove(p);}
	
	public boolean hasSentTpHereRequest() {return PlayerData.a().senttphererequests().get(p) != null;}
	public Player getSentTpHereRequest() {return PlayerData.a().senttphererequests().get(p);}
	public void setSentTpHereRequest(Player pl) {PlayerData.a().senttphererequests().put(p, pl);}
	public void removeSentTpHereRequest(Player pl) {PlayerData.a().senttphererequests().remove(p);}

	public boolean hasRecievedTpHereRequest() {return PlayerData.a().recievedtphererequests().get(p) != null;}
	public Player getRecievedTpHereRequest() {return PlayerData.a().recievedtphererequests().get(p);}
	public void setRecievedTpHereRequest(Player pl) {PlayerData.a().recievedtphererequests().put(p, pl);}
	public void removeRecievedTpHereRequest(Player pl) {PlayerData.a().recievedtphererequests().remove(p);}
	
	public void addIgnored(Player p) {ignored.add(p.getName());}
	public void removeIgnored(Player p) {ignored.remove(p.getName());}
	public boolean hasIgnored(Player p) {return ignored.contains(p.getName());}
	public ArrayList<String> getIgnoredPlayers() {return ignored;}
	
	public String getNickName() {return nickname;}
	public void setNickName(String s) {
		nickname = s + "&r";
		p.setDisplayName(translateHexColorCodes(nickname));
	}
//	
//	public String getBadge() {return nickname;}
//	public void setBadge(String s) {
//		badge = s + "&r";
//	}
	
	public void kick() {p.kickPlayer("You have been kicked by an operator");}
	public void kick(String reason) {p.kickPlayer(translateHexColorCodes(reason));}

    public String translateHexColorCodes(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }
    
        return ChatColor.translateAlternateColorCodes('&', message);

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
	        return matcher.appendTail(buffer).toString();
    	} else {
    		return ChatColor.translateAlternateColorCodes('&', message);
    	}
    }
//	//DEFAULT BUKKIT PLAYER THINGS
//    public String getDisplayName() {return p.getDisplayName();}
//    public void setDisplayName(String name) {p.setDisplayName(name);}
//    public String getPlayerListName() {return p.getPlayerListName();}
//    public void setPlayerListName(String name) {p.setPlayerListName(name);}
//    public String getPlayerListHeader() {return p.getPlayerListHeader();}
//    public String getPlayerListFooter() {return p.getPlayerListFooter();}
//    public void setPlayerListHeader(String header) {p.setPlayerListHeader(header);}
//    public void setPlayerListFooter(String footer) {p.setPlayerListFooter(footer);}
//    public void setPlayerListHeaderFooter(String header, String footer) {p.setPlayerListHeaderFooter(header, footer);}
//    public void setCompassTarget(Location loc) {p.setCompassTarget(loc);}
//    public Location getCompassTarget() {return p.getCompassTarget();}
//    public InetSocketAddress getAddress() {return p.getAddress();}
//    public void sendRawMessage(String message) {p.sendRawMessage(message);}
//    public void kickPlayer(String message) {p.kickPlayer(message);}
//    public void chat(String msg) {p.chat(msg);}
//    public boolean performCommand(String command) {return p.performCommand(command);}
//    @Deprecated
//    public boolean isOnGround() {return p.isOnGround();}
//    public boolean isSneaking() {return p.isSneaking();}
//    public void setSneaking(boolean sneak) {p.setSneaking(sneak);}
//    public boolean isSprinting() {return p.isSprinting();}
//    public void setSprinting(boolean sprinting) {p.setSprinting(sprinting);}
//    public void saveData() {p.saveData();}
//    public void loadData() {p.loadData();}
//    public void setSleepingIgnored(boolean isSleeping) {p.setSleepingIgnored(isSleeping);}
//    public boolean isSleepingIgnored() {return p.isSleepingIgnored();}
//    public Location getBedSpawnLocation() {return p.getBedSpawnLocation();}
//    public void setBedSpawnLocation(Location location) {p.setBedSpawnLocation(location);}
//    public void setBedSpawnLocation(Location location, boolean force) {p.setBedSpawnLocation(location, force);}
//    @Deprecated
//    public void playNote(Location loc, byte instrument, byte note) {p.playNote(loc, instrument, note);}
//    public void playNote(Location loc, Instrument instrument, Note note) {p.playNote(loc, instrument, note);}
//    public void playSound(Location location, Sound sound, float volume, float pitch) {p.playSound(location, sound, volume, pitch);}
//    public void playSound(Location location, String sound, float volume, float pitch) {p.playSound(location, sound, volume, pitch);}
//    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {p.playSound(location, sound, category, volume, pitch);}
//    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {p.playSound(location, sound, category, volume, pitch);}
//    public void stopSound(Sound sound) {p.stopSound(sound);}
//    public void stopSound(String sound) {p.stopSound(sound);}
//    public void stopSound(Sound sound, SoundCategory category) {p.stopSound(sound, category);}
//    public void stopSound(String sound, SoundCategory category) {p.stopSound(sound, category);}
//    @Deprecated
//    public void playEffect(Location loc, Effect effect, int data) {p.playEffect(loc, effect, data);;}
//    public <T> void playEffect(Location loc, Effect effect, T data) {p.playEffect(loc, effect, data);}
//    public boolean breakBlock(Block block) {return p.breakBlock(block);}
//    @Deprecated
//    public void sendBlockChange(Location loc, Material material, byte data) {p.sendBlockChange(loc, material, data);}
//    public void sendBlockChange(Location loc, BlockData block) {p.sendBlockChange(loc, block);}
//    public void sendBlockDamage(Location loc, float progress) {p.sendBlockDamage(loc, progress);}
//    @Deprecated
//    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {return p.sendChunkChange(loc, sx, sy, sz, data);}
//    public void sendSignChange(Location loc, String[] lines) {p.sendSignChange(loc, lines);}
//    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor) {p.sendSignChange(loc, lines, dyeColor);}
//    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor, boolean hasGlowingText) {p.sendSignChange(loc, lines, dyeColor, hasGlowingText);}
//    public void sendMap(MapView map) {p.sendMap(map);}
//    @Deprecated
//    public void updateInventory() {p.updateInventory();}
//    public void setPlayerTime(long time, boolean relative) {p.setPlayerTime(time, relative);}
//    public long getPlayerTime() {return p.getPlayerTime();}
//    public long getPlayerTimeOffset() {return p.getPlayerTimeOffset();}
//    public boolean isPlayerTimeRelative() {return p.isPlayerTimeRelative();}
//    public void resetPlayerTime() {p.resetPlayerTime();}
//    public void setPlayerWeather(WeatherType type) {p.setPlayerWeather(type);}
//    public WeatherType getPlayerWeather() {return p.getPlayerWeather();}
//    public void resetPlayerWeather() {p.resetPlayerWeather();}
//    public void giveExp(int amount) {p.giveExp(amount);}
//    public void giveExpLevels(int amount) {p.giveExpLevels(amount);}
//    public float getExp() {return p.getExp();}
//    public void setExp(float exp) {p.setExp(exp);}
//    public int getLevel() {return p.getLevel();}
//    public void setLevel(int level) {p.setLevel(level);}
//    public int getTotalExperience() {return p.getTotalExperience();}
//    public void setTotalExperience(int exp) {p.setTotalExperience(exp);}
//    public void sendExperienceChange(float progress) {p.sendExperienceChange(progress);}
//    public void sendExperienceChange(float progress, int level) {p.sendExperienceChange(progress, level);}
//    public boolean getAllowFlight() {return p.getAllowFlight();}
//    public void setAllowFlight(boolean flight) {p.setAllowFlight(flight);}
//    @Deprecated
//    public void hidePlayer(Player player) {p.hidePlayer(player);}
//    public void hidePlayer(Plugin plugin, Player player) {p.hidePlayer(plugin, player);}
//    @Deprecated
//    public void showPlayer(Player player) {p.showPlayer(player);}
//    public void showPlayer(Plugin plugin, Player player) {p.showPlayer(plugin, player);}
//    public boolean canSee(Player player) {return p.canSee(player);}
//    public boolean isFlying() {return p.isFlying();}
//    public void setFlying(boolean value) {p.setFlying(value);}
//    public void setFlySpeed(float value) {p.setFlySpeed(value);}
//    public void setWalkSpeed(float value) {p.setWalkSpeed(value);}
//    public float getFlySpeed() {return p.getFlySpeed();}
//    public float getWalkSpeed() {return p.getWalkSpeed();}
//    @Deprecated
//    public void setTexturePack(String url) {p.setTexturePack(url);}
//    public void setResourcePack(String url) {p.setResourcePack(url);}
//    public void setResourcePack(String url, byte[] hash) {p.setResourcePack(url, hash);}
//    public Scoreboard getScoreboard() {return p.getScoreboard();}
//    public void setScoreboard(Scoreboard scoreboard) {p.setScoreboard(scoreboard);}
//    public boolean isHealthScaled() {return p.isHealthScaled();}
//    public void setHealthScaled(boolean scale) {p.setHealthScaled(scale);}
//    public void setHealthScale(double scale) {p.setHealthScale(scale);}
//    public double getHealthScale() {return p.getHealthScale();}
//    public Entity getSpectatorTarget() {return p.getSpectatorTarget();}
//    public void setSpectatorTarget(Entity entity) {p.setSpectatorTarget(entity);}
//    @Deprecated
//    public void sendTitle(String title, String subtitle) {p.sendTitle(title, subtitle);}
//    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);}
//    public void resetTitle() {p.resetTitle();}
//    public void spawnParticle(Particle particle, Location location, int count) {p.spawnParticle(particle, location, count);}
//    public void spawnParticle(Particle particle, double x, double y, double z, int count) {p.spawnParticle(particle, x, y, z, count);}
//    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {p.spawnParticle(particle, location, count, data);}
//    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {p.spawnParticle(particle, x, y, z, count, data);}
//    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {p.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);}
//    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {p.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ);}
//    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {p.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, data);}
//    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {p.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, data);}
//    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {p.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);}
//    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {p.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra);}
//    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {p.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);}
//    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {p.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);}
//    public AdvancementProgress getAdvancementProgress(Advancement advancement) {return p.getAdvancementProgress(advancement);}
//    public int getClientViewDistance() {return p.getClientViewDistance();}
//    public int getPing() {return p.getPing();}
//    public String getLocale() {return p.getLocale();}
//    public void updateCommands() {p.updateCommands();}
//    public void openBook(ItemStack book) {p.openBook(book);}
	
}
