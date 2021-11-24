package com.desticube.storage;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.desticube.interfaces.Defaults;
import com.desticube.objects.DestiKit;
import com.desticube.objects.DestiPlayer;

public class PlayerData implements Listener, Defaults {
	public PlayerData() {}
    static PlayerData instance = new PlayerData();
    public static PlayerData a() {return instance;}
    
	public HashMap<Player, Player> senttprequests;
	public HashMap<Player, Player> senttprequests() {return senttprequests;}
	public HashMap<Player, Player> recievedtprequests;
	public HashMap<Player, Player> recievedtprequests() {return recievedtprequests;}
	public HashMap<Player, Player> senttphererequests;
	public HashMap<Player, Player> senttphererequests() {return senttprequests;}
	public HashMap<Player, Player> recievedtphererequests;
	public HashMap<Player, Player> recievedtphererequests() {return recievedtprequests;}
	
	public void setup(Plugin pl) {
		pl.getServer().getPluginManager().registerEvents(instance, pl);
		senttprequests = new HashMap<Player, Player>();
		recievedtprequests = new HashMap<Player, Player>();
		senttphererequests = new HashMap<Player, Player>();
		recievedtphererequests = new HashMap<Player, Player>();
		}
	
	@EventHandler
	public void onJoinEvents(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		getDestiServer().getOnlineDestiPlayers().put(e.getPlayer(), new DestiPlayer(e.getPlayer()));
		PlayerDB.a().onJoin(getDestiPlayer(p));
	}
	
	@EventHandler
	public void onQuitEvents(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		PlayerDB.a().onQuit(p);
		PlayerDB.a().clearHomes(uuid.toString());
		getDestiPlayer(p).getHomes().forEach(destihome -> {
			String name = destihome.getName();
			Location loc = destihome.getLocation();
			PlayerDB.a().setHomesLocation(uuid.toString(), name, loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
			Bukkit.broadcastMessage(destihome.getName());
		});
		
		int god = 0;
		int tptoggled = 0;
		int socialspy = 0;
		if (getDestiPlayer(p).getGodMode()) god = 1;
		if (getDestiPlayer(p).getTpToggled()) tptoggled = 1;
		if (getDestiPlayer(p).getSocialSpy()) socialspy = 1;
		PlayerDB.a().setOnJoinData(uuid.toString(), god, tptoggled, socialspy);
		PlayerDB.a().clearIgnoredPlayers(p.getUniqueId().toString());
		for (String pl : getDestiPlayer(p).getIgnoredPlayers()) {
			PlayerDB.a().setIgnored(p.getUniqueId().toString(), pl);
		}
		PlayerDB.a().clearKits(uuid.toString());
		for (DestiKit kit : getDestiPlayer(p).getUsedKits()) {
			PlayerDB.a().setKitUse(uuid.toString(), kit.getName(), getDestiPlayer(p).getTimeSinceKitUsed(kit));
		}
		PlayerDB.a().deleteNickName(uuid.toString());
		PlayerDB.a().setNickName(uuid.toString(), getDestiPlayer(p).getNickName());
//		PlayerDB.a().deleteBadge(uuid.toString());
//		PlayerDB.a().setBadge(uuid.toString(), getDestiPlayer(p).getBadge());
		// REMOVE PLAYER FROM LIST
		getDestiServer().getOnlineDestiPlayers().remove(e.getPlayer());
	}
	
}
