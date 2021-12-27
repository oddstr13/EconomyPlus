package me.itswagpvp.economyplus.vault;

import me.itswagpvp.economyplus.EconomyPlus;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class VHook {
    public void onHook() {
        plugin.getServer().getServicesManager().register(Economy.class, EconomyPlus.veco, plugin, ServicePriority.Highest);
    }

    public void offHook() {
        plugin.getServer().getServicesManager().unregister(Economy.class, EconomyPlus.veco);
    }
}
