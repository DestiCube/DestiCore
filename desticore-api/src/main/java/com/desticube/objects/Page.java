package com.desticube.objects;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class Page {
	PageType type;
	int number;
	ArrayList<ItemStack> items;
	public Page(PageType type, int number, ArrayList<ItemStack> items) {
		this.type = type;
		this.number = number;
		this.items = items;
	}
	
	public PageType getType() {return type;}
	public int getNumber() {return number;}
	public ArrayList<ItemStack> getItems() {return items;}
}
enum PageType {
	PLAYER_WARP(),
	SERVER_WARP();
}