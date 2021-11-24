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
		command = "delkit",
		description = "Delete that kit like it never existed!",
		aliases = {},
		permissions = {"desticore.delkit"})
public class DelKitCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.delkit")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			if (getDestiServer().getKits().size() == 0) return p.sendMessage("No kits");
				StringBuilder builder = new StringBuilder();
				for (String destihome : getDestiServer().getKits()) {
					builder.append(destihome + ", ");
				}
				return p.sendMessage("Kits: " + builder.toString());
			
		} else if (args.length >= 1) {
			if (getDestiServer().getKits().size() == 0) return p.sendMessage("No kits");
			if (!getDestiServer().kitExists(args[0])) return p.sendMessage("Kit doesnt exists");
			getDestiServer().deleteKit(args[0]);
			return p.sendMessage("Kit " + args[0] + " deleted");
		}
		return false;
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> names = new ArrayList<String>();
			getDestiServer().getKits().forEach(s -> names.add(s));
			return names;
		} else {
			return null;
		}
	}
}
