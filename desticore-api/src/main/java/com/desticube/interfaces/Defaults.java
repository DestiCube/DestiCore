package com.desticube.interfaces;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.desticube.API;
import com.desticube.objects.DestiPlayer;
import com.desticube.objects.DestiServer;

import net.md_5.bungee.api.ChatColor;

public interface Defaults {
	public default String color(String s) {return ChatColor.translateAlternateColorCodes('&', s);}
	public default DestiPlayer getDestiPlayer(Player p) {return API.a().server().getDestiPlayer(p);}
	public default DestiServer getDestiServer() {return API.a().server();}
	default public boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

    public default String color(String message, boolean hex) {
    	if (hex == true) {
	    	char COLOR_CHAR = ChatColor.COLOR_CHAR;
	    	Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
	        //Sourced from this post by imDaniX: https://github.com/SpigotMC/BungeeCord/pull/2883#issuecomment-653955600
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
	
}
