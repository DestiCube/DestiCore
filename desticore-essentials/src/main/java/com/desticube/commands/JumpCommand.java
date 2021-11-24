package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "jump",
		description = "teleport to that block in the distance",
		aliases = {"j", "jumpto"},
		permissions = {"desticore.jump"})
public class JumpCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.jump")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		Block b = p.getTargetBlock(null, config.GENERAL_JUMP_DISTANCE);
		if (!b.getChunk().isLoaded()) {return p.sendMessage("Block too far");}
		if (p.getLocation().distance(b.getLocation()) > config.GENERAL_JUMP_DISTANCE) {return p.sendMessage("Block too far");}
		if (b.getType() == Material.AIR || b == null) {return p.sendMessage("Block too far");}
		Location loc = new Location(b.getWorld(), b.getX(), b.getY(), b.getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
		p.teleport(loc);
		return false;
	}
}
