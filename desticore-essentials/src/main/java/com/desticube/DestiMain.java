package com.desticube;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.desticube.handlers.CommandHandler;
import com.desticube.handlers.ListenerHandler;
import com.desticube.objects.DestiServer;

public class DestiMain extends JavaPlugin {
	API api;
	DestiServer server;
	CommandHandler cmdhandler;
	static DestiMain instance;
	@Override
	public void onEnable() {
		instance = this;
		api = API.a();
		server = api.server();
        		cmdhandler = new CommandHandler(this, "DestiEssentials", "com.desticube.commands");
        		cmdhandler.startup();
        		ListenerHandler.a().setup(this, "com.desticube.listeners");

	}
	
	@Override
	public void onDisable() {
	}
	
	public static DestiMain a() {return instance;}
	public DestiServer server() {return server;}
	public API api() {return api;}

	   
//	   private boolean translateFromEssentials() {
//		   long startTime = System.currentTimeMillis();
//		   String sep = File.separator;
//		   Bukkit.getLogger().info("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
//		   Bukkit.getLogger().info("§aTranslating all players from essentials to new plugin");
//		   Bukkit.getLogger().info("§aThis could cause lag and take a while");
//		   Bukkit.getLogger().info("");
//		   File folder = new File("plugins" + sep + "Essentials" + sep + "userdata");
//		   File[] listOfFiles = folder.listFiles();
//		   for (int i = 0; i < listOfFiles.length; i++) {
//			   File data = listOfFiles[i];
//			   YamlConfiguration config = YamlConfiguration.loadConfiguration(data);
//			   String playername = config.getString("last-account-name");
//			   OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(playername);
//			   //ECONOMY
//			   double money = Double.parseDouble(config.getString("money"));
//			   if (EconomyHandler.a().getAccounts().hasAccount(offlineplayer)) {
//	              Account account = EconomyHandler.a().getAccounts().getAccount(offlineplayer);
//	              account.setBalance(money);
//	              EconomyHandler.a().getSQLDatabase().updateAccountAsync(account);
//				  Bukkit.getLogger().info(playername + "'s balance was set to " + money);
//	            } else {
//	         	 EconomyHandler.a().getAccounts().createAccount(offlineplayer);
//	              Account account = EconomyHandler.a().getAccounts().getAccount(offlineplayer);
//	              account.setBalance(money);
//	              EconomyHandler.a().getSQLDatabase().updateAccountAsync(account);
//	 			 Bukkit.getLogger().info(playername + "'s was set to " + money);
//	            }
//		   }
//
//		   Bukkit.getLogger().info("");
//		   Bukkit.getLogger().info("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
//		   Bukkit.getLogger().info("§aAll players have been translated to the new economy");
//		   Bukkit.getLogger().info("§aIt took " + (System.currentTimeMillis() - startTime) + "ms to complete");
//		   return true;
//	   }
}
