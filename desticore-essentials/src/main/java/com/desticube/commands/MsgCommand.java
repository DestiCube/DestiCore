package com.desticube.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		command = "msg",
		description = "MOREEEEEEEEE I NEED MOREEEEE",
		aliases = {"tell", "pm", "message"},
		permissions = {"desticore.msg"})
public class MsgCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.msg")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 2) return p.sendMessage("Correct usage: /msg (player) (message)"); 
		if (!getDestiServer().isOnline(args[0])) return p.sendMessage(msg.GENERAL_PLAYER_NOT_ONLINE.replaceAll("%player%", args[0]));
		DestiPlayer target = getDestiServer().getRealDestiPlayer(args[0]);
		StringBuilder builder = new StringBuilder();
		args[0] = "";
		Arrays.asList(args).forEach(s -> {
			builder.append(s);
			builder.append(" ");
		});
		target.sendMessage(config.GENERAL_MESSAGE_RECIEVED_FORMAT.replaceAll("%player%", p.getName()).replaceAll("%message%", builder.toString()));
		p.sendMessage(config.GENERAL_MESSAGE_SENT_FORMAT.replaceAll("%player%", target.getName().replaceAll("%message%", builder.toString())));
		target.setPreviousMessage(p);
		p.setPreviousMessage(target);
		for (DestiPlayer pl : getDestiServer().getOnlineDestiPlayers().values()) {
			if (pl.getSocialSpy()) {
				pl.sendMessage(config.GENERAL_SOCIAL_SPY_FORMAT.replaceAll("%player%", p.getName()).replaceAll("%target%", target.getName()).replaceAll("%message%", builder.toString()));
				continue;
			}
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
}
