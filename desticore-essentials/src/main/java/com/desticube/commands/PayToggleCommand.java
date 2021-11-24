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
		command = "paytoggle",
		description = "Why would you deny money..?",
		aliases = {},
		permissions = {"desticore.paytoggle"})
public class PayToggleCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.paytoggle")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		Player player = (Player) p.getPlayer();
		Account account = EconomyHandler.a().getAccounts().getAccount(player);
		account.setReceive(!account.canReceive());
		if (account.canReceive()) {
			p.sendMessage(msg.ECO_MONEY_TOGGLE_ON);
		} else {
			p.sendMessage(msg.ECO_MONEY_TOGGLE_OFF);
		}
		return true;
	}


}
