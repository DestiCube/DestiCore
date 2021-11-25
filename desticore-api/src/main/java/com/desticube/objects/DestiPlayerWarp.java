package com.desticube.objects;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class DestiPlayerWarp {
	String name;
	Location loc;
	ItemStack displayItem;
	OfflinePlayer owner;
	public DestiPlayerWarp(OfflinePlayer owner, String name, Location loc, ItemStack displayItem) {
		this.owner = owner;
		this.name = name;
		this.loc = loc;
		this.displayItem = displayItem;
	}
	
	public OfflinePlayer getOwner() {return owner;}
	public ItemStack getDisplayItem() {return displayItem;}
	public Location getLocation() {return loc;}
	public String getName() {return name;}

}
