package dev.olegsyrotyuk.spleef.listener;

import dev.olegsyrotyuk.spleef.Spleef;
import dev.olegsyrotyuk.spleef.util.ChatUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Spleef spleef = Spleef.getInstance();

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        spleef.getObjectPool().get(player.getName());
        player.setPlayerListHeaderFooter(TextComponent.fromLegacyText(ChatUtil.colorize("&b&lSpleef")),
                TextComponent.fromLegacyText(ChatUtil.colorize("&a&lПриятной игры!")));
        if (player.isOp()) {
            player.setPlayerListName(ChatUtil.colorize("&c%s", player.getName()));
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        spleef.getObjectPool().invalidate(player.getName());
    }

}
