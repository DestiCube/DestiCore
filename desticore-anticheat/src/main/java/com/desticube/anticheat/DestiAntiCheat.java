package com.desticube.anticheat;

import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import com.desticube.API;
import com.desticube.annotations.Command;
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
		cmdhandler = new CommandHandler(this, "DestiAntiCheat", getClasses("com.desticube.anticheat.commands"));
		cmdhandler.startup();
		ListenerHandler.a().setup(this, getClasses("com.desticube.listeners"));
	}
	
	@Override
	public void onDisable() {
	}
	
	public static DestiAntiCheat a() {return instance;}
	public DestiServer server() {return server;}
	public API api() {return api;}

	public Set<Class<?>> getClasses(String pkg) {
		 Reflections reflections = new Reflections(pkg);

		 Set<Class<? extends Object>> allClasses = 
		     reflections.getTypesAnnotatedWith(Command.class);
		 return allClasses;
	}

}
