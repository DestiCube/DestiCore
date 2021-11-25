package com.desticube.objects;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class DestiWarp {
	String name;
	Location loc;
	ItemStack displayItem;
	public DestiWarp(String name, Location loc, ItemStack displayItem) {
		this.name = name;
		this.loc = loc;
		this.displayItem = displayItem;
	}
	
	public ItemStack getDisplayItem() {return displayItem;}
	public Location getLocation() {return loc;}
	public String getName() {return name;}

}
