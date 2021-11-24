package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "hat",
		description = "put a hat on",
		aliases = {},
		permissions = {"desticore.hat"})
public class HatCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.hat")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (p.getItemInMainHand() == null || p.getItemInMainHand().getType() == Material.AIR) return p.sendMessage("Cannot make air your hat");
		ItemStack head;
		if (p.getInventory().getHelmet() != null) {head = p.getInventory().getHelmet();}
		else {head = null;}
		ItemStack hand = p.getItemInMainHand();
		p.getInventory().setHelmet(hand);
		p.sendMessage(msg.GENERAL_HAT);
		if (head == null) {
			p.getItemInMainHand().setAmount(0);
			p.getItemInMainHand().setType(Material.AIR);
		}
		else {
			p.getItemInMainHand().setAmount(0);
			p.getItemInMainHand().setType(Material.AIR);
			p.getInventory().addItem(head);
		}
		return false;
	}


}
