package com.desticube.commands;

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
		command = "reply",
		description = "MOREEEEEEEEE I NEED MOREEEEE",
		aliases = {"r"},
		permissions = {"desticore.reply"})
public class ReplyCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.reply")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) return p.sendMessage("Correct usage: /r msg (message)"); 
		if (!p.getPreviousMessage().isOnline()) return p.sendMessage(msg.GENERAL_PLAYER_NOT_ONLINE);
		DestiPlayer target = p.getPreviousMessage();
		StringBuilder builder = new StringBuilder();
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

}
