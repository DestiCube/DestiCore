package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "tpaccept",
		description = "TPA",
		aliases = {},
		permissions = {"desticore.tpaccept"})
public class TpacceptCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.tpaccept")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (p.hasRecievedTpRequest()) {
			Player target = p.getRecievedTpRequest();
			DestiPlayer destitarget = getDestiPlayer(target);
			p.sendMessage(msg.GENERAL_TPAACCEPT);
			for (String s : config.TPA_ACCEPTED_FORMAT) {
				destitarget.sendMessage(s.replaceAll("%player%", p.getName()));
			}
			destitarget.teleport(p.getLocation());
			destitarget.removeSentTpRequest(p.getPlayer());
			p.removeRecievedTpRequest(target);
			return true;
		} else if (p.hasRecievedTpHereRequest()) {
			Player target = p.getRecievedTpHereRequest();
			DestiPlayer destitarget = getDestiPlayer(target);
			p.sendMessage(msg.GENERAL_TPAACCEPT);
			for (String s : config.TPA_ACCEPTED_FORMAT) {
				destitarget.sendMessage(s.replaceAll("%player%", p.getName()));
			}
			destitarget.teleport(p.getLocation());
			destitarget.removeSentTpHereRequest(p.getPlayer());
			p.removeRecievedTpHereRequest(target);
			return true;
		} else {
			return p.sendMessage("No request recieved");
		}
	}
	 
}
