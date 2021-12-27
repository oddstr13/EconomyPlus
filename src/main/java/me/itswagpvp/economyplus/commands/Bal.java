package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Bal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) {

            if (args.length != 1) {
                sender.sendMessage(plugin.getMessage("InvalidArgs.Balance"));
                return true;
            }

            OfflinePlayer p2 = Bukkit.getServer().getOfflinePlayer(args[0]);

            if (p2 == null) {

                sender.sendMessage(plugin.getMessage("PlayerNotFound"));
                return true;

            }

            if (p2 == sender) {

                sender.sendMessage(plugin.getMessage("NoConsole"));

                return true;
            }

            double balance = EconomyPlus.veco.getBalance(p2);

            sender.sendMessage(plugin.getMessage("Balance.Others")
                    .replaceAll("%money%", "" + new Utils().format(balance))
                    .replaceAll("%money_formatted%", "" + new Utils().fixMoney(balance))
                    .replaceAll("%player%", ""+ p2.getName()));

            Utils.playSuccessSound(sender);

            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {

            if (!p.hasPermission("economyplus.balance")) {
                sender.sendMessage(plugin.getMessage("NoPerms"));
                Utils.playErrorSound(sender);
                return true;
            }

            double balance = EconomyPlus.veco.getBalance(p);

            p.sendMessage(plugin.getMessage("Balance.Self")
                    .replaceAll("%money%", "" + new Utils().format(balance))
                    .replaceAll("%money_formatted%", ""+ new Utils().fixMoney(balance)));

            Utils.playSuccessSound(sender);

            return true;
        }

        if (args.length == 1) {

            OfflinePlayer p2 = Bukkit.getServer().getOfflinePlayer(args[0]);

            if (p2 == sender) {

                double balance = EconomyPlus.veco.getBalance(p);

                sender.sendMessage(plugin.getMessage("Balance.Self")
                        .replaceAll("%money%", "" + new Utils().format(balance))
                        .replaceAll("%money_formatted%", "" + new Utils().fixMoney(balance)));

                Utils.playSuccessSound(sender);

                return true;
            }

            if (!p.hasPermission("economyplus.balance.others")) {
                sender.sendMessage(plugin.getMessage("NoPerms"));
                Utils.playErrorSound(sender);
                return true;
            }

            double balance = EconomyPlus.veco.getBalance(p2);

            sender.sendMessage(plugin.getMessage("Balance.Others")
                    .replaceAll("%money%", "" + new Utils().format(balance))
                    .replaceAll("%money_formatted%", "" + new Utils().fixMoney(balance))
                    .replaceAll("%player%", ""+ p2.getName()));

            Utils.playSuccessSound(sender);

            return true;

        }

        p.sendMessage(plugin.getMessage("InvalidArgs.Balance"));
        Utils.playErrorSound(sender);
        return true;

    }
}
