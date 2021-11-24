package com.desticube.timers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.desticube.API;
import com.desticube.interfaces.Defaults;

public class TpaRequestRemove implements Defaults {
	public TpaRequestRemove() { }
    static TpaRequestRemove instance = new TpaRequestRemove();
    public static TpaRequestRemove a() {return instance;}
    BukkitRunnable timer;
    Plugin pl;
    public void setup(Plugin pl) {
    	this.pl = pl;
    }
    
	public void startDeleteTimer(Player p, Player target, Integer seconds) {
		timer = new TpaLocalTimer(p, target, seconds);
		timer.runTaskTimer(API.a(), 20, 20);
	}
}
class TpaLocalTimer extends BukkitRunnable implements Defaults {
	Player p;
	Player target;
	Integer seconds;
	public TpaLocalTimer(Player p, Player target, Integer seconds) {
		this.p = p;
		this.target = target;
		this.seconds = seconds;
	}
	@Override
	public void run() {
    	if (seconds != 0) {
        	if (!getDestiPlayer(p).hasSentTpRequest()) this.cancel();
    		seconds--;
    	} else {
    		p.sendMessage("Teleport canceled by timer");
    		getDestiPlayer(p).removeSentTpRequest(target);
    		getDestiPlayer(target).removeRecievedTpRequest(p);
    		this.cancel();
    	}
	}
	
}