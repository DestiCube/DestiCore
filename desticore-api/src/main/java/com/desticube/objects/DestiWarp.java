package com.desticube.objects;

import org.bukkit.Location;

public class DestiWarp {
	String name;
	Location loc;
	public DestiWarp(String name, Location loc) {
		this.name = name;
		this.loc = loc;
	}
	
	public Location getLocation() {return loc;}
	public String getName() {return name;}

}
