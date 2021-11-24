package com.desticube.commands;

import org.bukkit.Bukkit;
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
		command = "baltop",
		description = "Balance top command to see the best of the best",
		aliases = {"btop", "topbal", "balancetop"},
		permissions = {"desticore.top", "desticore.top.pages"})
public class BalTopCommand extends AbstractCommand implements Defaults  {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		int[] page = { 0 };
        if (!p.hasPermission("desticore.top")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
        if (args.length == 1) {
            if (!p.hasPermission("desticore.top.pages")) {
                p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
                return true;
            }
            try {
                page[0] = Integer.parseInt(args[0]) - 1;
                if (page[0] < 0) {
                    p.sendMessage(msg.ECO_POSITIVE);
                    return true;
                }
                page[0] *= 10;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args.length > 1) {
            p.sendMessage("§cInvalid arguments! Please use /baltop <page>");
            return false;
        }
        EconomyHandler.a().getTopAsync(page[0], (all_balance, list) -> {
            if (list.size() == 0) {
                p.sendMessage(msg.ECO_TOP_NOT_FOUND);
                return;
            }
            p.sendMessage(msg.ECO_TOP);
            p.sendMessage(" ");
            for (Account account : list) {
                page[0]++;
                p.sendMessage(msg.ECO_TOP_RANK.replace("%money%", EconomyHandler.a().formatCurrency(account.getBalance())).replace("%name%", account.getName()).replace("%rank%", String.valueOf(page[0])));
            }
            if (page[0] <= 10) {
                p.sendMessage(" ");
                p.sendMessage(msg.ECO_TOP_ALL_MONEY.replace("%all_money%", EconomyHandler.a().formatCurrency(all_balance)));
            }
        });
        return true;
	}


}