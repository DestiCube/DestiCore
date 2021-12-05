package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
import com.desticube.objects.DestiPlayerWarp;

@Command(
		command = "delplayerwarp",
		description = "Delete that warp like it never existed!",
		aliases = {},
		permissions = {"desticore.delplayerwarp"})
public class DelPlayerWarpCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.delplayerwarp")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
				StringBuilder builder = new StringBuilder();
				for (DestiPlayerWarp warp : getDestiServer().playerWarpList()) {
					if (warp.getOwner().getUniqueId().toString().equalsIgnoreCase(p.getUUID().toString())) {
						builder.append(warp + ", ");
					}
				}
				return p.sendMessage(msg.GENERAL_WARP_LIST.replaceAll("%warps%", builder.toString()));
			
		} else if (args.length >= 1) {
			if (!getDestiServer().playerWarpExists(args[0])) return p.sendMessage(msg.GENERAL_WARP_NOT_SET);
			if (!getDestiServer().getPlayerWarpOwner(args[0]).equalsIgnoreCase(p.getUUID().toString())) return p.sendMessage("You are not the owner of this warp");
			getDestiServer().delPlayerWarp(getDestiServer().getPlayerWarp(args[0]));
			return p.sendMessage(msg.GENERAL_WARP_REMOVED.replaceAll("%name%", args[0]));
		}
		return false;
	}

}
