package com.desticube.badges.commands;

import java.util.Arrays;

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
		command = "badges",
		description = "Main Badge Command",
		aliases = {"badge"},
		permissions = {"desticore.badges"})
public class BadgeCommand extends AbstractCommand implements Defaults {
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.broadcast")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args == null || args.length == 0) return p.sendMessage("&cNot enough arguments");
		StringBuilder builder = new StringBuilder();
		Arrays.asList(args).forEach(s -> {
		builder.append(s);
		builder.append(" ");
		});
		p.sendMessage(config.GENERAL_BROADCAST_FORMAT.replaceAll("%message%", builder.toString()));
		return true;
	}
}
