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
		command = "book",
		description = "BOOKSSSSSSSSSS",
		aliases = {},
		permissions = {"desticore.book"})
public class BookCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.book")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		ItemStack item = p.getItemInMainHand();
		if (item.getType() == Material.WRITABLE_BOOK) {
			item.setType(Material.WRITTEN_BOOK);
			return p.sendMessage("Book changed to a written book");
		} else if (item.getType() == Material.WRITTEN_BOOK) {
			item.setType(Material.WRITABLE_BOOK);
			return p.sendMessage("Book changed to a writable book");
		} else {
			return p.sendMessage("Item not a book");
		}
	}
	
}
