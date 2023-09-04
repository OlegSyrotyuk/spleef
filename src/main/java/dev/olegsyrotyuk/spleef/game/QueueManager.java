package dev.olegsyrotyuk.spleef.game;

import com.google.common.collect.Lists;
import dev.olegsyrotyuk.spleef.Spleef;
import dev.olegsyrotyuk.spleef.player.GamePlayer;

import java.util.List;

public class QueueManager {

    private final List<GamePlayer> queue = Lists.newArrayList();

    public void addToQueue(GamePlayer player) {
        if (queue.contains(player)) {
            player.sendMessage("&cВы уже находитесь в очереди.");
            return;
        }
        queue.add(player);
        player.sendMessage("&aВы были добавлены в очередь. Ожидайте других игроков.");
        if (queue.size() == 2) {
            GamePlayer firstPlayer = queue.get(0);
            GamePlayer secondPlayer = queue.get(1);
            Spleef.getInstance().getGameManager().startGame(firstPlayer, secondPlayer);
            queue.remove(firstPlayer);
            queue.remove(secondPlayer);
        }
    }

}
