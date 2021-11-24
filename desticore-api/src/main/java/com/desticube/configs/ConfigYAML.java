package com.desticube.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.gamerduck.configs.Config;

public class ConfigYAML {
	private ConfigYAML() { }
    static ConfigYAML instance = new ConfigYAML();
    public static ConfigYAML a() {return instance;}
    
    Plugin pl;
	Config config;
	Config messages;
	Config locations;
	Config rules;
	Config kits;
	Config customtext;
	
	public void setup(Plugin pl) {
    	this.pl = pl;
    	config = new Config(pl, "config.yml");
		config.loadFromResource("config.yml");
    	config.load(pl);	
    	
    	messages = new Config(pl, "messages.yml");
    	messages.loadFromResource("messages.yml");
    	messages.load(pl);
    	
    	locations = new Config(pl, "locations.yml");
    	locations.loadFromResource("locations.yml");
    	locations.load(pl);
    	
    	rules = new Config(pl, "rules.yml");
    	rules.loadFromResource("rules.yml");
    	rules.load(pl);
    	
    	kits = new Config(pl, "kits.yml");
    	kits.loadFromResource("kits.yml");
    	kits.load(pl);
    	
    	customtext = new Config(pl, "customtext.yml");
    	customtext.loadFromResource("customtext.yml");
    	customtext.load(pl);
	}
    
    public FileConfiguration getConfig() {return config.getConfig();}
    public void saveConfig() {config.saveConfig();}
    public void reloadConfig() {config.reloadConfig();}
    
    public FileConfiguration getMessages() {return messages.getConfig();}
    public void saveMessages() {messages.saveConfig();}
    public void reloadMessages() {messages.reloadConfig();}
    
    public FileConfiguration getLocations() {return locations.getConfig();}
    public void saveLocations() {locations.saveConfig();}
    public void reloadLocations() {locations.reloadConfig();}
    
    public FileConfiguration getRules() {return rules.getConfig();}
    public void saveRules() {rules.saveConfig();}
    public void reloadRules() {rules.reloadConfig();}
    
    public FileConfiguration getKits() {return kits.getConfig();}
    public void saveKits() {kits.saveConfig();}
    public void reloadKits() {kits.reloadConfig();}
    
    public FileConfiguration getCustomText() {return customtext.getConfig();}
    public void saveCustomText() {customtext.saveConfig();}
    public void reloadCustomText() {customtext.reloadConfig();}
}
