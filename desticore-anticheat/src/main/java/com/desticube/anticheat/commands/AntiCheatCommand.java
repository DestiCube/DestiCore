package com.desticube.anticheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "anticheat",
		description = "VIRTUAL THINGY",
		aliases = {"ac"},
		permissions = {"desticore.anticheat"})
public class AntiCheatCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.anticheat")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		p.sendMessage("#5B65FFTEST");
		return true;
	}
}
