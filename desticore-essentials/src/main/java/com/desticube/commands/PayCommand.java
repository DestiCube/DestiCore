package com.desticube.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.economy.EconomyHandler;
import com.desticube.economy.account.Account;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "pay",
		description = "Pay that bitch that you owe money before they kill you",
		aliases = {"payplayer", "payperson"},
		permissions = {"desticore.pay"})
public class PayCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.pay")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
	        if (args.length != 2) {
	            p.sendMessage("§cInvalid arguments! /pay <name> <amount>");
	        } else {
	            Player player = (Player) p.getPlayer();
	            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
	            if (player.getUniqueId() == target.getUniqueId()) {
	                p.sendMessage(msg.ECO_MONEY_YOURSELF);
	            } else {
	                try {
	                    double amount = Math.abs(Double.parseDouble(args[1]));
	                    if (amount < 0) {
	                        p.sendMessage(msg.ECO_POSITIVE);
	                    } else if (!EconomyHandler.a().getAccounts().hasAccount(target)) {
	                        p.sendMessage(msg.ECO_PAY_ACCOUNT);
	                    } else {
	                        Account playerAccount = EconomyHandler.a().getAccounts().getAccount(player);
	                        if (playerAccount.getBalance() < amount) {
	                            p.sendMessage(msg.ECO_NOT_ENOUGH);
	                        } else {
	                            Account targetAccount = EconomyHandler.a().getAccounts().getAccount(target);
	                            if (!targetAccount.canReceive()) {
	                                p.sendMessage(msg.ECO_MONEY_DENIED);
	                            } else {
	                                playerAccount.setBalance(playerAccount.getBalance() - amount);
	                                targetAccount.setBalance(targetAccount.getBalance() + amount);
	                                p.sendMessage(msg.ECO_SENT_TO.replace("%money%", EconomyHandler.a().formatCurrency(amount)).replace("%name%", target.getName()));
	                                if (target.isOnline()) {
	                                    ((Player) target).sendMessage(msg.ECO_RECIEVED_FROM.replace("%money%", EconomyHandler.a().formatCurrency(amount)).replace("%name%", player.getName()));
	                                }
	                                return true;
	                            }
	                        }
	                    }
	                } catch (NumberFormatException e) {
	                    p.sendMessage(msg.ECO_PAY_NOT_NUMBER);
	                }
	            }
	        }
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> players = new ArrayList<String>();
			Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
			return players;
		} else if (args.length == 2) {
			ArrayList<String> arguments = new ArrayList<String>();
			arguments.add("10");
			arguments.add("100");
			arguments.add("1000");
			arguments.add("10000");
			arguments.add("100000");
			arguments.add("1000000");
			return arguments;
		} else {
			return null;
		}
	}
}
