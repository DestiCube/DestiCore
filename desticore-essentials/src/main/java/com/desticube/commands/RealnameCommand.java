package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "realname",
		description = "Change my name",
		aliases = {},
		permissions = {"desticore.realname"})
public class RealnameCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.realname")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) { return p.sendMessage("Correct usage: /realname (player)");
		} else {
			if (!getDestiServer().isOnline(args[0])) return p.sendMessage(msg.GENERAL_PLAYER_NOT_ONLINE.replaceAll("%player%", args[0]));
			DestiPlayer target = getDestiServer().getRealDestiPlayer(args[0]);
			return p.sendMessage(msg.GENERAL_REALNAME.replaceAll("%nickname%", target.getNickName()).replaceAll("%realname%", p.getRealName()));
		}
	}
}
