package com.desticube.listeners;

import java.time.LocalDateTime;
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.desticube.annotations.Listener;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.interfaces.IListener;
import com.desticube.objects.DestiKit;
import com.desticube.objects.DestiPlayer;

@Listener
public class PlayerListener implements IListener, Defaults {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.getPlayer().setDisplayName(color(getDestiPlayer(e.getPlayer()).getNickName() + "&r"));
		for (DestiKit kit : getDestiPlayer(e.getPlayer()).getUsedKits()) {
			LocalDateTime time = getDestiPlayer(e.getPlayer()).getTimeSinceKitUsed(kit);
			e.getPlayer().sendMessage(kit.getName() + " used at " + time.getMonthValue() + "month, " + time.getDayOfMonth() + "day, " 
			+ time.getHour() + "hour, " + time.getMinute() + "minute, "  + time.getSecond() + "second, "); 
		}
	}public PlayerListener() {
		// TODO Auto-generated constructor stub
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		DestiPlayer p = getDestiPlayer(e.getEntity());
		if (p.hasPermission("desticore.back")) {
			p.sendMessage(msg.GENERAL_BACK_DEATH_MESSAGE);
			p.setBackLocation(p.getLocation());
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		DestiPlayer p = getDestiPlayer(e.getPlayer());
		if (p.getHomes() == null || p.getHomes().size() < 1) {e.setRespawnLocation(getDestiServer().getSpawn());}
		else {e.setRespawnLocation(p.getHomeLocation(p.getHomes().get(0).getName()));}
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			DestiPlayer dp = getDestiPlayer(p);
			if (dp.getGodMode()) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChatRecieve(AsyncPlayerChatEvent e) {
		final Iterator<Player> it = e.getRecipients().iterator();
		while (it.hasNext()) {
            final DestiPlayer u = getDestiPlayer(it.next());
            if (u.hasIgnored(e.getPlayer())) {
                it.remove();
            }
        }
	}
}
