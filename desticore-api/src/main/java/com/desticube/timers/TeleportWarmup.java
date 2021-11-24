package com.desticube.timers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.desticube.API;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;

public class TeleportWarmup implements Listener, Defaults {
	public TeleportWarmup() { }
    static TeleportWarmup instance = new TeleportWarmup();
    public static TeleportWarmup a() {return instance;}
    BukkitRunnable timer;
    HashMap<UUID, Boolean> warmupstarted;
    Plugin pl;
    public void setup(Plugin pl) {
    	this.pl = pl;
    	warmupstarted = new HashMap<UUID, Boolean>();
		pl.getServer().getPluginManager().registerEvents(this, pl);
    }
    
	public void startWarmUp(Player p, Location loc, Integer seconds) {
		timer = new LocalTimer(p, loc, seconds);
		timer.runTaskTimer(API.a(), 20, 20);
		warmupstarted.put(p.getUniqueId(), true);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (warmupstarted.get(e.getPlayer().getUniqueId()) == null) return;
		if (warmupstarted.get(e.getPlayer().getUniqueId()) == true) {
			if (e.getFrom().getX() != e.getTo().getX()
					|| e.getFrom().getY() != e.getTo().getY()
					|| e.getFrom().getZ() != e.getTo().getZ()) {
				timer.cancel();
				warmupstarted.remove(e.getPlayer().getUniqueId());
				e.getPlayer().sendMessage(color(msg.GENERAL_TELEPORT_CANCELLED));
			}
		}
	}
}
class LocalTimer extends BukkitRunnable implements Defaults {
	Player p;
	Location loc;
	Integer seconds;
	public LocalTimer(Player p, Location loc, Integer seconds) {
		this.p = p;
		this.loc = loc;
		this.seconds = seconds;
	}
	@Override
	public void run() {
    	if (seconds != 0) {
    		p.sendMessage(color(msg.GENERAL_TELEPORTING_COUNT_DOWN.replaceAll("%seconds%", String.valueOf(seconds))));
    		seconds--;
    	} else {
    		p.sendMessage(color(msg.GENERAL_TELEPORTING));
    		p.teleport(loc, TeleportCause.PLUGIN);
    		TeleportWarmup.a().warmupstarted.remove(p.getUniqueId());
    		this.cancel();
    	}
	}
	
}