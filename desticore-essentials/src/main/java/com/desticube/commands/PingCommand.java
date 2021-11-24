package com.desticube.commands;

import java.util.ArrayList;	
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
@Command(
		command = "ping",
		description = "Pong but better",
		aliases = {},
		permissions = {"desticore.ping", "desticore.ping.other"})
public class PingCommand extends AbstractCommand implements Defaults {
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.ping")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			p.sendMessage(msg.GENERAL_PING_FORMAT.replaceAll("%ping%", String.valueOf(p.getPing())));
		} else {
			if (!p.hasPermission("desticore.ping.other")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
			Player target = getDestiServer().getRealPlayer(args[0]);
			if (target == null) return p.sendMessage(msg.GENERAL_PLAYER_NOT_ONLINE);
			return p.sendMessage(msg.GENERAL_PING_FORMAT_OTHER.replaceAll("%ping%", String.valueOf(p.getPing())).replaceAll("%player%", target.getName()));
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1 && sender.hasPermission("desticore.ping.other")) {
			ArrayList<String> players = new ArrayList<String>();
			Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
			return players;
		} else {
			return null;
		}
	}
}
