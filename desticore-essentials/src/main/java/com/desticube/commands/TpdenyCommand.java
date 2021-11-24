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
		command = "tpdeny",
		description = "I'm stalking you while you're offline",
		aliases = {},
		permissions = {"desticore.tpdeny"})
public class TpdenyCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.tpdeny")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (p.hasRecievedTpRequest()) {
			Player target = p.getRecievedTpRequest();
			DestiPlayer destitarget = getDestiPlayer(target);
			p.removeRecievedTpRequest(target);
			p.sendMessage(msg.GENERAL_TPADENIED);
			destitarget.removeSentTpRequest(p.getPlayer());
			destitarget.sendMessage(p.getName() + " Denied your tp request");
			return true;
		} else if (p.hasRecievedTpHereRequest()) {
			Player target = p.getRecievedTpHereRequest();
			DestiPlayer destitarget = getDestiPlayer(target);
			p.removeRecievedTpHereRequest(target);
			p.sendMessage(msg.GENERAL_TPADENIED);
			destitarget.removeSentTpHereRequest(p.getPlayer());
			destitarget.sendMessage(p.getName() + " Denied your tp request");
			return true;
		} else {
			return p.sendMessage("No tp request");
		}
	}
}
