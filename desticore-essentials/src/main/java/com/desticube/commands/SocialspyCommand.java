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
		command = "socialspy",
		description = "I'm stalking youuuuuuuuu",
		aliases = {"msgspy"},
		permissions = {"desticore.socialspy"})
public class SocialspyCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.socialspy")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (p.getSocialSpy()) {
			p.setSocialspy(false);
			p.sendMessage("socialspy disabled");
		} else {
			p.setSocialspy(true);
			p.sendMessage("socialspy enabled");
		}
		return false;
	}

}
