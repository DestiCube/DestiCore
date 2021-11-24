package com.desticube.commands;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "nick",
		description = "Change my name",
		aliases = {},
		permissions = {"desticore.nick", "desticore.nick.color"})
public class NicknameCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.nick")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			if (!p.getNickName().equalsIgnoreCase(p.getName())) {
				p.setNickName(p.getName());
				return p.sendMessage(msg.GENERAL_NICK_REMOVED);
			} else {
				return p.sendMessage("Correct usage: /nick (player)");	
			}
		} else {
			Pattern pat = Pattern.compile("&([0-fk-or])", Pattern.CASE_INSENSITIVE);
			if (pat.matcher(args[0]).find()) {
				if (!p.hasPermission("desticore.nick.color")) {
					return p.sendMessage(msg.GENERAL_NICK_NO_COLOR);
				} else {
					p.setNickName(color(args[0]));
					return p.sendMessage(msg.GENERAL_NICK.replaceAll("%nickname%", args[0]));
				}	
			} else {
				p.setNickName(args[0]);
				return p.sendMessage(msg.GENERAL_NICK.replaceAll("%nickname%", args[0]));
			}
		}
	}
}
