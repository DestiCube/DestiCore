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
		command = "god",
		description = "NOTHING CAN HURT ME MORTAL!",
		aliases = {"godmode"},
		permissions = {"desticore.god", "desticore.god.other"})
public class GodCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.god")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			if (p.getGodMode()) {
				p.setGodMode(false);
				p.sendMessage(msg.GENERAL_GOD_DISABLED);
			} else {
				p.setGodMode(true);
				p.sendMessage(msg.GENERAL_GOD_ENABLED);
			}
		} else {
			Player target = getDestiServer().getRealPlayer(args[0]);
			if (target == null) { return p.sendMessage(msg.GENERAL_PLAYER_NOT_ONLINE.replaceAll("%player%", args[0])); }
			if (!p.hasPermission("desticore.god.other")) {return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);}
			DestiPlayer dt = getDestiPlayer(target);
			if (dt.getGodMode()) {
				dt.setGodMode(false);
				dt.sendMessage(msg.GENERAL_GOD_DISABLED);
				p.sendMessage(msg.GENERAL_GOD_DISABLED_OTHER.replaceAll("%player%", dt.getName()));
			} else {
				dt.setGodMode(true);
				dt.sendMessage(msg.GENERAL_GOD_ENABLED);
				p.sendMessage(msg.GENERAL_GOD_ENABELD_OTHER.replaceAll("%player%", dt.getName()));
			}
		}
		return true;
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1 && sender.hasPermission("desticore.god.other")) {
			ArrayList<String> players = new ArrayList<String>();
			Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
			return players;
		} else {
			return null;
		}
	}

}
