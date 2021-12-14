package com.desticube;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.desticube.interfaces.Defaults;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPIExpansion extends PlaceholderExpansion implements Defaults {

    private final API plugin;
    
    public PAPIExpansion(API plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor() {
        return "GamerDuck123";
    }
    
    @Override
    public String getIdentifier() {
        return "desticore";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.contains("kitcooldown")){
        	String[] split = params.split("_");
        	Player p = null;
        	if (player != null) { p = (Player) player; }
            return getDestiPlayer(p).getTimeTillNextKit(getDestiServer().getKit(split[1]));
        }
        return null; // Placeholder is unknown by the Expansion
    }
}
