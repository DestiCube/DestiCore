package com.desticube.listeners;

import java.time.LocalDateTime;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import com.desticube.API;
import com.desticube.annotations.Listener;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.interfaces.IListener;
import com.desticube.objects.DestiKit;
import com.desticube.objects.DestiPlayer;

import net.md_5.bungee.api.ChatColor;

@Listener
public class PlayerListener implements IListener, Defaults {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!e.getPlayer().hasPlayedBefore()) {
			for (ItemStack item : getDestiServer().getKit(config.STARTERKIT).getItems()) {
				if (item == null || item.getType() == Material.AIR) { continue; }
				getDestiPlayer(e.getPlayer()).getInventory().addItem(item);
			}
			LocalDateTime time = LocalDateTime.now();
			getDestiPlayer(e.getPlayer()).setKitUsed(getDestiServer().getKit(config.STARTERKIT), time);
		}
		e.getPlayer().setDisplayName(color(getDestiPlayer(e.getPlayer()).getNickName() + "&r"));
		for (DestiKit kit : getDestiPlayer(e.getPlayer()).getUsedKits()) {
			LocalDateTime time = getDestiPlayer(e.getPlayer()).getTimeSinceKitUsed(kit);
			e.getPlayer().sendMessage(kit.getName() + " used at " + time.getMonthValue() + "month, " + time.getDayOfMonth() + "day, " 
			+ time.getHour() + "hour, " + time.getMinute() + "minute, "  + time.getSecond() + "second, "); 
		}
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
	

    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
    	if (color(e.getView().getTitle(), true).contains(color(config.WARPMENU_TITLE, true))) {
    		e.setCancelled(true);
    		NamespacedKey key = new NamespacedKey(API.a(), "warpmenu");
    		ItemStack i = e.getCurrentItem();
    		ItemMeta im = i.getItemMeta();
    		CustomItemTagContainer ctc = im.getCustomTagContainer();
    		if (ctc.getCustomTag(key, ItemTagType.STRING).equalsIgnoreCase("SERVER")) {
    			getDestiServer().openServerWarpGUI(getDestiPlayer((Player) e.getWhoClicked()), 1);
    		}
    		else if (ctc.getCustomTag(key, ItemTagType.STRING).equalsIgnoreCase("PLAYER")) {
    			getDestiServer().openPlayerWarpGUI(getDestiPlayer((Player) e.getWhoClicked()), 1);
    		}
    	}
    	if (ChatColor.stripColor(e.getView().getTitle()).contains("Server Warps")) {
    		e.setCancelled(true);
    		NamespacedKey pagekey = new NamespacedKey(API.a(), "page");
    		NamespacedKey key = new NamespacedKey(API.a(), "warps");
    		ItemStack i = e.getCurrentItem();
    		ItemMeta im = i.getItemMeta();
    		if (im.getCustomTagContainer().hasCustomTag(pagekey, ItemTagType.STRING)) {
    			CustomItemTagContainer ctc = im.getCustomTagContainer();
    			String[] split = ChatColor.stripColor(e.getView().getTitle()).split(" ");
    			if (ctc.getCustomTag(pagekey, ItemTagType.STRING).equalsIgnoreCase("next")) {
    				getDestiServer().openServerWarpGUI(getDestiPlayer((Player) e.getWhoClicked()), Integer.valueOf(split[3]) + 1);	
    			}
    			if (ctc.getCustomTag(pagekey, ItemTagType.STRING).equalsIgnoreCase("previous")) {
    				getDestiServer().openServerWarpGUI(getDestiPlayer((Player) e.getWhoClicked()), Integer.valueOf(split[3]) - 1);	
    			}
    			return;
    		}
    		CustomItemTagContainer ctc = im.getCustomTagContainer();
    		getDestiPlayer((Player) e.getWhoClicked()).teleport(API.a().server().getWarp(ctc.getCustomTag(key, ItemTagType.STRING)).getLocation());
    	}
    	if (e.getView().getTitle().contains("Player Warps")) {
    		NamespacedKey pagekey = new NamespacedKey(API.a(), "page");
    		NamespacedKey key = new NamespacedKey(API.a(), "playerwarps");
    		ItemStack i = e.getCurrentItem();
    		ItemMeta im = i.getItemMeta();
    		if (im.getCustomTagContainer().hasCustomTag(pagekey, ItemTagType.STRING)) {
    			CustomItemTagContainer ctc = im.getCustomTagContainer();
    			String[] split = ChatColor.stripColor(e.getView().getTitle()).split(" ");
    			if (ctc.getCustomTag(pagekey, ItemTagType.STRING).equalsIgnoreCase("next")) {
    				getDestiServer().openServerWarpGUI(getDestiPlayer((Player) e.getWhoClicked()), Integer.valueOf(split[3]) + 1);	
    			}
    			if (ctc.getCustomTag(pagekey, ItemTagType.STRING).equalsIgnoreCase("previous")) {
    				getDestiServer().openServerWarpGUI(getDestiPlayer((Player) e.getWhoClicked()), Integer.valueOf(split[3]) - 1);	
    			}
    			return;
    		}
    		CustomItemTagContainer ctc = im.getCustomTagContainer();
    		getDestiPlayer((Player) e.getWhoClicked()).teleport(API.a().server().getPlayerWarp(ctc.getCustomTag(key, ItemTagType.STRING)).getLocation());
    	}
    }
}
