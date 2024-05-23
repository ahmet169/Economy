package dev.ahmet.economy.commands;

import dev.ahmet.economy.Economy;
import dev.ahmet.economy.managers.EconomyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PayToggleCommand implements CommandExecutor {

    private final EconomyManager manager;

    public PayToggleCommand() {
        this.manager = Economy.getInstance().getManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String prefix = Economy.getInstance().getPrefix();

        if(!player.hasPermission("economy.togglepay")) {
            player.sendMessage("Dazu hast du keinen Zugriff!");
            return true;
        }

        if(manager.getToggle(player.getUniqueId())) {
            player.sendMessage(prefix + "Du nimmst nun kein Geld mehr an");
            manager.updateToggle(player.getUniqueId(), false);
        } else {
            player.sendMessage(prefix + "Du nimmst nun wieder Geld an");
            manager.updateToggle(player.getUniqueId(), true);
        }

        return false;
    }
}
