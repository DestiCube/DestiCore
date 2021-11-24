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
		command = "money",
		description = "$$$$$$$$$$$$$$",
		aliases = {"bal", "emoney", "balance"},
		permissions = {"desticore.money.other", "desticore.money"})
public class MoneyCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.money")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
        if (args.length == 0) {
                if (p instanceof DestiPlayer) {
                    Player player = (Player) p.getPlayer();
                    Account account = EconomyHandler.a().getAccounts().getAccount(player);
                    p.sendMessage(msg.ECO_MONEY.replace("%money%", EconomyHandler.a().formatCurrency(account.getBalance())));
                    return true;
                } else {
                    p.sendMessage("§cInvalid arguments! Please use /money <name>");
                }
        } else if (args.length == 1) {
            if (p.hasPermission("desticore.money.other")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (EconomyHandler.a().getAccounts().hasAccount(target)) {
                    Account account = EconomyHandler.a().getAccounts().getAccount(target);
                    p.sendMessage(msg.ECO_MONEY_PLAYER.replace("%money%", EconomyHandler.a().formatCurrency(account.getBalance())).replace("%name%", target.getName()));
                    return true;
                } else {
                    p.sendMessage("§cThe account was not found!");
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
		} else {
			return null;
		}
	}
}