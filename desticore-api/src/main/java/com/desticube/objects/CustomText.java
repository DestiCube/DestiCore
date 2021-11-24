package com.desticube.objects;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.desticube.interfaces.Defaults;

public class CustomText extends AbstractCommand implements Defaults {
	List<String> messages;
	String command;
	
	public CustomText(String command, List<String> messages) {
		this.messages = messages;
		this.command = command;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase(command)) return true;
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Player Only Command"); 
			return true;
		} else {
			getDestiPlayer((Player) sender).sendMessage(messages);
		}
		return false;
	}

}
