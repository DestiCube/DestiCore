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
		command = "warp",
		description = "warppppp speeeeeeed",
		aliases = {},
		permissions = {"desticore.warp", "desticore.teleport.instant"})
public class WarpCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.warp")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			getDestiServer().openWarpMenuGUI(p);
//			StringBuilder builder = new StringBuilder();
//			for (String warpname : getDestiServer().warpListAsString()) {
//				builder.append(warpname + ", ");
//			}
//			return p.sendMessage(msg.GENERAL_WARP_LIST.replaceAll("%warps%", builder.toString()));
		} else if (args.length >= 1) {
			if (!getDestiServer().warpExists(args[0])) return p.sendMessage(msg.GENERAL_WARP_NOT_SET.replaceAll("%name%", args[0]));
			p.teleport(getDestiServer().getWarp(args[0]).getLocation());
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> names = new ArrayList<String>();
			getDestiServer().warpListAsString().forEach(s -> names.add(s));
			return names;
		} else {
			return null;
		}
	}
}
