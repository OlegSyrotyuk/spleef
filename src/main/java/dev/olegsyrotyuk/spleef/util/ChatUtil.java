package dev.olegsyrotyuk.spleef.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class ChatUtil {

    public String transformByCount(int amount, String uno, String duo, String many) {
        int mod10 = amount % 10, mod100 = amount % 100;
        if (mod10 == 1 && mod100 != 11) {
            return uno;
        }
        if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 > 20)) {
            return duo;
        }
        return many;
    }

    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String colorize(String message, Object... args) {
        return colorize(String.format(message, args));
    }

    public String prefixed(String prefix, String message) {
        return colorize("&e&l%s &8> %s", prefix, message);
    }

    public String prefixed(String prefix, String message, Object... args) {
        return prefixed(prefix, String.format(message, args));
    }

    public String getBooleanString(boolean yes, String trueText, String falseText) {
        String color = yes ? "&a" : "&c";
        String message = yes ? trueText : falseText;
        return color + message;
    }

    public HoverEvent makeHoverEvent(List<String> lore) {
        BaseComponent[] comps = new BaseComponent[lore.size()];
        for (int i = 0; i < lore.size(); ++i) {
            comps[i] = new TextComponent(colorize("%s%s", lore.get(i), i == lore.size() - 1 ? "" : "\n"));
        }
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, comps);
    }

    public TextComponent makeTextComponentUncolored(String text) {
        return new TextComponent(TextComponent.fromLegacyText(text));
    }

    public TextComponent makeTextComponent(String text) {
        return makeTextComponentUncolored(colorize(text));
    }

    public TextComponent makeTextComponent(String text, Object... args) {
        return makeTextComponentUncolored(colorize(text, args));
    }

    public void sendClickableMessage(Player player, String msg, List<String> lore, String command) {
        TextComponent component = makeTextComponent(msg);
        if (lore != null) {
            HoverEvent event = makeHoverEvent(lore);
            component.setHoverEvent(event);
        }
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        player.spigot().sendMessage(component);
    }


}
