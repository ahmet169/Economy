package dev.ahmet.economy.commands;

import dev.ahmet.economy.Economy;
import dev.ahmet.economy.managers.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PayAllCommand implements CommandExecutor {

    private final EconomyManager manager;

    public PayAllCommand() {
        this.manager = Economy.getInstance().getManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String prefix = Economy.getInstance().getPrefix();

        if(!player.hasPermission("economy.payall")) {
            player.sendMessage("Dazu hast du keinen Zugriff!");
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(prefix + "Bitte benutze /payall <Betrag>");
            return true;
        }

        if(!isInt(args[0])) {
            player.sendMessage(prefix + "Du musst eine Zahl eingeben");
            return true;
        }

        int amount = Integer.parseInt(args[0]);

        if(amount < 0) {
            player.sendMessage(prefix + "Du musst eine positive Zahl eingeben");
            return true;
        }

        if(manager.getCoins(player.getUniqueId()) < amount) {
            player.sendMessage(prefix + "Du hast nicht genug Geld");
            return true;
        }

        List<Player> size = new ArrayList<>();
        var onlinePlayers =  Bukkit.getOnlinePlayers();

        for (Player onlinePlayer : onlinePlayers) {
            if(manager.getToggle(onlinePlayer.getUniqueId())) {
                size.add(onlinePlayer);
            }
        }

        int completeAmount = amount * size.size();

        manager.removeCoins(player.getUniqueId(), completeAmount);

        for (Player onlinePlayer : onlinePlayers) {
            if(manager.getToggle(onlinePlayer.getUniqueId())) {
                manager.addCoins(onlinePlayer.getUniqueId(), amount);
                onlinePlayer.sendMessage("§b" + player.getName() + "§7 hat dir " + amount + " Coins gegeben");
            }
        }
        player.sendMessage("§7Du hast allen Spielern " + amount + " Coins gegeben");

        return false;
    }

    public boolean isInt(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
