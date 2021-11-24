package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "kittycannon",
		description = "Kitty go BOOM",
		aliases = {},
		permissions = {"desticore.kittycannon"})
public class KittyCannonCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.kittycannon")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		Entity cat = p.getWorld().spawnEntity(p.getLocation(), EntityType.CAT);
		cat.setVelocity(p.getDirection().multiply(10));
		return true;
	}

}
