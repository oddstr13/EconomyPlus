package me.itswagpvp.economyplus.commands;

import me.itswagpvp.economyplus.EconomyPlus;
import me.itswagpvp.economyplus.misc.Utils;
import me.itswagpvp.economyplus.vault.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.itswagpvp.economyplus.EconomyPlus.plugin;

public class Eco implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 3) {
            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[0]);

            if (args[2].startsWith("-")) {
                sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
                Utils.playErrorSound(sender);
                return true;
            }

            String arg;
            if (args[2].contains(",")) {
                arg = args[2].replaceAll(",", ".");
            } else {
                arg = args[2];
            }

            double value = Double.parseDouble(arg);

            Economy money = new Economy(p, value);
            Utils utility = new Utils();

            if (args[1].equalsIgnoreCase("set")) {
                if (!sender.hasPermission("economyplus.eco.set")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                money.setBalance();

                sender.sendMessage(plugin.getMessage("Money.Done"));

                if (p.getPlayer() != null)  {
                    p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                            .replaceAll("%money_formatted%", "" + utility.fixMoney(value))
                            .replaceAll("%money%", "" + utility.format(value)));

                    Utils.playSuccessSound(p.getPlayer());
                }

                Utils.playSuccessSound(sender);

                return true;
            }

            if (args[1].equalsIgnoreCase("take")) {
                if (!sender.hasPermission("economyplus.eco.take")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                double balance = EconomyPlus.veco.getBalance(p);
                EconomyPlus.veco.withdrawPlayer(p, Double.min(balance, value));

                balance = EconomyPlus.veco.getBalance(p);

                sender.sendMessage(plugin.getMessage("Money.Done"));

                if (p.getPlayer() != null) {
                    p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                            .replaceAll("%money%", "" + utility.fixMoney(balance)));
                    Utils.playSuccessSound(p.getPlayer());
                }

                Utils.playSuccessSound(sender);

                return true;
            }

            if (args[1].equalsIgnoreCase("give")) {
                if (!sender.hasPermission("economyplus.eco.give")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                EconomyPlus.veco.depositPlayer(p, value);

                sender.sendMessage(plugin.getMessage("Money.Done"));

                if (p.getPlayer() != null) {
                    p.getPlayer().sendMessage(plugin.getMessage("Money.Refreshed")
                            .replaceAll("%money_formatted%", "" + utility.fixMoney(value))
                            .replaceAll("%money%", "" + utility.format(value)));
                    Utils.playSuccessSound(p.getPlayer());
                }

                Utils.playSuccessSound(sender);

                return true;
            }

            return true;

        }

        if (args.length == 2) {
            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[0]);

            if (!args[0].equalsIgnoreCase("reset")) {

                if (!sender.hasPermission("economyplus.eco.reset")) {
                    sender.sendMessage(plugin.getMessage("NoPerms"));
                    Utils.playErrorSound(sender);
                    return true;
                }

                Economy eco = new Economy(p, plugin.getConfig().getDouble("Starting-Balance"));
                eco.setBalance();

                sender.sendMessage(plugin.getMessage("Money.Done"));

                if (p.getPlayer() != null) {
                    p.getPlayer().sendMessage(plugin.getMessage("Money.Reset"));
                    Utils.playErrorSound(p.getPlayer());
                }

                Utils.playSuccessSound(sender);

                return true;
            }
        }

        sender.sendMessage(plugin.getMessage("InvalidArgs.Eco"));
        Utils.playErrorSound(sender);
        return true;
    }
}
