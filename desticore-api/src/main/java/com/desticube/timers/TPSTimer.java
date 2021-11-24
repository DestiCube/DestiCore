package com.desticube.timers;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.desticube.API;
import com.desticube.interfaces.Defaults;

public class TPSTimer implements Defaults {
	public TPSTimer() { }
    static TPSTimer instance = new TPSTimer();
    public static TPSTimer a() {return instance;}
    TPSLocalTimer localtimer;
    BukkitRunnable timer;
    Plugin pl;
    public void setup(Plugin pl) {
    	this.pl = pl;
    }
    
	public void startTPSTimer() {
		localtimer = new TPSLocalTimer();
		timer = localtimer;
		timer.runTaskTimer(API.a(), 1000, 50);
	}
	
	public Double getTPS() {
		return localtimer.getTPS();
	}
	
}
class TPSLocalTimer extends BukkitRunnable implements Defaults {
    private final LinkedList<Double> history = new LinkedList<>();
//    private final long maxTime = 10 * 1000000;
    private final long tickInterval = 50;
    private transient long lastPoll = System.nanoTime();
    
	public TPSLocalTimer() {
	}
	@Override
	public void run() {
		final long startTime = System.nanoTime();
//        final long currentTime = System.currentTimeMillis();
        long timeSpent = (startTime - lastPoll) / 1000;
        if (timeSpent == 0) {
            timeSpent = 1;
        }
        if (history.size() > 10) {
            history.remove();
        }
        final double tps = tickInterval * 1000000.0 / timeSpent;
        if (tps <= 21) {
            history.add(tps);
        }
        lastPoll = startTime;
	}
	
	public double getTPS() {
		double avg = 0;
        for (final Double f : history) {
            if (f != null) {
                avg += f;
            }
        }
        Bukkit.broadcastMessage("TPS GOTTEN");
        return avg / history.size();
    }
	
}