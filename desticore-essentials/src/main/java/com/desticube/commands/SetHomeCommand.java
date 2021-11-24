package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.ConfigYAML;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "sethome",
		description = "SET THE HOME SWEET HOME",
		aliases = {},
		permissions = {"desticore.sethome", "desticore.sethome.multiple.(rank)"})
public class SetHomeCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.sethome")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			if (p.isHomeSet("home")) {
				p.removeHome("home");
				p.setHome("home");
				p.sendMessage(msg.GENERAL_HOME_SET_DEFAULT);
			} else {
				p.setHome("home");
				p.sendMessage(msg.GENERAL_HOME_SET_DEFAULT);
			}
		} else if (args.length >= 1) {
			if (p.isHomeSet(args[0])) {
				p.removeHome(args[0]);
				p.setHome(args[0]);
				p.sendMessage(msg.GENERAL_HOME_SET_NAMED.replaceAll("%name%", args[0]));
			} else {
				FileConfiguration configyaml = ConfigYAML.a().getConfig();
				for (String rank : configyaml.getConfigurationSection("HomeAmounts").getKeys(false)) {
					if (!p.isOp()) {
						if (p.hasPermission("desticore.sethome.multiple." + rank)) {
							if (p.homeAmount() < configyaml.getInt("HomeAmounts." + rank)) {
								p.sendMessage(msg.GENERAL_HOME_SET_NAMED.replaceAll("%name%", args[0]));
								p.setHome(args[0]);
							} else {
								p.sendMessage(msg.GENERAL_HOME_MAXED);
							}
							break;
						} else {
							if (p.homeAmount() < configyaml.getInt("HomeAmounts.default")) {
								p.sendMessage(msg.GENERAL_HOME_SET_NAMED.replaceAll("%name%", args[0]));
								p.setHome(args[0]);
							} else {
								p.sendMessage(msg.GENERAL_HOME_MAXED);
							}
							break;
						}
						} else if (p.isOp()) {
							p.sendMessage(msg.GENERAL_HOME_SET_NAMED.replaceAll("%name%", args[0]));
							p.setHome(args[0]);
							break;
					}
				}
			  }
		}
		return false;
	}

}
