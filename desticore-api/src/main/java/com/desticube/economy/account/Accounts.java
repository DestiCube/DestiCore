package com.desticube.economy.account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.desticube.economy.EconomyHandler;

public class Accounts {

    private Map<UUID, Account> cached = new HashMap<>();

    public Accounts(Plugin main) {
    }

    public Map<UUID, Account> getCached() {
        return cached;
    }

    public void onJoin(Player p) {
        Account account = new Account(p.getUniqueId(), p.getName());
        account.setBalance(EconomyHandler.a().getDefaultMoney());
        EconomyHandler.a().getOrInsertAccountAsync(account);
        cached.put(p.getUniqueId(), account);
    }

    public void onQuit(Player p) {
        Account account = cached.remove(p.getUniqueId());
        if (account != null) {
            EconomyHandler.a().updateAccountAsync(account);
        }
    }

    public boolean createAccount(OfflinePlayer player) {
        Account account = cached.get(player.getUniqueId());
        if (account == null) {
            account = new Account(player);
            account.setBalance(EconomyHandler.a().getDefaultMoney());
            if (!EconomyHandler.a().hasAccount(account)) {
                return EconomyHandler.a().createAccount(account);
            }
        }
        return false;
    }

    public Account getAccount(OfflinePlayer player) {
        Account account = cached.get(player.getUniqueId());
        if (account == null) {
            account = new Account(player);
            EconomyHandler.a().getAccount(account, null);
        }
        return account;
    }

    public boolean hasAccount(OfflinePlayer player) {
        Account account = cached.get(player.getUniqueId());
        if (account == null) {
            return EconomyHandler.a().hasAccount(new Account(player));
        }
        return true;
    }

}
