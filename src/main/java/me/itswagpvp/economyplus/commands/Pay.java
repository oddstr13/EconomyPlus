package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.StorageManager;
import me.itswagpvp.economyplus.misc.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Pay implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(plugin.getMessage("NoConsole"));
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("economyplus.pay")) {
            p.sendMessage(plugin.getMessage("NoPerms"));
            Utils.playErrorSound(p);
            return true;
        }

        if (args.length != 2) {
            p.sendMessage(plugin.getMessage("InvalidArgs.Pay"));
            Utils.playErrorSound(p);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);

        if (target == null) {
            p.sendMessage(plugin.getMessage("PlayerNotFound"));
            Utils.playErrorSound(p);
            return true;
        }

        if (new StorageManager().getStorageConfig().getBoolean("PayToggle." + target.getName())) {
            p.sendMessage(plugin.getMessage("Pay.DisabledPayments"));
            Utils.playErrorSound(p);
            return true;
        }

        if (target == p) {
            p.sendMessage(plugin.getMessage("Pay.NoSelf"));
            Utils.playErrorSound(p);
            return true;
        }

        if (args[1].startsWith("-")) {
            p.sendMessage(plugin.getMessage("InvalidArgs.Pay"));
            Utils.playErrorSound(p);
            return true;
        }

        double money;
        try {
            money = Double.parseDouble(args[1]);
        } catch (Exception e) {
            p.sendMessage(plugin.getMessage("InvalidArgs.Pay"));
            Utils.playErrorSound(p);
            return true;
        }

        if (!EconomyPlus.veco.has(p, money)) {
            p.sendMessage(plugin.getMessage("Pay.NoMoney"));
            Utils.playErrorSound(p);
            return true;
        }

        Utils utilities = new Utils();

        EconomyResponse resp = EconomyPlus.veco.withdrawPlayer(p, money);
        if (resp.transactionSuccess()) {
            EconomyPlus.veco.depositPlayer(target, resp.amount);
        } else {
            p.sendMessage(plugin.getMessage("Pay.NoMoney"));
            Utils.playErrorSound(p);
            return true;
        }

        Utils.playSuccessSound(p);
        Utils.playSuccessSound(target);

        p.sendMessage(plugin.getMessage("Pay.Self")
                .replaceAll("%money_formatted%", "" + utilities.fixMoney(money))
                .replaceAll("%money%", "" + utilities.format(money))
                .replaceAll("%player%", "" + target.getName()));

        target.sendMessage(plugin.getMessage("Pay.Target")
                .replaceAll("%money_formatted%", "" + utilities.fixMoney(money))
                .replaceAll("%money%", "" + utilities.format(money))
                .replaceAll("%player%", "" + p.getName()));

        return true;
    }
}
