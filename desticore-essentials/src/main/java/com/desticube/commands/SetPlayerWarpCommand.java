package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "setplayerwarp",
		description = "set a fast travel thingy",
		aliases = {},
		permissions = {"desticore.setplayerwarp"})
public class SetPlayerWarpCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.setplayerwarp")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			p.sendMessage("&cCorrect usage: /setplayerwarp (name)");
		} else if (args.length >= 1) {
			if (p.getItemInMainHand() == null || p.getItemInMainHand().getType() == Material.AIR) return p.sendMessage("You need to be holding an item");
			if (getDestiServer().playerWarpExists(args[0])) return p.sendMessage("Warp exists already");
			if (p.getBalance() < config.PLAYERWARP_COST) return p.sendMessage(msg.ECO_NOT_ENOUGH);
			getDestiServer().setPlayerWarp(p.getPlayer(), args[0], p.getLocation(), p.getItemInMainHand());
			p.subBalance(config.PLAYERWARP_COST);
			p.sendMessage(msg.GENERAL_WARP_SET.replaceAll("%name%", args[0]));
		}
		return false;
	}

}
