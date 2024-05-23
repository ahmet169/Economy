package dev.ahmet.economy.listeners;

import dev.ahmet.economy.Economy;
import dev.ahmet.economy.managers.EconomyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private EconomyManager manager;

    public PlayerJoinListener() {
        this.manager = Economy.getInstance().getManager();
    }

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!manager.isRegistered(player.getUniqueId())) {
            manager.createPlayer(player.getUniqueId());
        }
    }
}
