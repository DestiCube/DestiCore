package com.desticube.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desticube.DestiMain;
import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.md_5.bungee.api.ChatColor;

@Command(
		command = "skull",
		description = "get that skull",
		aliases = {},
		permissions = {"desticore.skull"})
public class SkullCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.skull")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			getHead(p.getPlayer(), p.getName());
			return p.sendMessage(msg.GENERAL_SKULL_GIVEN.replaceAll("%name%", p.getName()));
		} else if (args.length == 1){
			getHead(p.getPlayer(), args[0]);
			return p.sendMessage(msg.GENERAL_SKULL_GIVEN.replaceAll("%name%", args[0]));
		} 
		return false;
	}

	private void getHead(Player p, String target){
        Bukkit.getScheduler().runTaskAsynchronously(DestiMain.a(), (Runnable) () -> {
            String value;
            value = getHeadValue(target);
            if (value == null){
                value = "";
            }
            ItemStack item = getHead(value);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + target + "'s Head"));
            item.setItemMeta(meta);
            p.getInventory().addItem(item);
        });
 
    }
	private ItemStack getHead(String value) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
    }
	private String getHeadValue(String name){
	    try {
	        String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
	        Gson g = new Gson();
	        JsonObject obj = g.fromJson(result, JsonObject.class);
	        String uid = obj.get("id").toString().replace("\"","");
	        String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
	        obj = g.fromJson(signature, JsonObject.class);
	        String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
	        String decoded = new String(Base64.getDecoder().decode(value));
	        obj = g.fromJson(decoded,JsonObject.class);
	        String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
	        byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
	        return new String(Base64.getEncoder().encode(skinByte));
	    } catch (Exception ignored){ }
	    return null;
	}
	private String getURLContent(String urlStr) {
	        URL url;
	        BufferedReader in = null;
	        StringBuilder sb = new StringBuilder();
	        try{
	            url = new URL(urlStr);
	            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8) );
	            String str;
	            while((str = in.readLine()) != null) {
	                sb.append( str );
	            }
	        } catch (Exception ignored) { }
	        finally{
	            try{
	                if(in!=null) {
	                    in.close();
	                }
	            }catch(IOException ignored) { }
	        }
	        return sb.toString();
	    }
 
}
