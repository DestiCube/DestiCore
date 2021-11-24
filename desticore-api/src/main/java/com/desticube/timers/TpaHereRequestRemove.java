package com.desticube.timers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.desticube.API;
import com.desticube.interfaces.Defaults;

public class TpaHereRequestRemove implements Defaults {
	public TpaHereRequestRemove() { }
    static TpaHereRequestRemove instance = new TpaHereRequestRemove();
    public static TpaHereRequestRemove a() {return instance;}
    BukkitRunnable timer;
    Plugin pl;
    public void setup(Plugin pl) {
    	this.pl = pl;
    }
    
	public void startDeleteTimer(Player p, Player target, Integer seconds) {
		timer = new TpaHereLocalTimer(p, target, seconds);
		timer.runTaskTimer(API.a(), 20, 20);
	}
}
class TpaHereLocalTimer extends BukkitRunnable implements Defaults {
	Player p;
	Player target;
	Integer seconds;
	public TpaHereLocalTimer(Player p, Player target, Integer seconds) {
		this.p = p;
		this.target = target;
		this.seconds = seconds;
	}
	@Override
	public void run() {
    	if (seconds != 0) {
        	if (!getDestiPlayer(p).hasSentTpHereRequest()) this.cancel();
    		seconds--;
    	} else {
    		getDestiPlayer(p).removeSentTpHereRequest(target);
    		getDestiPlayer(target).removeRecievedTpHereRequest(p);
    		this.cancel();
    	}
	}
	
}