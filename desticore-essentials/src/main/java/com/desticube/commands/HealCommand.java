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
		command = "heal",
		description = "I NEEDA HEAL OVER HERE",
		aliases = {},
		permissions = {"desticore.heal", "desticore.heal.other"})
public class HealCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.heal")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			p.healAll();
			p.sendMessage(msg.GENERAL_HEAL);
		} else if (args.length >= 1) {
			if (!p.hasPermission("desticore.heal.other")) { return p.sendMessage(msg.GENERAL_NO_PERMISSIONS_OTHER); }
			Player target = getDestiServer().getRealPlayer(args[0]);
			if (target == null) {return p.sendMessage(msg.GENERAL_PLAYER_NOT_ONLINE);}
			p.sendMessage(color(msg.GENERAL_HEAL_OTHER.replaceAll("%player%", target.getName())));
			target.sendMessage(color(msg.GENERAL_HEAL));
			getDestiPlayer(target).healAll();
		}
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
