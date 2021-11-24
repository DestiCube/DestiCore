package com.desticube.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.DestiMain;
import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
import com.desticube.storage.PlayerDB;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@Command(
		command = "seen",
		description = "I SEEN YOU",
		aliases = {"lastseen"},
		permissions = {"desticore.staffseen", "desticore.seen"})
public class SeenCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		Player target = Bukkit.getPlayer(args[0]);
		if (p.hasPermission("desticore.staffseen")) {
			if (args.length < 1 || args == null) {return p.sendMessage(color("&cCorrect usage: /seen (player)"));}
			if (target != null) {
				TextComponent message = new TextComponent(color(msg.GENERAL_SEEN_FORMAT_ONLINE
						.replaceAll("%player%", target.getName())
						.replaceAll("%time%", getTimeSinceOn(DestiMain.a().server().getDestiPlayer(target)))));
				
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(
						ChatColor.translateAlternateColorCodes('&', msg.GENERAL_SEEN_HOVER_FORMAT
						.replaceAll("%ipaddress%", PlayerDB.a().getGeneralDataIPAddress(target.getUniqueId().toString()))
						.replaceAll("%uuid%", PlayerDB.a().getGeneralDataUUID(target.getUniqueId().toString()))
						.replaceAll("%x%", String.valueOf(target.getLocation().getBlockX()))
						.replaceAll("%y%", String.valueOf(target.getLocation().getBlockY()))
						.replaceAll("%z%", String.valueOf(target.getLocation().getBlockZ()))
						.replace("\\n", "\n")))));
				p.sendMessage(message);
			} else {
				OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
				if (!PlayerDB.a().hasPlayerData(offline.getUniqueId().toString()) || offline == null) {p.sendMessage(msg.GENERAL_PLAYER_NEVER_JOINED.replaceAll("%player%", args[0]));}
				TextComponent message = new TextComponent(color(msg.GENERAL_SEEN_FORMAT_OFFLINE
						.replaceAll("%player%", PlayerDB.a().getGeneralDataName(offline.getUniqueId().toString()))
						.replaceAll("%time%", getTimeSinceOff(offline))));
				
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(
						ChatColor.translateAlternateColorCodes('&', msg.GENERAL_SEEN_HOVER_FORMAT
						.replaceAll("%ipaddress%", PlayerDB.a().getGeneralDataIPAddress(offline.getUniqueId().toString()))
						.replaceAll("%uuid%", PlayerDB.a().getGeneralDataUUID(offline.getUniqueId().toString()))
						.replaceAll("%x%", String.valueOf(PlayerDB.a().getLastLocation(offline.getUniqueId().toString()).getBlockX()))
						.replaceAll("%y%", String.valueOf(PlayerDB.a().getLastLocation(offline.getUniqueId().toString()).getBlockY()))
						.replaceAll("%z%", String.valueOf(PlayerDB.a().getLastLocation(offline.getUniqueId().toString()).getBlockZ()))
						.replace("\\n", "\n")))));
				p.sendMessage(message);
			}
		} else if (p.hasPermission("desticore.seen")) {
			if (args.length < 1 || args == null) {return p.sendMessage(color("&cCorrect usage: /seen (player)"));}
			if (target != null) {
				TextComponent message = new TextComponent(color(msg.GENERAL_SEEN_FORMAT_ONLINE
						.replaceAll("%player%", target.getName())
						.replaceAll("%time%", getTimeSinceOn(DestiMain.a().server().getDestiPlayer(target)))));
				p.sendMessage(message);
			} else {
				
				OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
				if (!PlayerDB.a().hasPlayerData(offline.getUniqueId().toString()) || offline == null) {p.sendMessage(msg.GENERAL_PLAYER_NEVER_JOINED.replaceAll("%player%", args[0]));}
				TextComponent message = new TextComponent(color(msg.GENERAL_SEEN_FORMAT_OFFLINE
						.replaceAll("%player%", PlayerDB.a().getGeneralDataName(offline.getUniqueId().toString()))
						.replaceAll("%time%", getTimeSinceOff(offline))));
				p.sendMessage(message);
			}
		} else {
			return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		}
		return true;
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> players = new ArrayList<String>();
			Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
			return players;
		} else {
			return null;
		}
	}
	
	public String getTimeSinceOn(DestiPlayer p)
	{
		LocalDateTime current = LocalDateTime.now();
		LocalDateTime lastlogin = p.getLastLogin();
		StringBuilder builder = new StringBuilder();
		if ((current.getMonthValue() - lastlogin.getMonthValue()) > 0) { builder.append((current.getMonthValue() - lastlogin.getMonthValue()) + " months "); }
		if ((current.getDayOfMonth() - lastlogin.getDayOfMonth()) > 0) { builder.append((current.getDayOfMonth() - lastlogin.getDayOfMonth()) + " days "); }
		if ((current.getYear() - lastlogin.getYear()) > 0) { builder.append((current.getYear() - lastlogin.getYear()) + " years "); }
		if ((current.getHour() - lastlogin.getHour()) > 0) { builder.append((current.getHour() - lastlogin.getHour()) + " hours "); }
		if ((current.getMinute() - lastlogin.getMinute()) > 0) { builder.append((current.getMinute() - lastlogin.getMinute()) + " minutes "); }
		if ((current.getSecond() - lastlogin.getSecond()) > 0) { builder.append((current.getSecond() - lastlogin.getSecond()) + " seconds"); }
		return builder.toString();
	}
	public String getTimeSinceOff(OfflinePlayer p)
	{
		LocalDateTime current = LocalDateTime.now();
		LocalDateTime lastlogin = PlayerDB.a().getLastLogout(p.getUniqueId().toString());
		StringBuilder builder = new StringBuilder();
		if ((current.getMonthValue() - lastlogin.getMonthValue()) > 0) { builder.append((current.getMonthValue() - lastlogin.getMonthValue()) + " months "); }
		if ((current.getDayOfMonth() - lastlogin.getDayOfMonth()) > 0) { builder.append((current.getDayOfMonth() - lastlogin.getDayOfMonth()) + " days "); }
		if ((current.getYear() - lastlogin.getYear()) > 0) { builder.append((current.getYear() - lastlogin.getYear()) + " years "); }
		if ((current.getHour() - lastlogin.getHour()) > 0) { builder.append((current.getHour() - lastlogin.getHour()) + " hours "); }
		if ((current.getMinute() - lastlogin.getMinute()) > 0) { builder.append((current.getMinute() - lastlogin.getMinute()) + " minutes "); }
		if ((current.getSecond() - lastlogin.getSecond()) > 0) { builder.append((current.getSecond() - lastlogin.getSecond()) + " seconds"); }
		return builder.toString();
	}

}
