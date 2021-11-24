package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "more",
		description = "MOREEEEEEEEE I NEED MOREEEEE",
		aliases = {},
		permissions = {"desticore.more"})
public class MoreCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.more")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (p.getItemInMainHand().getType() == Material.AIR || p.getItemInMainHand() == null) return p.sendMessage(msg.GENERAL_MORE_AIR);
		p.getItemInMainHand().setAmount(64);
		p.sendMessage(msg.GENERAL_MORE.replaceAll("%item%", p.getItemInMainHand().getType().toString()));
		return true;
	}

}
