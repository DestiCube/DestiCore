package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "near",
		description = "Find those pesky stalkers",
		aliases = {"nearby", "nearme"},
		permissions = {"desticore.near"})
public class NearCommand extends AbstractCommand implements Defaults {
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.near")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		StringBuilder withoutbuilder = new StringBuilder();
		StringBuilder withbuilder = new StringBuilder();
		if (msg.GENERAL_NEAR_FORMAT.contains("%playerswithoutdistance%")) {
			double dist = config.GENERAL_NEAR_RADIUS;
			p.getNearyByPlayers(dist, dist, dist).forEach(pl -> {
				withoutbuilder.append(pl.getName() + ", ");
			});
		} else if (msg.GENERAL_NEAR_FORMAT.contains("%playerswithdistance%")) {
				double dist = config.GENERAL_NEAR_RADIUS;
				p.getNearyByPlayers(dist, dist, dist).forEach(pl -> {
					double distance = 0.0;
					if (p.getLocation().distance(pl.getLocation()) > 0.0) distance = p.getLocation().distance(pl.getLocation());
					withbuilder.append(msg.GENERAL_NEAR_DISTANCE_FORMAT
							.replaceAll("%player%", pl.getName())
							.replaceAll("%distance%", String.format("%.2f",distance))
							+ ", ");
				});
			}
		p.sendMessage(msg.GENERAL_NEAR_FORMAT
				.replaceAll("%playerswithoutdistance%", withoutbuilder.toString())
				.replaceAll("%playerswithdistance%", withbuilder.toString()));
		return true;
	}
	
}
