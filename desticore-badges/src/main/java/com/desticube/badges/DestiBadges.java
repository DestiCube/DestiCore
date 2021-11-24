package com.desticube.badges;

import org.bukkit.plugin.java.JavaPlugin;

import com.desticube.API;
import com.desticube.handlers.CommandHandler;
import com.desticube.handlers.ListenerHandler;
import com.desticube.objects.DestiServer;

public class DestiBadges extends JavaPlugin {
	API api;
	DestiServer server;
	static DestiBadges instance;
	@Override
	public void onEnable() {
		instance = this;
		api = API.a();
		server = api.server();
		CommandHandler.a().setup(this, this, "DestiBadges", "com.desticube.badges.commands");
		ListenerHandler.a().setup(this, "com.desticube.badges.listeners");
	}
	
	@Override
	public void onDisable() {
	}
	
	public static DestiBadges a() {return instance;}
	public DestiServer server() {return server;}
	public API api() {return api;}


}
