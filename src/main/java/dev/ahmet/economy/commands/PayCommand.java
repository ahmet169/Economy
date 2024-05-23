package dev.ahmet.economy.commands;

import dev.ahmet.economy.Economy;
import dev.ahmet.economy.managers.EconomyManager;
import dev.ahmet.economy.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PayCommand implements CommandExecutor {

    private final EconomyManager manager;

    public PayCommand() {
        this.manager = Economy.getInstance().getManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String prefix = Economy.getInstance().getPrefix();

        if(args.length != 2) {
            player.sendMessage(prefix + "Bitte benutze /pay <Betrag>");
            return true;
        }

        UUID targetUuid = UUIDFetcher.getUUID(args[0]);
        if(targetUuid != null) {
            Player targetPlayer = Bukkit.getPlayer(targetUuid);
            if(targetPlayer == null) {
                player.sendMessage(prefix + "Dieser Spieler ist nicht online");
                return true;
            }
        } else {
            player.sendMessage(prefix + "Dieser Spieler existiert nicht");
            return true;
        }

        if(!manager.getToggle(targetUuid)) {
            player.sendMessage(prefix + "§c" + UUIDFetcher.getName(targetUuid) + " nimmt kein Geld an");
            return true;
        }

        int amount = Integer.parseInt(args[1]);
        if(!isInt(args[1])) {
            player.sendMessage(prefix + "Du musst eine Zahl eingeben");
            return true;
        }

        if(amount < 0) {
            player.sendMessage(prefix + "Du musst eine positive Zahl eingeben");
            return true;
        }

        if(manager.getCoins(player.getUniqueId()) < amount) {
            player.sendMessage(prefix + "Du hast nicht genug Geld");
            return true;
        }

        if(args[0].equalsIgnoreCase(player.getName())) {
            player.sendMessage(prefix + "Du kannst dir kein Geld geben");
            return true;
        }

        manager.removeCoins(player.getUniqueId(), amount);
        manager.addCoins(targetUuid, amount);

        Player targetPlayer = Bukkit.getPlayer(targetUuid);
        if(targetPlayer != null) {
            targetPlayer.sendMessage(prefix + "§b" + player.getName() + " §7hat dir §b" + amount + " Coins §7gegeben.");
        }

        player.sendMessage("Du hast §b" + UUIDFetcher.getName(targetUuid) + " " + amount + " Coins §7gegeben.");

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
