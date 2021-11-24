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
		command = "rules",
		description = "see them rules",
		aliases = {},
		permissions = {"desticore.rules"})
public class RulesCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.invsee")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			return p.sendMessage(getDestiServer().getRules(1));
		} else if (args.length == 1){
			if (!isNumeric(args[0])) return p.sendMessage(msg.GENERAL_NOT_NUMBER);
			if (getDestiServer().getRules(Integer.valueOf(args[0])) == null) return p.sendMessage("Not enough rules pages");
			return p.sendMessage(getDestiServer().getRules(Integer.valueOf(args[0])));
		} 
		return false;
	}

}
