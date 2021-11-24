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
		command = "eco",
		description = "Main command to control eco",
		aliases = {"ecc", "ec"},
		permissions = {"desticore.eco"})
public class EcoCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.eco")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
        if (args.length == 0) {
            p.sendMessage("&7[&cDestiEconomy§7] Made by GamerDuck123.");
            p.sendMessage("&c - /eco give <name> <amount>");
            p.sendMessage("&c - /eco set <name> <amount>");
            p.sendMessage("&c - /eco take <name> <amount>");
            return true;
        } else if (args.length == 3) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            try {
                double amount = Double.parseDouble(args[2]);
                if (args[0].equals("give")) {
                    if (EconomyHandler.a().getAccounts().hasAccount(target)) {
                        Account account = EconomyHandler.a().getAccounts().getAccount(target);
                        account.setBalance(account.getBalance() + amount);
                        EconomyHandler.a().updateAccountAsync(account);
                        p.sendMessage("§2You gave §f" + EconomyHandler.a().formatCurrency(amount) + " §2to §f" + target.getName());
                    } else {
                        p.sendMessage("§cThe account doesn't exists!");
                    }
                    return true;
                } else if (args[0].equals("set")) {
                    if (EconomyHandler.a().getAccounts().hasAccount(target)) {
                        Account account = EconomyHandler.a().getAccounts().getAccount(target);
                        account.setBalance(amount);
                        EconomyHandler.a().updateAccountAsync(account);
                        p.sendMessage("§2You set §f" + target.getName() + " §2to §f"  + EconomyHandler.a().formatCurrency(amount));
                    } else {
                        p.sendMessage("§cThe account doesn't exists!");
                    }
                    return true;
                } else if (args[0].equals("take")) {
                    if (EconomyHandler.a().getAccounts().hasAccount(target)) {
                        Account account = EconomyHandler.a().getAccounts().getAccount(target);
                        if (account.getBalance() < amount) {
                            p.sendMessage("§cNot enough funds!");
                        } else {
                            account.setBalance(account.getBalance() - amount);
                            EconomyHandler.a().updateAccountAsync(account);
                            p.sendMessage("§2You took §f" + EconomyHandler.a().formatCurrency(amount) + " §2from §f" + target.getName());
                        }
                    } else {
                        p.sendMessage("§cThe account doesn't exists!");
                    }
                    return true;
                }
            } catch (NumberFormatException e) {
                p.sendMessage("§cThe amount must be a number!");
            }
        }
        p.sendMessage("§cInvalid arguments! Type /eco for available commands!");
    return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> arguments = new ArrayList<String>();
			arguments.add("give");
			arguments.add("set");
			arguments.add("take");
			return arguments;
		} else if (args.length == 2) {
				ArrayList<String> players = new ArrayList<String>();
				Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
				return players;
			} else if (args.length == 3) {
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