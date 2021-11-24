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
		command = "stonecutter",
		description = "VIRTUAL THINGY",
		aliases = {"stonecut"},
		permissions = {"desticore.stonecutter"})
public class StonecutterCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.stonecutter")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		p.openVirtualStoneCutter();
		p.sendMessage(msg.GENERAL_STONE_CUTTER_OPEN);
		return true;
	}
	
}
