package dev.olegsyrotyuk.spleef.game;

import com.google.common.collect.Lists;
import dev.olegsyrotyuk.spleef.Spleef;
import dev.olegsyrotyuk.spleef.player.GamePlayer;
import dev.olegsyrotyuk.spleef.util.ChatUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QueueManager {

    private final List<GamePlayer> queue = Lists.newArrayList();

    public void addToQueue(GamePlayer player) {
        if (queue.contains(player)) {
            player.getHandle().sendMessage(ChatUtil.prefixed("Spleef", "&cВы уже находитесь в очереди."));
            return;
        }
        queue.add(player);
        player.getHandle().sendMessage(ChatUtil.prefixed("Spleef", "&aВы были добавлены в очередь. Ожидайте других игроков."));
        if (queue.size() == 2) {
            GamePlayer firstPlayer = queue.get(0);
            GamePlayer secondPlayer = queue.get(1);
            Spleef.getInstance().getGameManager().startGame(firstPlayer, secondPlayer);
            queue.remove(firstPlayer);
            queue.remove(secondPlayer);
        }
    }

}
