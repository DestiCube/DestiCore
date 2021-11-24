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
		command = "speed",
		description = "TIME TO GO FASTER",
		aliases = {},
		permissions = {"desticore.speed"})
public class SpeedCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.stonecutter")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			p.sendMessage("Correct usage: /speed (amount) [type]"); 
		} else if (args.length == 1) {
			if (!isNumeric(args[0])) return p.sendMessage(msg.GENERAL_NOT_NUMBER);
			Float amount = Float.parseFloat(args[0]);
			if (amount > 10 || amount < 0) return p.sendMessage(msg.GENERAL_SPEED_HAS_TO_BE_BETWEEN); 
			if (!p.isFlying()) {
				p.setWalkSpeed((amount / 10) + 0.1F);
				return p.sendMessage(msg.GENERAL_WALK_SPEED_SET.replaceAll("%amount%", args[0]));
			} else {
				p.setFlySpeed(amount / 10);
				return p.sendMessage(msg.GENERAL_FLY_SPEED_SET.replaceAll("%amount%", args[0]));
			}
		} else if (args.length == 2) {
			if (!isNumeric(args[0])) return p.sendMessage(msg.GENERAL_NOT_NUMBER);
			float amount = Float.parseFloat(args[0]);
			if (amount > 10 || amount < 0) return p.sendMessage(msg.GENERAL_SPEED_HAS_TO_BE_BETWEEN); 
			if (args[1].equalsIgnoreCase("walk")) {
				p.setWalkSpeed((amount / 10) + 0.1F);
				return p.sendMessage(msg.GENERAL_WALK_SPEED_SET.replaceAll("%amount%", args[0]));
			} else if (args[1].equalsIgnoreCase("fly")) {
				p.setFlySpeed(amount / 10);
				return p.sendMessage(msg.GENERAL_FLY_SPEED_SET.replaceAll("%amount%", args[0]));
			}
			
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> arg = new ArrayList<String>();
			arg.add("0");
			arg.add("1");
			arg.add("2");
			arg.add("3");
			arg.add("4");
			arg.add("5");
			arg.add("6");
			arg.add("7");
			arg.add("8");
			arg.add("9");
			arg.add("10");
			return arg;
		} else if (args.length == 2) {
			ArrayList<String> arg = new ArrayList<String>();
			arg.add("walk");
			arg.add("fly");
			return arg;
		} else {
			return null;
		}
	}
	
}
