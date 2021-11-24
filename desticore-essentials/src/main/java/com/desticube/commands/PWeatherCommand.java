package com.desticube.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
@Command(
		command = "pweather",
		description = "time to change my own weather, fuck everyone else",
		aliases = {"personalweather"},
		permissions = {"desticore.pweather"})
public class PWeatherCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.pweather")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			return p.sendMessage("Correct usage: /pweather (clear|rain|reset)");
		} else if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("clear")) p.setPlayerWeather(WeatherType.CLEAR);
			else if (args[0].equalsIgnoreCase("rain")) p.setPlayerWeather(WeatherType.DOWNFALL);
			else if (args[0].equalsIgnoreCase("reset")) {
				p.resetPlayerWeather();
				p.sendMessage(msg.GENERAL_PWEATHER_RESET);
			}
			else return p.sendMessage(msg.GENERAL_PWEATHER_NOT_OPTION.replaceAll("%weather%", args[0]));
			return p.sendMessage(msg.GENERAL_PWEATHER_SET.replaceAll("%weather%", args[0]));
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (args.length == 1) {
			ArrayList<String> arguments = new ArrayList<String>();
			arguments.add("reset");
			arguments.add("clear");
			arguments.add("rain");
			return arguments;
		} else {
			return null;
		}
	}
}