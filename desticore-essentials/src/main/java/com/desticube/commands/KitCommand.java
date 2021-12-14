package com.desticube.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "kit",
		description = "warppppp speeeeeeed",
		aliases = {},
		permissions = {"desticore.kit", "desitcore.kit.(name)"})
public class KitCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.kit")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			StringBuilder builder = new StringBuilder();
			for (String kitname : getDestiServer().getKits()) {
				if (p.hasPermission("desticore.kit." + kitname)) {
					builder.append(kitname + ", ");
				} else {
					continue;
				}
			}
			return p.sendMessage("Kits: " + builder.toString());
		} else if (args.length >= 1) {
			if (!getDestiServer().kitExists(args[0])) return p.sendMessage("Kit " + args[0] + " does not exist");
			if (!p.hasPermission("desticore.kit." + args[0])) {return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);}
			if (!p.canUseKit(getDestiServer().getKit(args[0]))) return p.sendMessage("Cannot use kit for another " + p.getTimeTillNextKit(getDestiServer().getKit(args[0])));
			for (ItemStack item : getDestiServer().getKit(args[0]).getItems()) {
				if (item == null || item.getType() == Material.AIR) { continue; }
				p.getInventory().addItem(item);
			}
			p.sendMessage("Given kit: " + args[0]);
			LocalDateTime time = LocalDateTime.now();
			p.setKitUsed(getDestiServer().getKit(args[0]), time);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> names = new ArrayList<String>();
			getDestiServer().getKits().forEach(s -> {
				if (sender.hasPermission("desticore.kit." + s)) {
					names.add(s);
				}
					});
			return names;
		} else {
			return null;
		}
	}
}
