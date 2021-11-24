package com.desticube.commands;

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
		command = "delhome",
		description = "Delete that home like it never existed!",
		aliases = {},
		permissions = {"desticore.delhome"})
public class DelHomeCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.delhome")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			if (p.homeAmount() == null || p.homeAmount() == 0) return p.sendMessage(msg.GENERAL_HOME_NONE);
				StringBuilder builder = new StringBuilder();
				for (DestiHome destihome : p.getHomes()) {
					builder.append(destihome.getName() + ", ");
				}
				return p.sendMessage(msg.GENERAL_HOME_LIST.replaceAll("%homes%", builder.toString()));
			
		} else if (args.length >= 1) {
			if (p.homeAmount() == null || p.homeAmount() == 0) return p.sendMessage(msg.GENERAL_HOME_NONE);
			if (!p.isHomeSet(args[0])) return p.sendMessage(msg.GENERAL_HOME_NOT_SET);
			p.removeHome(args[0]);
			return p.sendMessage(msg.GENERAL_HOME_REMOVE.replaceAll("%name%", args[0]));
		}
		return false;
	}

}
