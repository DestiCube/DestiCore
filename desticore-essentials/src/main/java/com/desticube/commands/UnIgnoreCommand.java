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
		command = "unignore",
		description = "IGNOREEEEEEEEEE",
		aliases = {},
		permissions = {"desticore.unignore"})
public class UnIgnoreCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.unignore")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) return p.sendMessage("Correct usage: /unignore (name)");
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) return p.sendMessage(msg.GENERAL_PLAYER_NOT_ONLINE);
		if (!p.hasIgnored(target)) return p.sendMessage(msg.GENERAL_IGNORE_NEVER.replaceAll("%player%", target.getName()));
		p.sendMessage(msg.GENERAL_UNIGNORE.replaceAll("%player%", target.getName()));
		p.removeIgnored(target);
		return false;
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
