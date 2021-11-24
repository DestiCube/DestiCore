package com.desticube.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
import com.desticube.storage.PlayerDB;

@Command(
		command = "tpo",
		description = "I'm stalking you while you're offline",
		aliases = {"teleportoffline", "tpoff", "tpoffline"},
		permissions = {"desticore.tpoffline"})
public class TpofflineCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.tpoffline")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1 || args == null) {return p.sendMessage(color("&cCorrect usage: /tpo (player)"));}
		Player target = Bukkit.getPlayer(args[0]);
		if (target != null) {
			p.sendMessage(msg.GENERAL_TPOFFLINE.replaceAll("%player%", target.getName()));
			p.teleportWithNoDelay(target.getLocation());
		} else {
			OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
			if (!PlayerDB.a().hasPlayerData(offline.getUniqueId().toString()) || offline == null) {p.sendMessage(msg.GENERAL_PLAYER_NEVER_JOINED.replaceAll("%player%", args[0]));}
			p.sendMessage(msg.GENERAL_TPOFFLINE.replaceAll("%player%", args[0]));
			p.teleportWithNoDelay(PlayerDB.a().getLastLocation(offline.getUniqueId().toString()));
		}
		return false;
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
}
