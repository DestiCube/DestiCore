package com.desticube.handlers;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class VaultHandler {
	private VaultHandler() { }
    static VaultHandler instance = new VaultHandler();
    public static VaultHandler a() {return instance;}

    public Chat chat = null;
    public Permission perms = null;
    public Plugin pl;
    
    public boolean setupVault(Plugin p) {
    	pl = p;
    	return setupPermissions() && setupChat();
    }
    
    public boolean setupPermissions() {
        if (pl.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = pl.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }
    
    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = pl.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
    
	public Chat getChat() {return chat;}
    public Permission getPerms() {return perms;}
	
}
