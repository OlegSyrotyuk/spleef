package dev.olegsyrotyuk.spleef.player;

import dev.olegsyrotyuk.spleef.game.arena.Arena;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ua.olegsyrotyuk.athena.client.annotation.Id;
import ua.olegsyrotyuk.athena.client.annotation.IgnoreField;

@Getter
@AllArgsConstructor
public class GamePlayer {

    @Id
    private final String name;
    private int games;
    private int wins;
    private int coins;
    @Setter
    @IgnoreField
    private Arena arena;
    @Setter
    @IgnoreField
    private boolean alive;

    public void changeCoins(int coins) {
        if (this.coins + coins < 0)
            return;
        this.coins += coins;
    }

    public void changeWins(int wins) {
        if (this.wins + wins < 0)
            return;
        this.wins += wins;
    }

    public Player getHandle() {
        return Bukkit.getPlayerExact(name);
    }

    public void teleportToSpawn() {
        Player handle = getHandle();
        if (handle != null) {
            handle.getInventory().clear();
            getHandle().teleport(Bukkit.getWorld("lobby").getSpawnLocation());
        }
    }

}
