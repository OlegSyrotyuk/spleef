package dev.olegsyrotyuk.spleef;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.plugin.SWMPlugin;
import com.grinderwolf.swm.plugin.config.WorldData;
import dev.olegsyrotyuk.spleef.game.GameManager;
import dev.olegsyrotyuk.spleef.game.QueueManager;
import dev.olegsyrotyuk.spleef.game.handler.GameHandler;
import dev.olegsyrotyuk.spleef.handler.PlayerHandler;
import dev.olegsyrotyuk.spleef.player.GamePlayer;
import dev.olegsyrotyuk.spleef.util.ChatUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ua.olegsyrotyuk.athena.client.ObjectPool;
import ua.olegsyrotyuk.athena.client.Storage;
import ua.olegsyrotyuk.athena.client.impl.local.LocalStorageManager;

@Getter
public class Spleef extends JavaPlugin {

    @Getter
    private static Spleef instance;

    private final LocalStorageManager storageManager = new LocalStorageManager();
    private Storage<GamePlayer> storage;
    private ObjectPool<GamePlayer> objectPool;
    private SlimePlugin slimePlugin;
    private SlimeLoader slimeLoader;
    private QueueManager queueManager;
    private GameManager gameManager;
    private final WorldData DEFAULT_WORLD_DATA = new WorldData();

    {
        DEFAULT_WORLD_DATA.setLoadOnStartup(true);
        DEFAULT_WORLD_DATA.setSpawn("-13, 75, 8");
        DEFAULT_WORLD_DATA.setDifficulty("easy");
        DEFAULT_WORLD_DATA.setAllowAnimals(false);
        DEFAULT_WORLD_DATA.setAllowMonsters(false);
        DEFAULT_WORLD_DATA.setReadOnly(true);
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        slimePlugin = SWMPlugin.getInstance();
        slimeLoader = slimePlugin.getLoader("file");
        String MONGO_URI = System.getenv("MONGO_URI");
        String MONGO_DATABASE = System.getenv("MONGO_DATABASE");
        storageManager.connect(MONGO_URI, MONGO_DATABASE);
        storage = storageManager.create("spleef_players", GamePlayer.class);
        objectPool = storage.newObjectPool();
        objectPool.setDefaultObject(new GamePlayer(
                null,
                0,
                0,
                0,
                null,
                false));
        queueManager = new QueueManager();
        gameManager = new GameManager(getSlimePlugin().loadWorld(
                getSlimeLoader(),
                "spleef",
                false,
                DEFAULT_WORLD_DATA.toPropertyMap()
        ));
        registerListeners(
                new PlayerHandler(),
                new GameHandler()
        );
        spawnNpc();
    }

    @Override
    public void onDisable() {
        gameManager.clearArenas();
        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(Entity::remove));
        objectPool.saveAll(true);
        storageManager.disconnect();
    }

    private void spawnNpc() {
        World world = Bukkit.getWorld("lobby");
        Location location = new Location(world, 0, 69, -27);
        Villager entity = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
        entity.setCustomName(ChatUtil.colorize("&a&lВстать в очередь"));
        entity.setCustomNameVisible(true);
        entity.setAI(false);
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
