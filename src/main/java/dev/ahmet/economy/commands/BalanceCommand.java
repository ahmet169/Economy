package dev.ahmet.economy.commands;

import dev.ahmet.economy.Economy;
import dev.ahmet.economy.managers.EconomyManager;
import dev.ahmet.economy.utils.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BalanceCommand implements CommandExecutor {

    private final EconomyManager manager;


    public BalanceCommand() {
        this.manager = Economy.getInstance().getManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String prefix = Economy.getInstance().getPrefix();
        
        if(args.length == 0) {
            player.sendMessage(prefix + "Du hast derzeit §e" + manager.getCoins(player.getUniqueId()) + " Coins§7.");
            return true;
        }

        if(player.hasPermission("economy.see")) {
            UUID uuid = UUIDFetcher.getUUID(args[0]);
            String name = UUIDFetcher.getName(uuid);
           if(args.length > 1) {
               int coins = manager.getCoins(uuid);
               player.sendMessage(prefix + name + " hat §e" + coins + " Coins§7.");
           }
        }

        return false;
    }
}
