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
		command = "delwarp",
		description = "Delete that warp like it never existed!",
		aliases = {},
		permissions = {"desticore.delwarp"})
public class DelWarpCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.delwarp")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
				StringBuilder builder = new StringBuilder();
				for (String warp : getDestiServer().warpListAsString()) builder.append(warp + ", ");
				return p.sendMessage(msg.GENERAL_WARP_LIST.replaceAll("%warps%", builder.toString()));
			
		} else if (args.length >= 1) {
			if (!getDestiServer().warpExists(args[0])) return p.sendMessage(msg.GENERAL_WARP_NOT_SET);
			getDestiServer().delWarp(getDestiServer().getWarp(args[0]));
			return p.sendMessage(msg.GENERAL_WARP_REMOVED.replaceAll("%name%", args[0]));
		}
		return false;
	}

}
