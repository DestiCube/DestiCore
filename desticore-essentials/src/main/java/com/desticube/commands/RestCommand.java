package com.desticube.commands;

import java.util.ArrayList;	
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
@Command(
		command = "rest",
		description = "ZZZZZZZZZ",
		aliases = {},
		permissions = {"desticore.rest", "desticore.rest.other"})
public class RestCommand extends AbstractCommand implements Defaults {
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.rest")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			p.sendMessage(msg.GENERAL_REST);
			p.setStatistic(Statistic.TIME_SINCE_REST, 0);
		} else {
			if (!p.hasPermission("desticore.rest.other")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
			if (Bukkit.getPlayer(args[0]) != null) {
				DestiPlayer target = getDestiPlayer(Bukkit.getPlayer(args[0]));
				p.sendMessage(msg.GENERAL_REST_OTHER.replaceAll("%player%", target.getName()));
				target.setStatistic(Statistic.TIME_SINCE_REST, 0);
				target.sendMessage(msg.GENERAL_REST);
			}
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1 && sender.hasPermission("desticore.rest.other")) {
			ArrayList<String> players = new ArrayList<String>();
			Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
			return players;
		} else {
			return null;
		}
	}
}
