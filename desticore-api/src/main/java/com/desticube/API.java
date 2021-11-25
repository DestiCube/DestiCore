package com.desticube;

import java.io.IOException;
import java.net.URISyntaxException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.desticube.configs.ConfigYAML;
import com.desticube.configs.msg;
import com.desticube.economy.EconomyHandler;
import com.desticube.handlers.CommandHandler;
import com.desticube.handlers.VaultHandler;
import com.desticube.objects.DestiServer;
import com.desticube.storage.PlayerDB;
import com.desticube.storage.PlayerData;
import com.desticube.timers.TPSTimer;
import com.desticube.timers.TeleportWarmup;
import com.desticube.timers.TpaRequestRemove;

public class API extends JavaPlugin {
	Plugin pl;
	DestiServer server;
	static API instance;
	private ConfigYAML config;
	@Override
	public void onEnable() {
		this.pl = this;
		instance = this;

		ConfigYAML.a().setup(this);
		VaultHandler.a().setupVault(this);
		server = new DestiServer(this.getServer(), this);
		msg.a().setup();
		EconomyHandler.a().setup(this, this);
		
		PlayerDB.a().startup(this);
		config = ConfigYAML.a();
		com.desticube.configs.config.a().setup();
		TeleportWarmup.a().setup(this);
		TpaRequestRemove.a().setup(this);
		PlayerData.a().setup(this);
		TPSTimer.a().startTPSTimer();
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
        		try {
					Wiki.b().create(pl);
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
            }
        }, (20 * 10));
	}
	public API() {}
	public static API a() {return instance;}
	public DestiServer server() {return server;}
	
	@Override
	public void onDisable() {
		EconomyHandler.a().shutdown();
		PlayerDB.a().closedown(pl);
		server().shutdown();
	}
	
	public ConfigYAML config() {return config;}
	


	
}
