package com.desticube.anticheat;

import org.bukkit.plugin.java.JavaPlugin;

import com.desticube.API;
import com.desticube.handlers.CommandHandler;
import com.desticube.handlers.ListenerHandler;
import com.desticube.objects.DestiServer;

public class DestiAntiCheat extends JavaPlugin {
	API api;
	DestiServer server;
	static DestiAntiCheat instance;
	CommandHandler cmdhandler;
	@Override
	public void onEnable() {
		instance = this;
		api = API.a();
		server = api.server();
		cmdhandler = new CommandHandler(this, this, "DestiAntiCheat", "com.desticube.anticheat.commands");
		ListenerHandler.a().setup(this, "com.desticube.listeners");
	}
	
	@Override
	public void onDisable() {
	}
	
	public static DestiAntiCheat a() {return instance;}
	public DestiServer server() {return server;}
	public API api() {return api;}


}
