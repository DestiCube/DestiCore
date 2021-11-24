package com.desticube.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.desticube.configs.ConfigYAML;
import com.desticube.interfaces.Defaults;
import com.desticube.objects.DestiPlayer;

public class PlayerDB implements Defaults {
	public PlayerDB() { }
    static PlayerDB instance = new PlayerDB();
    public static PlayerDB a() {return instance;}
	private Database database;
	private Connection connection;
    public void startup(Plugin pl) {
    	FileConfiguration config = ConfigYAML.a().getConfig();
        Bukkit.getLogger().warning("§a - Loading Player database...");
        if (config.getBoolean("PlayerMySQL.Enabled")) {
            try {
                String host = config.getString("PlayerMySQL.Host");
                String db = config.getString("PlayerMySQL.Database");
                String username = config.getString("PlayerMySQL.Username");
                String password = config.getString("PlayerMySQL.Password");
                boolean reconnect = config.getBoolean("PlayerMySQL.AutoReconnect");
                int port = config.getInt("PlayerMySQL.Port");
                database = new Database(pl, reconnect, host, db, username, password, port);
            } catch (Exception e) {
                Bukkit.getLogger().warning("§cPlayerMySQL has failed to load: " + e.getMessage());
                pl.getServer().getPluginManager().disablePlugin(pl);
                return;
            }
        } else {
            try {
                database = new Database(pl, "playerdata");
            } catch (Exception e) {
                Bukkit.getLogger().warning("§cSQLite has failed to load: " + e.getMessage());
                pl.getServer().getPluginManager().disablePlugin(pl);
                return;
            }
        }

        connection = database.connection();
	    try {
	        database.createTable("CREATE TABLE IF NOT EXISTS generaldata (UUID VARCHAR(36) UNIQUE, NAME VAR(36) UNIQUE, IPADDRESS VAR(36));");
	        Bukkit.getLogger().info("General Data Created");
	        database.createTable("CREATE TABLE IF NOT EXISTS lastlocation (UUID VARCHAR(36) UNIQUE, WORLD VARCHAR(36), LOCX DOUBLE(64,2), LOCY DOUBLE(64,2), LOCZ DOUBLE(64,2));");
	        Bukkit.getLogger().info("Last Location Created");
	        database.createTable("CREATE TABLE IF NOT EXISTS lastlogout (UUID VARCHAR(36) UNIQUE, MONTH INT(2), DAY INT(2), YEAR INT(4), HOUR INT(2), MINUTE INT(2), SECOND INT(2));");
	        Bukkit.getLogger().info("Last Logout Created");
	        database.createTable("CREATE TABLE IF NOT EXISTS homes (UUID VARCHAR(36), HOMENAME VARCHAR(36) UNIQUE, WORLD VARCHAR(36), LOCX DOUBLE(64,2), LOCY DOUBLE(64,2), LOCZ DOUBLE(64,2), LOCYAW FLOAT(64,2), LOCPITCH FLOAT(64,2));");
	        Bukkit.getLogger().info("Homes Created");
	        database.createTable("CREATE TABLE IF NOT EXISTS onjoindata (UUID VARCHAR(36) UNIQUE, GOD TINYINT(1), TPTOGGLED TINYINT(1), SOCIALSPY TINYINT(1));");
	        Bukkit.getLogger().info("On Join Data Created");
	        database.createTable("CREATE TABLE IF NOT EXISTS ignoredplayers (UUID VARCHAR(36), IGNOREDPLAYER VARCHAR(36));");
	        Bukkit.getLogger().info("Ignored Players Created");
	        
	        database.createTable("CREATE TABLE IF NOT EXISTS kitdata (UUID VARCHAR(36), KIT VARCHAR(36), MONTH INT(2), DAY INT(2), YEAR INT(4), HOUR INT(2), MINUTE INT(2), SECOND INT(2));");
	        Bukkit.getLogger().info("Kit Data Created");
	        database.createTable("CREATE TABLE IF NOT EXISTS nicknames (UUID VARCHAR(36) UNIQUE, NICKNAME VARCHAR(36));");
	        Bukkit.getLogger().info("Nicknames Created");
	        database.createTable("CREATE TABLE IF NOT EXISTS badges (UUID VARCHAR(36) UNIQUE, BADGE VARCHAR(36));");
	        
	    } catch (SQLException e) {
	    	Bukkit.getLogger().warning("§cThe table has failed to load: " + e.getMessage());
	        pl.getServer().getPluginManager().disablePlugin(pl);
	        return;
	    }
	}
    
    public Database getDatabase() {
    	return database;
    }
    public Connection getConnection() {
    	return connection;
    }
    
    
    public void closedown(Plugin pl) {
    	Bukkit.getOnlinePlayers().forEach(p -> onQuit(p));
        database.close();
    }
    
    public void onQuit(Player p) {
    	setGeneralData(p.getUniqueId().toString(), p.getName(), p.getPlayer().getAddress().getAddress().getHostAddress());
    	setLastLocation(p.getUniqueId().toString(), p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
    	setLastLogout(p.getUniqueId().toString(), LocalDateTime.now());
    }
    
    public void onJoin(DestiPlayer p) {
    	setGeneralData(p.getUUID().toString(), p.getName(), p.getPlayer().getAddress().getAddress().getHostAddress());
    }
    
    public void setGeneralData(String UUID, String name, String ip) {
	    	try (PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO generaldata VALUES (?, ?, ?)")) {
	            insert.setString(1, UUID);
	            insert.setString(2, name);
	            insert.setString(3, ip);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    }
    
    public String getGeneralDataUUID(String UUID) {
    	String uuid = "N/A";
    	if (hasData(UUID, "generaldata")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT UUID FROM generaldata WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                    uuid = result.getString(1);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return uuid;
    }
    
    public String getGeneralDataName(String UUID) {
    	String name = "N/A";
    	if (hasData(UUID, "generaldata")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT NAME FROM generaldata WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                    name = result.getString(1);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return name;
    }
    
    public String getGeneralDataIPAddress(String UUID) {
    	String ipaddress = "N/A";
    	if (hasData(UUID, "generaldata")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT IPADDRESS FROM generaldata WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                	ipaddress = result.getString(1);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return ipaddress;
    }
    
    
    public void setLastLocation(String UUID, World world, double X, double Y, double Z) {
	    	try (PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO lastlocation VALUES (?, ?, ?, ?, ?)")) {
	            insert.setString(1, UUID);
	            insert.setString(2, world.getName());
	            insert.setDouble(3, X);
	            insert.setDouble(4, Y);
	            insert.setDouble(5, Z);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    }
    
    public Location getLastLocation(String UUID) {
    	String world = "N/A";
    	Double x = 0.0;
    	Double y = 0.0;
    	Double z = 0.0;
    	if (hasData(UUID, "lastlocation")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT WORLD, LOCX, LOCY, LOCZ FROM lastlocation WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                	world = result.getString(1);
	                	x = result.getDouble(2);
	                	y = result.getDouble(3);
	                	z = result.getDouble(4);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return new Location(Bukkit.getWorld(world),x,y,z);
    }
    
    

    
    public void setLastLogout(String UUID, LocalDateTime date) {
	    	try (PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO lastlogout VALUES (?, ?, ?, ?, ?, ?, ?)")) {
	            insert.setString(1, UUID);
	            insert.setInt(2, date.getMonthValue());
	            insert.setInt(3, date.getDayOfMonth());
	            insert.setInt(4, date.getYear());
	            insert.setInt(5, date.getHour());
	            insert.setInt(6, date.getMinute());
	            insert.setInt(7, date.getSecond());
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	
    }
    //UUID VARCHAR(36) UNIQUE, MONTH INT(2), DAY INT(2), YEAR INT(4), HOUR INT(2), MINUTE INT(2), SECOND INT(2)
    public LocalDateTime getLastLogout(String UUID) {
    	Integer month = 0;
    	Integer day = 0;
    	Integer year = 0;
    	Integer hour = 0;
    	Integer minute = 0;
    	Integer second = 0;
    	if (hasData(UUID, "lastlogout")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT MONTH, DAY, YEAR, HOUR, MINUTE, SECOND FROM lastlogout WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                	month = result.getInt(1);
	                	day = result.getInt(2);
	                	year = result.getInt(3);
	                	hour = result.getInt(4);
	                	minute = result.getInt(5);
	                	second = result.getInt(6);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return LocalDateTime.of(year, month, day, hour, minute, second);
    }
    
    
    
    public void setHomesLocation(String UUID, String homename, World world, double X, double Y, double Z, float yaw, float pitch) {
    		try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO homes VALUES (?, ?, ?, ?, ?, ?, ?, ?);")) {
    			statement.setString(1, UUID);
    			statement.setString(2, homename);
    			statement.setString(3, world.getName());
    			statement.setDouble(4, X);
	            statement.setDouble(5, Y);
	            statement.setDouble(6, Z);
	            statement.setDouble(7, yaw);
	            statement.setDouble(8, pitch);
                statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }
    
    public ArrayList<String> getHomes(String UUID) {
    	ArrayList<String> homes = new ArrayList<String>();
    	if (hasData(UUID, "homes")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT HOMENAME FROM homes WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                while (result.next()) {
	                	homes.add(result.getString("HOMENAME"));
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return homes;
    }
    
    public Location getHomeLocation(String UUID, String home) {
    	String world = "N/A";
    	Double x = 0.0;
    	Double y = 0.0;
    	Double z = 0.0;
    	Float yaw = 0.0f;
    	Float pitch = 0.0f;
    	if (hasData(UUID, "homes")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT HOMENAME, WORLD, LOCX, LOCY, LOCZ, LOCYAW, LOCPITCH FROM homes WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                while (result.next()) {
	                	if (result.getString("HOMENAME").equalsIgnoreCase(home)) {
	            			world = result.getString(2);
	            			x = result.getDouble(3);
	            			y = result.getDouble(4);
	            			z = result.getDouble(5);
	            			yaw = result.getFloat(6);
	            			pitch = result.getFloat(7);
	                	}
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
    public void clearHomes(String UUID) {
//    	DELETE FROM products WHERE product_id=1;
    	if (hasData(UUID, "homes")) {
	    	try (PreparedStatement insert = connection.prepareStatement("DELETE FROM homes WHERE UUID = ?;")) {
	            insert.setString(1, UUID);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    }
    
//	    CREATE TABLE IF NOT EXISTS onjoindata (UUID VARCHAR(36) UNIQUE, GOD TINYINT(1), TPTOGGLED TINYINT(1), SOCIALSPY TINYINT(1));

    public void setOnJoinData(String UUID, int god, int tptoggled, int socialspy) {
	    	try (PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO onjoindata VALUES (?, ?, ?, ?)")) {
	            insert.setString(1, UUID);
	            insert.setInt(2, god);
	            insert.setInt(3, tptoggled);
	            insert.setInt(4, socialspy);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    }

    public boolean getOnJoinSocialSpy(String UUID) {
    	int socialspy = 0;
    	if (hasData(UUID, "onjoindata")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT SOCIALSPY FROM onjoindata WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                	socialspy = result.getInt(1);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	if (socialspy == 1) return true;
    	else if (socialspy == 0) return false;
    	else return true;
    }
    public boolean getOnJoinGodMode(String UUID) {
    	int godmode = 0;
    	if (hasData(UUID, "onjoindata")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT GOD FROM onjoindata WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                	godmode = result.getInt(1);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	if (godmode == 1) return true;
    	else if (godmode == 0) return false;
    	else return true;
    }

    public boolean getOnJoinTpToggled(String UUID) {
    	int tptoggled = 0;
    	if (hasData(UUID, "onjoindata")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT TPTOGGLED FROM onjoindata WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                	tptoggled = result.getInt(1);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	if (tptoggled == 1) return true;
    	else if (tptoggled == 0) return false;
    	else return true;
    }
    
//("CREATE TABLE IF NOT EXISTS kitdata (UUID VARCHAR(36) UNIQUE, KIT VARCHAR(36), MONTH INT(2), DAY INT(2), YEAR INT(4), HOUR INT(2), MINUTE INT(2), SECOND INT(2));");
    public void setKitUse(String UUID, String kit, LocalDateTime date) {
	    	try (PreparedStatement insert = connection.prepareStatement("INSERT INTO kitdata VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
	            insert.setString(1, UUID);
	            insert.setString(2, kit);
	            insert.setInt(3, date.getMonthValue());
	            insert.setInt(4, date.getDayOfMonth());
	            insert.setInt(5, date.getYear());
	            insert.setInt(6, date.getHour());
	            insert.setInt(7, date.getMinute());
	            insert.setInt(8, date.getSecond());
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    }
    public LocalDateTime getKitUse(String UUID, String kit) {
    	Integer month = 0;
    	Integer day = 0;
    	Integer year = 0;
    	Integer hour = 0;
    	Integer minute = 0;
    	Integer second = 0;
    	if (hasData(UUID, "kitdata")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT KIT, MONTH, DAY, YEAR, HOUR, MINUTE, SECOND FROM kitdata WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                	if (result.getString(1).equalsIgnoreCase(kit)) {
		                	month = result.getInt(2);
		                	day = result.getInt(3);
		                	year = result.getInt(4);
		                	hour = result.getInt(5);
		                	minute = result.getInt(6);
		                	second = result.getInt(7);
	                	}
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return LocalDateTime.of(year, month, day, hour, minute, second);
    }
    

    public ArrayList<String> getUsedKits(String UUID) {
    	ArrayList<String> homes = new ArrayList<String>();
    	if (hasData(UUID, "kitdata")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT KIT FROM kitdata WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                while (result.next()) {
	                	homes.add(result.getString("KIT"));
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return homes;
    }
    
    public void clearKits(String UUID) {
//    	DELETE FROM products WHERE product_id=1;
    	if (hasData(UUID, "kitdata")) {
	    	try (PreparedStatement insert = connection.prepareStatement("DELETE FROM kitdata WHERE UUID = ?;")) {
	            insert.setString(1, UUID);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    }
    
//CREATE TABLE IF NOT EXISTS ignoredplayers (UUID VARCHAR(36) UNIQUE, IGNOREDPLAYER VARCHAR(36));
    public void setIgnored(String UUID, String ignoreduuid) {
	    	try (PreparedStatement insert = connection.prepareStatement("INSERT INTO ignoredplayers VALUES (?, ?)")) {
	            insert.setString(1, UUID);
	            insert.setString(2, ignoreduuid);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    }
    public boolean getIgnoredPlayer(String UUID, String ignoreduuid) {
    	boolean value = false;
    	if (hasData(UUID, "ignoredplayers")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT IGNOREDPLAYER FROM ignoredplayers WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
	                	if (result.getString(1).equalsIgnoreCase(ignoreduuid)) {
		                	value = true;
	                	}
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return value;
    }
    

    public ArrayList<String> getIgnoredPlayers(String UUID) {
    	ArrayList<String> players = new ArrayList<String>();
    	if (hasData(UUID, "ignoredplayers")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT IGNOREDPLAYER FROM ignoredplayers WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                while (result.next()) {
	                	players.add(result.getString("IGNOREDPLAYER"));
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return players;
    }
    
    public void clearIgnoredPlayers(String UUID) {
//    	DELETE FROM products WHERE product_id=1;
    	if (hasData(UUID, "ignoredplayers")) {
	    	try (PreparedStatement insert = connection.prepareStatement("DELETE FROM ignoredplayers WHERE UUID = ?;")) {
	            insert.setString(1, UUID);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    }
    
  //CREATE TABLE IF NOT EXISTS nicknames (UUID VARCHAR(36) UNIQUE, NICKNAME VARCHAR(36));
    public void setNickName(String UUID, String nickname) {
	    	try (PreparedStatement insert = connection.prepareStatement("INSERT INTO nicknames VALUES (?, ?)")) {
	            insert.setString(1, UUID);
	            insert.setString(2, nickname);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    }
    public String getNickName(String UUID) {
    	String value = "null";
    	if (hasData(UUID, "nicknames")) {
	    	try (PreparedStatement select = connection.prepareStatement("SELECT NICKNAME FROM nicknames WHERE UUID = ?")) {
	            select.setString(1, UUID);
	            try (ResultSet result = select.executeQuery()) {
	                if (result.next()) {
		                value = result.getString(1);
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    	return value;
    }
    
    
    public void deleteNickName(String UUID) {
//    	DELETE FROM products WHERE product_id=1;
    	if (hasData(UUID, "nicknames")) {
	    	try (PreparedStatement insert = connection.prepareStatement("DELETE FROM nicknames WHERE UUID = ?;")) {
	            insert.setString(1, UUID);
	            insert.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
    	}
    }
    

    //CREATE TABLE IF NOT EXISTS nicknames (UUID VARCHAR(36) UNIQUE, NICKNAME VARCHAR(36));
      public void setBadge(String UUID, String badge) {
  	    	try (PreparedStatement insert = connection.prepareStatement("INSERT INTO badges VALUES (?, ?)")) {
  	            insert.setString(1, UUID);
  	            insert.setString(2, badge);
  	            insert.executeUpdate();
  	        } catch (SQLException ex) {
  	            ex.printStackTrace();
  	        }
      }
      public String getBadge(String UUID) {
      	String value = "null";
      	if (hasData(UUID, "badges")) {
  	    	try (PreparedStatement select = connection.prepareStatement("SELECT NICKNAME FROM badges WHERE UUID = ?")) {
  	            select.setString(1, UUID);
  	            try (ResultSet result = select.executeQuery()) {
  	                if (result.next()) {
  		                value = result.getString(1);
  	                }
  	            }
  	        } catch (SQLException ex) {
  	            ex.printStackTrace();
  	        }
      	}
      	return value;
      }
      
      
      public void deleteBadge(String UUID) {
//      	DELETE FROM products WHERE product_id=1;
      	if (hasData(UUID, "badges")) {
  	    	try (PreparedStatement insert = connection.prepareStatement("DELETE FROM badges WHERE UUID = ?;")) {
  	            insert.setString(1, UUID);
  	            insert.executeUpdate();
  	        } catch (SQLException ex) {
  	            ex.printStackTrace();
  	        }
      	}
      }
    public boolean hasPlayerData(String UUID) {
    	return hasData(UUID, "generaldata");
    }
    
    public boolean hasData(String UUID, String table) {
    	try (PreparedStatement select = connection.prepareStatement("SELECT UUID FROM " + table + " WHERE UUID = ?")) {
            select.setString(1, UUID);
            try (ResultSet result = select.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    } 
    
}
