package dev.olegsyrotyuk.spleef.game;

import com.google.common.collect.Maps;
import com.grinderwolf.swm.api.world.SlimeWorld;
import dev.olegsyrotyuk.spleef.Spleef;
import dev.olegsyrotyuk.spleef.game.arena.Arena;
import dev.olegsyrotyuk.spleef.game.arena.ArenaState;
import dev.olegsyrotyuk.spleef.player.GamePlayer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GameManager {

    private final SlimeWorld SPLEEF_MAP;
    private final Map<UUID, Arena> activeGames = Maps.newConcurrentMap();

    @SneakyThrows
    public void startGame(GamePlayer firstPlayer, GamePlayer secondPlayer) {
        UUID gameId = UUID.randomUUID();
        SlimeWorld slimeWorld = loadWorld(gameId.toString());
        World world = Bukkit.getWorld(slimeWorld.getName());
        Arena arena = new Arena(gameId, world, ArenaState.STARTING, firstPlayer, secondPlayer);
        activeGames.put(gameId, arena);
        arena.initializeGame();
    }

    public void endGame(Arena arena) {
        arena.getFirstPlayer().teleportToSpawn();
        arena.getSecondPlayer().teleportToSpawn();
        deleteWorld(arena.getWorld());
        activeGames.remove(arena.getId());
    }

    @SneakyThrows
    public SlimeWorld loadWorld(String worldName) {
        SlimeWorld world = SPLEEF_MAP.clone(worldName);
        Spleef.getInstance().getSlimePlugin().generateWorld(world);
        return world;
    }

    @SneakyThrows
    public void deleteWorld(World world) {
        Bukkit.unloadWorld(world, false);
        for (Chunk chunk : world.getLoadedChunks()) {
            chunk.unload();
        }
        FileUtils.deleteDirectory(world.getWorldFolder());
    }

    public void clearArenas() {
        activeGames.values().forEach(arena -> deleteWorld(arena.getWorld()));
    }

}
