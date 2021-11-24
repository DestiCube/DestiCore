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
import com.desticube.objects.DestiHome;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "home",
		description = "Home sweet home",
		aliases = {},
		permissions = {"desticore.home", "desticore.teleport.instant"})
public class HomeCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.home")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			if (p.homeAmount() == null || p.homeAmount() == 0) return p.sendMessage(msg.GENERAL_HOME_NONE);
			if (p.homeAmount() == 1) {
				p.teleport(p.getHomeLocation(p.getHomes().get(0).getName()));
			} else {
				StringBuilder builder = new StringBuilder();
				for (DestiHome destihome : p.getHomes()) {
					builder.append(destihome.getName() + ", ");
				}
				return p.sendMessage(msg.GENERAL_HOME_LIST.replaceAll("%homes%", builder.toString()));
			}
		} else if (args.length >= 1) {
			if (p.homeAmount() == null || p.homeAmount() == 0) return p.sendMessage(msg.GENERAL_HOME_NONE);
			if (!p.isHomeSet(args[0])) return p.sendMessage(msg.GENERAL_HOME_NOT_SET);
			p.teleport(p.getHomeLocation(args[0]));
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			DestiPlayer p = getDestiPlayer((Player) sender);
			ArrayList<String> names = new ArrayList<String>();
			p.getHomes().forEach(s -> names.add(s.getName()));
			return names;
		} else {
			return null;
		}
	}
}
