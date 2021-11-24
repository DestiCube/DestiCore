package com.desticube.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
import com.desticube.timers.TpaHereRequestRemove;

@Command(
		command = "tpahere",
		description = "TPAhere",
		aliases = {},
		permissions = {"desticore.tpahere"})
public class TpaHereCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.tpahere")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) return p.sendMessage("Correct usage: /tpa (name)");
		if (p.hasSentTpHereRequest()) return p.sendMessage("Already have request sent");
		Player target = getDestiServer().getRealPlayer(args[0]);
		DestiPlayer destitarget = getDestiPlayer(target);
		if (target == null) return p.sendMessage(msg.GENERAL_PLAYER_NOT_ONLINE.replaceAll("%player%", args[0]));
		if (!destitarget.getTpToggled()) return p.sendMessage("Player does not have tp toggled");
		for (String s : config.TPA_SENT_FORMAT) {
			p.sendMessage(s.replaceAll("%player%", target.getName()));
		}
		p.setSentTpHereRequest(target);
		for (String s : config.TPA_HERE_RECIEVED_FORMAT) {
			destitarget.sendMessage(s.replaceAll("%player%", p.getName()));	
		}
		destitarget.removeRecievedTpHereRequest(destitarget.getRecievedTpHereRequest());
		destitarget.setRecievedTpHereRequest(p.getPlayer());
		TpaHereRequestRemove.a().startDeleteTimer(p.getPlayer(), target, 120);
		return false;
	}


	 
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> players = new ArrayList<String>();
			Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
			return players;
		} else {
			return null;
		}
	}
}
