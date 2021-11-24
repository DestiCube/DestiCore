package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "createkit",
		description = "set a fast travel thingy",
		aliases = {},
		permissions = {"desticore.createkit"})
public class CreateKitCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.createkit")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 2) {
			p.sendMessage("&cCorrect usage: /createkit (name) (cooldown)");
		} else if (args.length >= 2) {
			if (!isNumeric(args[1])) p.sendMessage(msg.GENERAL_NOT_NUMBER);
			getDestiServer().createKit(args[0], p.getInventory(), Integer.parseInt(args[1]));
			p.sendMessage("Kit: " + args[0] + " with the cooldown of " + args[1] + " was created");
		}
		return false;
	}

}
