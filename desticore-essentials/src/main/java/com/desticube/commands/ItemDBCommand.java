package com.desticube.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desticube.annotations.Command;
import com.desticube.configs.config;
import com.desticube.configs.msg;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;

@Command(
		command = "itemdb",
		description = "Item Database Info",
		aliases = {},
		permissions = {"desticore.itemdb"})
public class ItemDBCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {sender.sendMessage(color(msg.GENERAL_PLAYER_ONLY_COMMAND)); return true;}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.itemdb")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (isTool(p.getItemInMainHand())) {
			ItemStack item = p.getItemInMainHand();
	        final int maxuses = item.getType().getMaxDurability();
	        final int durability = (maxuses + 1) - item.getDurability();
			String shortnames = item.getType().toString().toLowerCase() + ", " + "minecraft:" + item.getType().toString().toLowerCase();
			config.ITEM_DB_FORMAT_FOR_TOOL.forEach(s -> {
				p.sendMessage(s
						.replaceAll("%name%", "" + item.getType())
						.replaceAll("%uses%", "" + durability)
						.replaceAll("%shortnames%", shortnames));
			});
		} else {
			ItemStack item = p.getItemInMainHand();
			String shortnames = item.getType().toString().toLowerCase() + ", " + "minecraft:" + item.getType().toString().toLowerCase();
			config.ITEM_DB_FORMAT.forEach(s -> {
				p.sendMessage(s
						.replaceAll("%name%", "" + item.getType())
						.replaceAll("%shortnames%", shortnames));
			});
			
		}
		return true;
	}
	
//	ItemDBFormat:
//		  - "&7[&c&l!&7] Item: &c%name%"
//		  - "&7[&c&l!&7] Item short names: %shortnames%"
//		ItemDBFormatForTool:
//		  - "&7[&c&l!&7] Item: &c%name%"
//		  - "&7[&c&l!&7] This tool has &c%uses% uses left."
//		  - "&7[&c&l!&7] Item short names: &f%shortnames%"
	
	private boolean isTool(ItemStack mat) {
        final int maxuses = mat.getType().getMaxDurability();
		return maxuses != 0;
//		if (mat.toString().toUpperCase().contains("SWORD")) return true;
//		if (mat.toString().toUpperCase().contains("SHOVEL")) return true;
//		if (mat.toString().toUpperCase().contains("PICKAXE")) return true;
//		if (mat.toString().toUpperCase().contains("AXE")) return true;
//		if (mat.toString().toUpperCase().contains("HOE")) return true;
//		if (mat.toString().toUpperCase().contains("HELMET")) return true;
//		if (mat.toString().toUpperCase().contains("CHESTPLATE")) return true;
//		if (mat.toString().toUpperCase().contains("LEGGINGS")) return true;
//		if (mat.toString().toUpperCase().contains("BOOTS")) return true;
//		if (mat.toString().toUpperCase().contains("BOW")) return true;
//		return false;
	}
}
