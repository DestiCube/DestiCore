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
		command = "fly",
		description = "I BELIEVE I CAN FLY",
		aliases = {"flying"},
		permissions = {"desticore.fly"})
public class FlyCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.fly")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (!p.isAllowFlight()) {
			p.setAllowFlight(true);
			p.sendMessage(msg.GENERAL_FLY_ON);
		} else if (p.isAllowFlight()) {
			p.setAllowFlight(false);
			p.sendMessage(msg.GENERAL_FLY_OFF);
		}
		return true;
	}

}
