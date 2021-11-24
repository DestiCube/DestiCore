package com.desticube.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "gamemode",
		description = "Cheat? No Cheat? Cant Build? Whatever one you want",
		aliases = {"gm", "gmc", "gma", "gmsp", "gms"},
		permissions = {"desticore.gamemode", "desticore.gamemode.other"})
public class GamemodeCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.gamemode")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
			if (label.equalsIgnoreCase("gmc")) return gamemode(p, GameMode.CREATIVE);	
			else if (label.equalsIgnoreCase("gms")) return gamemode(p, GameMode.SURVIVAL);
			else if (label.equalsIgnoreCase("gma")) return gamemode(p, GameMode.ADVENTURE);
			else if (label.equalsIgnoreCase("gmsp")) return gamemode(p, GameMode.SPECTATOR);
			if (args.length < 2) { 
				if (args.length < 1) {
					return p.sendMessage("&cCorrect usage: /gamemode (creative|adventure|survival|spectator) [player]");
				} 
				else if (args[0].equalsIgnoreCase("creative")
						|| args[0].equalsIgnoreCase("c")) return gamemode(p, GameMode.CREATIVE);
				else if (args[0].equalsIgnoreCase("survival")
						|| args[0].equalsIgnoreCase("s")) return gamemode(p, GameMode.SURVIVAL);
				else if (args[0].equalsIgnoreCase("spectator")
						|| args[0].equalsIgnoreCase("sp")) return gamemode(p, GameMode.SPECTATOR);
				else if (args[0].equalsIgnoreCase("adventure")
						|| args[0].equalsIgnoreCase("a")) return gamemode(p, GameMode.ADVENTURE);
			} else {
				if (p.hasPermission("desticore.gamemode.other")) {
					Player target = getDestiServer().getRealPlayer(args[0]);
					if (target == null) p.sendMessage("&c" + args[0] + " is not online");
					else {
						if (args[0].equalsIgnoreCase("creative")
								|| args[0].equalsIgnoreCase("c")) return gamemode(target, GameMode.CREATIVE, p);
						else if (args[0].equalsIgnoreCase("survival")
								|| args[0].equalsIgnoreCase("s")) return gamemode(target, GameMode.SURVIVAL, p);
						else if (args[0].equalsIgnoreCase("spectator")
								|| args[0].equalsIgnoreCase("sp")) return gamemode(target, GameMode.SPECTATOR, p);
						else if (args[0].equalsIgnoreCase("adventure")
								|| args[0].equalsIgnoreCase("a")) return gamemode(target, GameMode.ADVENTURE, p);
					}
				} else p.sendMessage(msg.GENERAL_NO_PERMISSIONS); return false;
			}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> arguments = new ArrayList<String>();
			arguments.add("adventure");
			arguments.add("creative");
			arguments.add("survival");
			arguments.add("spectator");
			arguments.add("a");
			arguments.add("c");
			arguments.add("s");
			arguments.add("sp");
			return arguments;
		} else if (args.length == 2) {
				ArrayList<String> players = new ArrayList<String>();
				Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
				return players;
			} else {
				return null;
			}
	}
	private boolean gamemode(DestiPlayer p, GameMode gamemode) {
		p.setGameMode(gamemode);
		p.sendMessage(msg.GENERAL_CHANGE_GAMEMODE.replaceAll("%gamemode%", gamemode.toString()));
		return true;
	}
	
	private boolean gamemode(Player p, GameMode gamemode, DestiPlayer setter) {
		p.setGameMode(gamemode);
		p.sendMessage(msg.GENERAL_CHANGE_GAMEMODE.replaceAll("%gamemode%", gamemode.toString()));
		setter.sendMessage(msg.GENERAL_CHANGE_GAMEMODE_OTHER.replaceAll("%gamemode%", gamemode.toString()).replaceAll("%player%", p.getName()));
		return true;
	}

}
