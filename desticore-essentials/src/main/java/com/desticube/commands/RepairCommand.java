package com.desticube.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "fix",
		description = "Fix yo shit",
		aliases = {"fixit"},
		permissions = {"desticore.repair", "desticore.repair.all"})
public class RepairCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.repair")) {p.sendMessage(msg.GENERAL_NO_PERMISSIONS); return true;}
		if (args.length < 1) {
			ItemStack item = p.getItemInMainHand();
			item.setDurability((short) 0);
			p.sendMessage(msg.GENERAL_REPAIR_ITEM.replaceAll("%item%", item.getType().toString()));
		} else if (args[0].equalsIgnoreCase("all")) {
			if (p.hasPermission("desticore.repair.all")) {
				for (ItemStack i : p.getArmorContents()) {if (i != null) {i.setDurability((short) 0);}}
				for (ItemStack i : p.getInventoryContents()) {if (i != null) {i.setDurability((short) 0);}}
				p.sendMessage(msg.GENERAL_REPAIR_ALL_ITEMS);
			} else {p.sendMessage(msg.GENERAL_NO_PERMISSIONS); return true;}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> arguments = new ArrayList<String>();
			arguments.add("all");
			return arguments;
		} else {
			return null;
		}
	}
}
