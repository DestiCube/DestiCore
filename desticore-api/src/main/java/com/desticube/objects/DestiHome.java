package com.desticube.objects;

import org.bukkit.Location;

public class DestiHome {
	String name;
	Location loc;
	public DestiHome(String name, Location loc) {
		this.name = name;
		this.loc = loc;
	}
	
	public Location getLocation() {return loc;}
	public String getName() {return name;}

}
