package com.desticube.commands;

import java.util.HashMap;

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
		command = "condense",
		description = "Condense all of those ingots!",
		aliases = {},
		permissions = {"desticore.condense"})
public class CondenseCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.condense")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		HashMap<Material, Integer> returnamount = new HashMap<Material, Integer>();
		HashMap<Material, Integer>  giveamount = new HashMap<Material, Integer>();
		for (ItemStack item : p.getInventoryContents()) {
			if (item == null || item.getType() == Material.AIR) {continue;}
			if (!getCondenseList().keySet().contains(item.getType())) {continue;}
			else {
				if (item.getAmount() > 9) {
					int total = item.getAmount() / 9;
					int remainder = item.getAmount() % 9;
					Material giveitem = getCondenseList().get(item.getType());
					if (returnamount.containsKey(item.getType())) {returnamount.put(item.getType(), returnamount.get(item.getType()) + remainder);}
					else {returnamount.put(item.getType(), remainder);}
					if (giveamount.containsKey(giveitem)) {giveamount.put(giveitem, giveamount.get(giveitem) + total);}
					else {giveamount.put(giveitem, total);}
					p.getInventory().remove(item);
				} else {continue;}
			}
		}
		returnamount.keySet().forEach(mat -> {
			int amount = returnamount.get(mat);
			p.getInventory().addItem(new ItemStack(mat, amount));
		});
		giveamount.keySet().forEach(mat -> {
			int amount = giveamount.get(mat);
			p.getInventory().addItem(new ItemStack(mat, amount));
		});
 		return false;
	}
	private HashMap<Material, Material> getCondenseList() {
		HashMap<Material, Material> condenselist = new HashMap<Material, Material>();
		condenselist.put(Material.IRON_INGOT, Material.IRON_BLOCK);
		condenselist.put(Material.IRON_NUGGET, Material.IRON_INGOT);
		condenselist.put(Material.GOLD_INGOT, Material.GOLD_BLOCK);
		condenselist.put(Material.GOLD_NUGGET, Material.GOLD_INGOT);
		condenselist.put(Material.DIAMOND, Material.DIAMOND_BLOCK);
		condenselist.put(Material.EMERALD, Material.EMERALD_BLOCK);
		condenselist.put(Material.NETHERITE_INGOT, Material.NETHERITE_BLOCK);
		return condenselist;
	}
}
