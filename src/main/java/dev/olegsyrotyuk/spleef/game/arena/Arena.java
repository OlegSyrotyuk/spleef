package dev.olegsyrotyuk.spleef.game.arena;

import com.destroystokyo.paper.Title;
import dev.olegsyrotyuk.spleef.Spleef;
import dev.olegsyrotyuk.spleef.player.GamePlayer;
import dev.olegsyrotyuk.spleef.util.ChatUtil;
import dev.olegsyrotyuk.spleef.util.LocationUtil;
import dev.olegsyrotyuk.spleef.util.Timer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Arena {

    private final UUID id;
    private World world;
    private ArenaState state;
    private GamePlayer firstPlayer;
    private GamePlayer secondPlayer;
    private final Timer endGameTimer = new Timer(305);
    private final BossBar bossBar = Bukkit.createBossBar(ChatUtil.colorize("&a&lОжидаем начала игры..."), BarColor.RED, BarStyle.SOLID);

    public void initializeGame() {
        Player firstHandle = firstPlayer.getHandle();
        Player secondHandle = secondPlayer.getHandle();

        firstPlayer.setArena(this);
        secondPlayer.setArena(this);
        firstPlayer.setAlive(true);
        secondPlayer.setAlive(true);

        firstHandle.teleport(LocationUtil.strToLoc(Spleef.getInstance().getConfig().getString("arena.firstSpawnLocation")));
        preparePlayer(firstHandle);

        secondHandle.teleport(LocationUtil.strToLoc(Spleef.getInstance().getConfig().getString("arena.secondSpawnLocation")));
        preparePlayer(secondHandle);

        bossBar.addPlayer(firstHandle);
        bossBar.addPlayer(secondHandle);
        endGameTimer.setEverySecond(() -> {
            if (state != ArenaState.IN_GAME) {
                return;
            }
            bossBar.setTitle(ChatUtil.colorize("&a&lДо конца игры: &e%s %s", endGameTimer.getTime(),
                    ChatUtil.transformByCount(endGameTimer.getTime(), "секунда", "секунды", "секунд")));
            checkVictory();
        });
        endGameTimer.setOnFinish(this::handleDraw);
        endGameTimer.start();

        Timer timer = new Timer(5);
        timer.setEverySecond(() -> {
            Title title = Title.builder()
                    .title(ChatUtil.colorize("&aИгра начнется через..."))
                    .subtitle(ChatUtil.colorize("&c%s &a%s",
                            timer.getTime(),
                            ChatUtil.transformByCount(timer.getTime(), "секунда", "секунды", "секунд")))
                    .build();
            firstHandle.sendTitle(title);
            secondHandle.sendTitle(title);
        });
        timer.setOnFinish(() -> {
            state = ArenaState.IN_GAME;
            Title title = Title.builder()
                    .title(ChatUtil.colorize("&a&lУдачной игры!"))
                    .build();
            firstHandle.sendTitle(title);
            secondHandle.sendTitle(title);
        });
        timer.start();
    }

    private void preparePlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(makeSpade());
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 14000, 2));
    }

    private ItemStack makeSpade() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SPADE);
        itemStack.addEnchantment(Enchantment.DIG_SPEED, 5);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatUtil.colorize("&a&lАлмазная лопата"));
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void checkVictory() {
        if (!firstPlayer.isAlive() || firstPlayer.getHandle() == null) {
            handleVictory(secondPlayer);
        } else if (!secondPlayer.isAlive() || secondPlayer.getHandle() == null) {
            handleVictory(firstPlayer);
        } else if (!firstPlayer.isAlive() || firstPlayer.getHandle() == null
                && !secondPlayer.isAlive() || secondPlayer.getHandle() == null) {
            handleDraw();
        }
    }

    public void handleDraw() {
        endGameTimer.cancel();
        Title title = Title.builder()
                .title(ChatUtil.colorize("&c&lНичья"))
                .build();
        firstPlayer.getHandle().sendTitle(title);
        secondPlayer.getHandle().sendTitle(title);
        Spleef.getInstance().getGameManager().endGame(this);
    }

    public void handleVictory(GamePlayer winner) {
        bossBar.removeAll();
        endGameTimer.cancel();
        winner.changeCoins(100);
        winner.changeWins(1);
        winner.getHandle().sendTitle(Title.builder()
                .title(ChatUtil.colorize("&a&lПобеда!"))
                .build());
        Spleef.getInstance().getGameManager().endGame(this);
    }

}
