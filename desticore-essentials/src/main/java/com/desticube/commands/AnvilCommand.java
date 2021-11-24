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
		command = "anvil",
		description = "VIRTUAL THINGY",
		aliases = {},
		permissions = {"desticore.anvil"})
public class AnvilCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.anvil")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		p.openVirtualAnvil();
		p.sendMessage(msg.GENERAL_ANVIL_OPEN);
		return true;
	}
	
}
