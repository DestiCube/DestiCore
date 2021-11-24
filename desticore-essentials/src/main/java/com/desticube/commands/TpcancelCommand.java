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
		command = "tpcancel",
		description = "TPCANCEL",
		aliases = {},
		permissions = {"desticore.tpcancel"})
public class TpcancelCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.tpcancel")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (p.hasSentTpRequest()) {
			Player target = p.getSentTpRequest();
			DestiPlayer destitarget = getDestiPlayer(target);
			if (target == null) {
				p.removeSentTpRequest(target);
				return true;
			}
			p.sendMessage(msg.GENERAL_TPACANCEL.replaceAll("%player%", target.getName()));
			p.removeSentTpRequest(target);
			destitarget.removeSentTpRequest(p.getPlayer());
			return true;
		} else if (p.hasSentTpHereRequest()) {
			Player target = p.getSentTpHereRequest();
			DestiPlayer destitarget = getDestiPlayer(target);
			if (target == null) {
				p.removeSentTpHereRequest(target);
				return true;
			}
			p.sendMessage(msg.GENERAL_TPACANCEL.replaceAll("%player%", target.getName()));
			p.removeSentTpHereRequest(target);
			destitarget.removeSentTpHereRequest(p.getPlayer());
			return true;
		} else {
			 return p.sendMessage("No request sent out");
		}
	}

}
