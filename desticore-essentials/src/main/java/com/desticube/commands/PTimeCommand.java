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
		command = "ptime",
		description = "time to change my own time, fuck everyone else",
		aliases = {"personaltime"},
		permissions = {"desticore.ptime"})
public class PTimeCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.ptime")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			return p.sendMessage("Correct usage: /ptime (noon|day|midnight|night)");
		} else if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("noon")) p.setPlayerTime(6000, false);
			else if (args[0].equalsIgnoreCase("day")) p.setPlayerTime(1000, false);
			else if (args[0].equalsIgnoreCase("midnight")) p.setPlayerTime(18000, false);
			else if (args[0].equalsIgnoreCase("night")) p.setPlayerTime(13000, false);
			else if (args[0].equalsIgnoreCase("reset")) {
				p.resetPlayerWeather();
				p.sendMessage(msg.GENERAL_PTIME_RESET);
			}
			else p.setPlayerTime(Long.parseLong(args[0]), false);
			return p.sendMessage(msg.GENERAL_PTIME_SET.replaceAll("%time%", args[0]));
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (args.length == 1) {
			ArrayList<String> arguments = new ArrayList<String>();
			arguments.add("reset");
			arguments.add("noon");
			arguments.add("day");
			arguments.add("midnight");
			arguments.add("night");
			return arguments;
		} else {
			return null;
		}
	}
}