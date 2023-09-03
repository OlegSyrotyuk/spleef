package dev.olegsyrotyuk.spleef.game.handler;

import dev.olegsyrotyuk.spleef.Spleef;
import dev.olegsyrotyuk.spleef.game.arena.ArenaState;
import dev.olegsyrotyuk.spleef.player.GamePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

@RequiredArgsConstructor
public class GameHandler implements Listener {

    private final Spleef spleef = Spleef.getInstance();

    @EventHandler
    public void on(BlockBreakEvent event) {
        event.setDropItems(false);
        if (event.getBlock().getType() != Material.SNOW_BLOCK) {
            event.setCancelled(true);
        }
        GamePlayer gamePlayer = spleef.getObjectPool().get(event.getPlayer().getName());
        if (gamePlayer.getArena() != null &&
                gamePlayer.getArena().getState() == ArenaState.STARTING &&
                event.getBlock().getType() == Material.SNOW_BLOCK) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = spleef.getObjectPool().get(player.getName());
        if (gamePlayer.getArena() != null && gamePlayer.getArena().getState() == ArenaState.STARTING) {
            event.setCancelled(true);
        }
        if (gamePlayer.getArena() != null && player.getLocation().getY() < 65) {
            gamePlayer.setAlive(false);
        }
    }

    @EventHandler
    public void on(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity != null && entity.getCustomName() != null && entity.getCustomName().contains("Встать в очередь")) {
            spleef.getQueueManager().addToQueue(spleef.getObjectPool().get(event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        GamePlayer gamePlayer = spleef.getObjectPool().get(event.getPlayer().getName());
        if (gamePlayer.getArena() != null) {
            gamePlayer.setAlive(false);
            gamePlayer.getArena().checkVictory();
        }
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(CraftItemEvent event) {
        event.setCancelled(true);
    }

}
