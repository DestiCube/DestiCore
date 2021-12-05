package com.desticube.objects;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DestiItem extends ItemStack {
	ItemStack item;
	Material mat;
	Integer amount;
	String displayName;
	ArrayList<String> lore;
	public DestiItem(Material mat) {
		this.mat = mat;
		this.amount = 1;
		this.displayName = "";
		this.lore = new ArrayList<String>();
		item = new ItemStack(mat);
	}
	
	public ItemStack a() {
		return item;
	}
}
