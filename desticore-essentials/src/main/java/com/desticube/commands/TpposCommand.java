package com.desticube.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "tppos",
		description = "Teleport to a little location",
		aliases = {"tpposition"},
		permissions = {"desticore.tppos"})
public class TpposCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.tppos")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 3 || args == null) {return p.sendMessage(color("&cCorrect usage: /tppos (x) (y) (z)"));}
		double x = Double.parseDouble(args[0]);
		double y = Double.parseDouble(args[1]);
		double z = Double.parseDouble(args[2]);
		p.teleport(new Location(p.getWorld(), x, y, z));
		return false;
	}
 
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length <= 3) {
			ArrayList<String> arg = new ArrayList<String>();
			arg.add("1"); arg.add("10"); arg.add("100"); arg.add("1000"); arg.add("10000"); 
			return arg;
		} else {
			return null;
		}
	}

}
