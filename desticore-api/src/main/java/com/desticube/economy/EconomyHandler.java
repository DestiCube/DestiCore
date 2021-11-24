package com.desticube.economy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.desticube.configs.ConfigYAML;
import com.desticube.configs.msg;
import com.desticube.economy.account.Account;
import com.desticube.economy.account.Accounts;
import com.desticube.economy.vault.VaultHook;
import com.desticube.storage.Database;

import net.milkbowl.vault.economy.Economy;

public class EconomyHandler {
	

		public EconomyHandler() { }
    	static EconomyHandler instance = new EconomyHandler();
    	public static EconomyHandler a() {return instance;}
	
	 	private Accounts accounts;
	    private Database database;
	    private double defaultMoney;
	    private DecimalFormat formatter;
	    private ExecutorService executor;
	    private String table = "economy";
	    private Connection connection;

	    private VaultHook vault_hook;
	    
	    Plugin pl;
	    
	    public void setup(Plugin pl, JavaPlugin main) {
	    	this.pl = pl;
	    	ConsoleCommandSender console = pl.getServer().getConsoleSender();
	        console.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	        console.sendMessage("§a" + pl.getName() + "'s economy is loading... ");
	        console.sendMessage("§a - Loading config...");
	        FileConfiguration config = null;
	        try {
	            config = ConfigYAML.a().getConfig();
	            defaultMoney = config.getDouble("Settings.DefaultMoney");
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            pl.getServer().getPluginManager().disablePlugin(pl);
	            return;
	        }
	        console.sendMessage("§a - Loading Economy database...");
	        if (config.getBoolean("EconomyMySQL.Enabled")) {
	            try {
	                String host = config.getString("EconomyMySQL.Host");
	                String database = config.getString("EconomyMySQL.Database");
	                String username = config.getString("EconomyMySQL.Username");
	                String password = config.getString("EconomyMySQL.Password");
	                boolean reconnect = config.getBoolean("EconomyMySQL.AutoReconnect");
	                int port = config.getInt("EconomyMySQL.Port");
	                this.database = new Database(pl, reconnect, host, database, username, password, port);
	                connection = this.database.connection();
	            } catch (Exception e) {
	                console.sendMessage("§cEconomyMySQL has failed to load: " + e.getMessage());
	                pl.getServer().getPluginManager().disablePlugin(pl);
	                return;
	            }
	        } else {
	            try {
	                database = new Database(pl, "economy");
	                connection = database.connection();
	            } catch (Exception e) {
	                console.sendMessage("§cSQLite has failed to load: " + e.getMessage());
	                pl.getServer().getPluginManager().disablePlugin(pl);
	                return;
	            }
	        }
	        try {
	            database.createTable("CREATE TABLE IF NOT EXISTS " + table + " (UUID VARCHAR(36) UNIQUE, NAME VARCHAR(60), BALANCE DOUBLE(64,2));");
	        } catch (SQLException e) {
	            console.sendMessage("§cThe table has failed to load: " + e.getMessage());
                pl.getServer().getPluginManager().disablePlugin(pl);
	            return;
	        }
	        formatter = new DecimalFormat("#,##0.00");
	        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	        symbols.setDecimalSeparator('.');
	        symbols.setGroupingSeparator(',');
	        formatter.setDecimalFormatSymbols(symbols);
	        executor = Executors.newSingleThreadExecutor();
	        accounts = new Accounts(pl);
	        if (pl.getServer().getPluginManager().getPlugin("Vault") != null) {
	            console.sendMessage("§a - Hooking into Vault...");
	            pl.getServer().getServicesManager().register(Economy.class, vault_hook = new VaultHook(pl), pl, ServicePriority.High);
	        }
	        console.sendMessage("§a - Loading events...");	
	        pl.getServer().getPluginManager().registerEvents(new Events(), pl);
	        
	        Bukkit.getOnlinePlayers().forEach(p -> accounts.onJoin(p));
	        console.sendMessage("§a" + pl.getName() + "'s economy has been loaded!");
	        console.sendMessage("§a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	    }
	    
	   public void shutdown() {
		   if (database != null) {
	            pl.getServer().getServicesManager().unregister(vault_hook);
	            Bukkit.getOnlinePlayers().forEach(p -> accounts.onQuit(p));
	            executor.shutdownNow().forEach(r -> r.run());
	            database.close();
	        }
	   }
	   

	    public String getTable() {return table;}
	    public Accounts getAccounts() {return accounts;}
	    public ExecutorService getExecutor() {return executor;}
	    public double getDefaultMoney() {return defaultMoney;}
	    public String getCurrency(double amount) {return amount == 1.0 ? msg.ECO_CURRENCY_SINGULAR : msg.ECO_CURRENCY_PLURAL;}
	    public String formatCurrency(double amount) {return msg.ECO_CURRENCY_FORMAT.replace("%money%", formatter.format(amount)).replace("%currency%", getCurrency(amount));}

	    
	    //DATABASE PRESETS
	    public boolean createAccount(Account account) {
	        try (PreparedStatement insert = connection.prepareStatement("INSERT INTO " + EconomyHandler.a().getTable() + " VALUES (?, ?, ?)")) {
	            insert.setString(1, account.getUUID().toString());
	            insert.setString(2, account.getName());
	            insert.setDouble(3, account.getBalance());
	            insert.executeUpdate();
	            return true;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    public boolean hasAccount(Account account) {
	        try (PreparedStatement select = connection.prepareStatement("SELECT UUID, BALANCE FROM " + EconomyHandler.a().getTable() + " WHERE UUID = ?")) {
	            select.setString(1, account.getUUID().toString());
	            try (ResultSet result = select.executeQuery()) {
	                return result.next();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    public void updateAccount(Account account) {
	        try (PreparedStatement statement = connection.prepareStatement("UPDATE " + EconomyHandler.a().getTable() + " SET UUID = ?, NAME = ?, BALANCE = ? WHERE UUID = ?;")) {
	            statement.setString(1, account.getUUID().toString());
	            statement.setString(2, account.getName());
	            statement.setDouble(3, account.getBalance());
	            statement.setString(4, account.getUUID().toString());
	            statement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public void getAccount(Account account, Runnable ifFailed) {
	        try (PreparedStatement select = connection.prepareStatement("SELECT BALANCE FROM " + EconomyHandler.a().getTable() + " WHERE UUID = ?")) {
	            select.setString(1, account.getUUID().toString());
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                    account.setBalance(result.getDouble(1));
	                } else if (ifFailed != null) {
	                    ifFailed.run();
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public void getOrInsertAccountAsync(Account account) {
	    	EconomyHandler.a().getExecutor().execute(() -> getAccount(account, () -> createAccount(account)));
	    }

	    public void updateAccountAsync(Account account) {
	    	EconomyHandler.a().getExecutor().execute(() -> updateAccount(account));
	    }

	    public void getTopAsync(int page, BiConsumer<Double, List<Account>> consumer) {
	    	EconomyHandler.a().getExecutor().execute(() -> {
	            try (PreparedStatement select = connection.prepareStatement("SELECT NAME, BALANCE FROM " + EconomyHandler.a().getTable() + " ORDER BY BALANCE DESC")) {
	                try (ResultSet result = select.executeQuery()) {
	                    double all_balance = 0;
	                    List<Account> list = new ArrayList<>();
	                    for (int i = 0; result.next(); i++) {
	                        double balance = result.getDouble(2);
	                        if (page <= i && page + 10 > i) {
	                            String name = result.getString(1);
	                            if (name != null && name.length() > 0) {
	                                Account account = new Account(null, name);
	                                account.setBalance(balance);
	                                list.add(account);
	                            }
	                        }
	                        all_balance += balance;
	                    }
	                    consumer.accept(all_balance, list);
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        });
	    }
	    
	    
	    
}
class Events implements Listener {
	@EventHandler
    public void onJoin(PlayerJoinEvent e) {
       EconomyHandler.a().getAccounts().onJoin(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
    	EconomyHandler.a().getAccounts().onQuit(e.getPlayer());
    }
}

