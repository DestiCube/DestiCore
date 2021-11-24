package com.desticube.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "editsign",
		description = "Change that sign bitch",
		aliases = {"es", "edits"},
		permissions = {"desticore.editsign"})
public class EditsignCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);	
		if (!p.hasPermission("desticore.editsign")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (p.getTargetBlock(null, 10).getType().toString().contains("SIGN")) {
			if (args.length < 2) {p.sendMessage("&cCorrect usage: /editsign (line) (message)");} 
			else {
				Sign sign = (Sign) p.getTargetBlock(null, 10).getState();
				List<String> line = new ArrayList<String>(Arrays.asList(args.clone()));
				line.remove(1);
				StringBuilder builder = new StringBuilder();
				line.forEach(s -> builder.append(s));
				sign.setLine(Integer.parseInt(args[0]), builder.toString());
			}
		} else {
			p.sendMessage(msg.GENERAL_SIGN_EDIT_NOT_SIGN);
		}
		return false;
	}
	
}
