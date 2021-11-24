package com.desticube.objects;

import org.bukkit.inventory.ItemStack;

public class DestiKit {
	String name;
	ItemStack[] items;
	int cooldown;
	public DestiKit(String name, ItemStack[] items, int cooldown) {
		this.name = name;
		this.items = items;
		this.cooldown = cooldown;
	}
	
	public ItemStack[] getItems() {return items;}
	public String getName() {return name;}
	public int getCooldown() {return cooldown;}
}
