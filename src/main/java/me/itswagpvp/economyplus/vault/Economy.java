package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.database.cache.CacheManager;
import me.itswagpvp.economyplus.database.misc.Selector;
import org.bukkit.OfflinePlayer;

public class Economy extends VEconomy {

    private final OfflinePlayer player;
    private final double money;

    public Economy(OfflinePlayer player, double money) {
        super(EconomyPlus.plugin);
        this.player = player;
        this.money = money;
    }

    /**
     * @deprecated use EconomyPlus.veco.getBalance(player)
     * @return player account balance
     */
    @Deprecated
    public double getBalance() {
        return getBalance(player);
    }

    // Set the money of a player
    public void setBalance() {
        CacheManager.cachedPlayersMoneys.put(new Selector().playerToString(player), money);
        EconomyPlus.getDBType().setTokens(new Selector().playerToString(player), money);
    }

    // Add moneys to a player account
    /**
     * @deprecated use EconomyPlus.veco.depositPlayer(player, amount)
     */
    @Deprecated
    public void addBalance() {
        super.depositPlayer(player, money);
    }

    // Remove moneys from a player's account
    /**
     * @deprecated use EconomyPlus.veco.withdrawPlayer(player, amount)
     */
    @Deprecated
    public void takeBalance() {
        super.withdrawPlayer(player, money);
    }

    // Set player's bank to the constructor value
    public void setBank() {
        CacheManager.cachedPlayersBanks.put(new Selector().playerToString(player), money);
        EconomyPlus.getDBType().setBank(new Selector().playerToString(player), money);
    }

    // Returns the player bank
    public double getBank() {
        return CacheManager.cachedPlayersBanks.get(new Selector().playerToString(player));
    }

    // Controls if the player has enough moneys
    /**
     * @deprecated use EconomyPlus.veco.has(player, amount)
     */
    @Deprecated
    public boolean detractable() {
        return has(player, money);
    }
}