package dev.olegsyrotyuk.spleef.util;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class ConfigUtil {

    public FileConfiguration getConfig(Plugin plugin, String name) {
        saveDefaultConfig(plugin, name);
        File file = new File(plugin.getDataFolder(), name);
        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig(Plugin plugin, String name, Object config) {
        File file = new File(plugin.getDataFolder(), name);
        try {
            ((FileConfiguration) config).save(file);
        } catch (IOException ex) {
            throw new RuntimeException("Could not save config to " + file, ex);
        }
    }

    public void saveDefaultConfig(Plugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
    }
}
