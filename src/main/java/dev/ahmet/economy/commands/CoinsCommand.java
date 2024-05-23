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

public class CoinsCommand implements CommandExecutor {

    private final EconomyManager manager;

    public CoinsCommand() {
        this.manager = Economy.getInstance().getManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String prefix = Economy.getInstance().getPrefix();

        if (args.length == 0) {
            player.sendMessage(prefix + "Du hast derzeit " + manager.getCoins(player.getUniqueId()) + " Coins§7.");
            return true;
        }

        if (!player.hasPermission("economy.admin")) {
            player.sendMessage("§cDazu hast du keinen Zugriff!");
            return true;
        }

        if(args.length != 3) {
            if(args[0].equalsIgnoreCase("reset") && args.length == 2) {
                UUID target = UUIDFetcher.getUUID(args[1]);
                String name = UUIDFetcher.getName(target);
                manager.resetCoins(target);
                player.sendMessage(prefix + "Das Konto von " + name + " wurde §6zurückgesetzt§7.");
                return true;
            }
            player.sendMessage("§cUsage");
            return true;
        }

        UUID target = UUIDFetcher.getUUID(args[1]);
        if(isInt(args[1])) {
            player.sendMessage(prefix + "Dieser Befehl ist falsch geschrieben");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        if(target == null) {
            player.sendMessage(prefix + "Es gibt diesen Spieler nicht");
            return true;
        }

        if(!isInt(args[2])) {
            player.sendMessage(prefix + "Du musst eine Zahl eingeben");
            return true;
        }

        if(amount < 0) {
            player.sendMessage(prefix + "Du musst eine positive Zahl eingeben");
            return true;
        }

        String name = UUIDFetcher.getName(target);
        switch (args[0]) {
            case "add" -> {
                manager.addCoins(target, amount);
                player.sendMessage(prefix + name + " wurden §b" + amount + "Coins §ahinzugefügt§7");
            }
            case "remove" -> {
                manager.removeCoins(target, amount);
                player.sendMessage(prefix + name + " wurden §b" + amount + "Coins §cabgezogen§7");
            }
            case "set" -> {
                manager.setCoins(target, amount);
                player.sendMessage(prefix + "Das Konto von " + name + " wurde auf §b"+ amount + "Coins §agesetzt§7.");
            }
        }
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
