package com.desticube.commands;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "destitps",
		description = "look at that tps",
		aliases = {},
		permissions = {"desticore.tps"})
public class TPSCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.tps")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
    	DecimalFormat format = new DecimalFormat("00.00");
		p.sendMessage("Current TPS: " + format.format(getDestiServer().getTPS()));
		return true;
	}
	
}
