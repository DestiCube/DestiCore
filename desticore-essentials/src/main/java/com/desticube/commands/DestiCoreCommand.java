package com.desticube.commands;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.desticube.annotations.Command;
import com.desticube.configs.msg;
import com.desticube.economy.EconomyHandler;
import com.desticube.economy.account.Account;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.AbstractCommand;
import com.desticube.objects.DestiPlayer;
import com.desticube.storage.Database;
import com.desticube.storage.PlayerDB;

@Command(
		command = "desticore",
		description = "Its the main command like tf",
		aliases = {"dc", "destic"},
		permissions = {"desticore.command"})
public class DestiCoreCommand extends AbstractCommand implements Defaults {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender == Bukkit.getConsoleSender()) {
			if (args.length < 1) {
				sender.sendMessage("&7[&cDestiCore&7] DestiCore made by GamerDuck123");
				sender.sendMessage(" &c- /desticore reload");
				sender.sendMessage(" &c- /desticore translate (esseco, essplayerinfo, essall)");
			} else if (args[0].equalsIgnoreCase("reload")) {
				getDestiServer().reload();
				sender.sendMessage("&7[&c&l!&7] DestiCore config has been reloaded");
			} else if (args[0].equalsIgnoreCase("translate")) {
				if (args[1].equalsIgnoreCase("esseco")) {
					sender.sendMessage("&7[&c&l!&7] Translating ECONOMY infromation from Essentials...");
					long startTime = System.currentTimeMillis();
					translateEcoFromEssentials();
					sender.sendMessage("&7[&c&l!&7] Translation completed in " + (System.currentTimeMillis() - startTime) + "ms");
					
				} else if (args[1].equalsIgnoreCase("essplayerinfo")) {
						sender.sendMessage("&7[&c&l!&7] Translating PLAYER infromation from Essentials...");
						long startTime = System.currentTimeMillis();
						translateAllFromEssentials();
						sender.sendMessage("&7[&c&l!&7] Translation completed in " + (System.currentTimeMillis() - startTime) + "ms");
						
				} else  if (args[1].equalsIgnoreCase("essall")) {
					sender.sendMessage("&7[&c&l!&7] Translating ALL infromation from Essentials...");
					long startTime = System.currentTimeMillis();
					translateEcoFromEssentials();
					translateAllFromEssentials();
					sender.sendMessage("&7[&c&l!&7] Translation completed in " + (System.currentTimeMillis() - startTime) + "ms");
				}
			}
		}
		DestiPlayer p = getDestiPlayer((Player) sender);
		if (!p.hasPermission("desticore.command")) return p.sendMessage(msg.GENERAL_NO_PERMISSIONS);
		if (args.length < 1) {
			p.sendMessage("&7[&cDestiCore&7] DestiCore made by GamerDuck123");
			p.sendMessage(" &c- /desticore reload");
			p.sendMessage(" &c- /desticore translate (esseco, essplayerinfo, essall)");
		} else if (args[0].equalsIgnoreCase("reload")) {
			getDestiServer().reload();
			p.sendMessage("&7[&c&l!&7] DestiCore config has been reloaded");
		} else if (args[0].equalsIgnoreCase("translate")) {
			if (args[1].equalsIgnoreCase("esseco")) {
				p.sendMessage("&7[&c&l!&7] Translating ECONOMY infromation from Essentials...");
				long startTime = System.currentTimeMillis();
				translateEcoFromEssentials();
				p.sendMessage("&7[&c&l!&7] Translation completed in " + (System.currentTimeMillis() - startTime) + "ms");
				
			} else if (args[1].equalsIgnoreCase("essplayerinfo")) {
					p.sendMessage("&7[&c&l!&7] Translating PLAYER infromation from Essentials...");
					long startTime = System.currentTimeMillis();
					translateAllFromEssentials();
					p.sendMessage("&7[&c&l!&7] Translation completed in " + (System.currentTimeMillis() - startTime) + "ms");
					
			} else  if (args[1].equalsIgnoreCase("essall")) {
				p.sendMessage("&7[&c&l!&7] Translating ALL infromation from Essentials...");
				long startTime = System.currentTimeMillis();
				translateEcoFromEssentials();
				translateAllFromEssentials();
				p.sendMessage("&7[&c&l!&7] Translation completed in " + (System.currentTimeMillis() - startTime) + "ms");
			}
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
			String[] args) {
		if (args.length == 1) {
			ArrayList<String> arguments = new ArrayList<String>();
			arguments.add("reload");
			arguments.add("translate");
			return arguments;
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("translate")) {
				ArrayList<String> arguments = new ArrayList<String>();
				arguments.add("esseco");
				arguments.add("essplayerinfo");
				arguments.add("essall");
				return arguments;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	   private boolean translateEcoFromEssentials() {
	   long startTime = System.currentTimeMillis();
	   String sep = File.separator;
	   Bukkit.getLogger().info("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	   Bukkit.getLogger().info("§aTranslating all players from essentials to new plugin");
	   Bukkit.getLogger().info("§aThis could cause lag and take a while");
	   Bukkit.getLogger().info("");
	   File folder = new File("plugins" + sep + "Essentials" + sep + "userdata");
	   File[] listOfFiles = folder.listFiles();
	   for (int i = 0; i < listOfFiles.length; i++) {
		   File data = listOfFiles[i];
		   YamlConfiguration config = YamlConfiguration.loadConfiguration(data);
		   String playername = config.getString("last-account-name");
		   OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(playername);
		   //ECONOMY
		   double money = Double.parseDouble(config.getString("money"));
		   if (EconomyHandler.a().getAccounts().hasAccount(offlineplayer)) {
           Account account = EconomyHandler.a().getAccounts().getAccount(offlineplayer);
           account.setBalance(money);
           EconomyHandler.a().updateAccountAsync(account);
			  Bukkit.getLogger().info(playername + "'s balance was set to " + money);
         } else {
      	 EconomyHandler.a().getAccounts().createAccount(offlineplayer);
           Account account = EconomyHandler.a().getAccounts().getAccount(offlineplayer);
           account.setBalance(money);
           EconomyHandler.a().updateAccountAsync(account);
			 Bukkit.getLogger().info(playername + "'s was set to " + money);
         }
	   }

	   Bukkit.getLogger().info("");
	   Bukkit.getLogger().info("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	   Bukkit.getLogger().info("§aAll players have been translated to the new economy");
	   Bukkit.getLogger().info("§aIt took " + (System.currentTimeMillis() - startTime) + "ms to complete");
	   return true;
}
	   private boolean translateAllFromEssentials() {
	   long startTime = System.currentTimeMillis();
	   String sep = File.separator;
	   Bukkit.getLogger().info("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	   Bukkit.getLogger().info("§aTranslating all players from essentials to new plugin");
	   Bukkit.getLogger().info("§aThis could cause lag and take a while");
	   Bukkit.getLogger().info("");
	   Database database = PlayerDB.a().getDatabase();
	   Connection connection = PlayerDB.a().getConnection();
	   Bukkit.getLogger().info("§aDropping Old Tables...");
	   try {
	   connection.createStatement().executeUpdate("DROP TABLE generaldata");
	   connection.createStatement().executeUpdate("DROP TABLE lastlocation");
	   connection.createStatement().executeUpdate("DROP TABLE lastlogout");
	   connection.createStatement().executeUpdate("DROP TABLE homes");
	   connection.createStatement().executeUpdate("DROP TABLE onjoindata");
	   connection.createStatement().executeUpdate("DROP TABLE ignoredplayers");
	   connection.createStatement().executeUpdate("DROP TABLE kitdata");
	   connection.createStatement().executeUpdate("DROP TABLE nicknames");
	   Bukkit.getLogger().info("§aOld tables successfully dropped");

	   Bukkit.getLogger().info("§aCreating new tables...");
       database.createTable("CREATE TABLE IF NOT EXISTS generaldata (UUID VARCHAR(36) UNIQUE, NAME VAR(36) UNIQUE, IPADDRESS VAR(36));");
       database.createTable("CREATE TABLE IF NOT EXISTS lastlocation (UUID VARCHAR(36) UNIQUE, WORLD VARCHAR(36), LOCX DOUBLE(64,2), LOCY DOUBLE(64,2), LOCZ DOUBLE(64,2));");
       database.createTable("CREATE TABLE IF NOT EXISTS lastlogout (UUID VARCHAR(36) UNIQUE, MONTH INT(2), DAY INT(2), YEAR INT(4), HOUR INT(2), MINUTE INT(2), SECOND INT(2));");
       database.createTable("CREATE TABLE IF NOT EXISTS homes (UUID VARCHAR(36), HOMENAME VARCHAR(36) UNIQUE, WORLD VARCHAR(36), LOCX DOUBLE(64,2), LOCY DOUBLE(64,2), LOCZ DOUBLE(64,2), LOCYAW FLOAT(64,2), LOCPITCH FLOAT(64,2));");
       database.createTable("CREATE TABLE IF NOT EXISTS onjoindata (UUID VARCHAR(36) UNIQUE, GOD TINYINT(1), TPTOGGLED TINYINT(1), SOCIALSPY TINYINT(1));");
       database.createTable("CREATE TABLE IF NOT EXISTS ignoredplayers (UUID VARCHAR(36), IGNOREDPLAYER VARCHAR(36));");
       database.createTable("CREATE TABLE IF NOT EXISTS kitdata (UUID VARCHAR(36), KIT VARCHAR(36), MONTH INT(2), DAY INT(2), YEAR INT(4), HOUR INT(2), MINUTE INT(2), SECOND INT(2));");
       database.createTable("CREATE TABLE IF NOT EXISTS nicknames (UUID VARCHAR(36) UNIQUE, NICKNAME VARCHAR(36));");
	   Bukkit.getLogger().info("§aNew tables created successfully");
	   } catch (SQLException e) {
		   e.printStackTrace();
	   }
	   File folder = new File("plugins" + sep + "Essentials" + sep + "userdata");
	   File[] listOfFiles = folder.listFiles();
	   for (int i = 0; i < listOfFiles.length; i++) {
		   File data = listOfFiles[i];
		   YamlConfiguration config = YamlConfiguration.loadConfiguration(data);
		   String playername = config.getString("last-account-name");
		   String uuid = data.getName().replaceAll(".yml", "");
		   String ip = config.getString("ip-address");
		   Boolean godmode = config.getBoolean("godmode");
		   Boolean tptoggled = config.getBoolean("teleportenabled");
		   Boolean socialspy = config.getBoolean("socialspy");
		   String nickname = config.getString("nickname");
		   
		   PlayerDB.a().setGeneralData(uuid, playername, ip);
		   Bukkit.getLogger().info(playername + ": ");
		   Bukkit.getLogger().info("UUID: " + uuid);
		   Bukkit.getLogger().info("IP: " + ip);
		   
		   PlayerDB.a().setNickName(uuid, nickname);
		   Bukkit.getLogger().info("Nickname: " + nickname);

		   Bukkit.getLogger().info("Homes:");
		   if (config.getConfigurationSection("homes") != null) {
			   for (String home : config.getConfigurationSection("homes").getKeys(false)) {
				   World world = Bukkit.getWorld(config.getString("homes." + home + ".world-name"));
				   double X = config.getDouble("homes." + home + ".x");
				   double Y = config.getDouble("homes." + home + ".y");
				   double Z = config.getDouble("homes." + home + ".z");
				   float yaw = config.getLong("homes." + home + ".yaw");
				   float pitch = config.getLong("homes." + home + ".pitch");
				   PlayerDB.a().setHomesLocation(uuid, home, world, X, Y, Z, yaw, pitch);
				   Bukkit.getLogger().info(home + ":");
				   Bukkit.getLogger().info("- World: " + world.getName());
				   Bukkit.getLogger().info("- X: " + X);
				   Bukkit.getLogger().info("- Y: " + Y);
				   Bukkit.getLogger().info("- Z: " + Z);
			   }
		   }
		   
		   PlayerDB.a().setOnJoinData(uuid, godmode ? 1 : 0, tptoggled ? 1 : 0, socialspy ? 1 : 0);
		   Bukkit.getLogger().info("God: " + godmode.toString());
		   Bukkit.getLogger().info("Tptoggled: " + tptoggled.toString());
		   Bukkit.getLogger().info("SocialSpy: " + socialspy.toString());
		   if (config.getString("ignore") != null) {
			   for (String ignored : config.getStringList("ignore")) {
				   PlayerDB.a().setIgnored(uuid, ignored);
			   }
		   }
	   }

	   Bukkit.getLogger().info("");
	   Bukkit.getLogger().info("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	   Bukkit.getLogger().info("§aAll players have been translated to the new economy");
	   Bukkit.getLogger().info("§aIt took " + (System.currentTimeMillis() - startTime) + "ms to complete");
	   return true;
}
}
