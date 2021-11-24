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
		command = "setwarp",
		description = "set a fast travel thingy",
		aliases = {},
		permissions = {"desticore.setwarp"})
public class SetWarpCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.setwarp")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			p.sendMessage("&cCorrect usage: /setwarp (name)");
		} else if (args.length >= 1) {
			getDestiServer().setWarp(args[0], p.getLocation());
			p.sendMessage(msg.GENERAL_WARP_SET.replaceAll("%name%", args[0]));
		}
		return false;
	}

}
