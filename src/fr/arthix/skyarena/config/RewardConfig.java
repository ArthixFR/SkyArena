package fr.arthix.skyarena.config;

import fr.arthix.skyarena.SkyArena;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RewardConfig {

    private File file;
    private FileConfiguration fileConfiguration;

    public RewardConfig(SkyArena plugin, String name) {
        file = new File(plugin.getDataFolder() + "/rewards", name + ".yml");
        File folder = new File(plugin.getDataFolder() + "/rewards");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String name() {
        return file.getName().replaceAll(".yml", "");
    }
}
