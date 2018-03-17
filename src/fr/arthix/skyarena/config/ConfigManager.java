package fr.arthix.skyarena.config;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class ConfigManager {

    private List<ArenaConfig> arenaConfigs = new ArrayList<>();
    private SkyArena plugin;
    private ArenaManager arenaManager;

    public ConfigManager(SkyArena plugin) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
    }

    public void loadArenas() {
        File folder = new File(plugin.getDataFolder() + "/arenas");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File[] files = folder.listFiles();

        for (File file : files) {
            plugin.getLogger().log(Level.INFO, "Loading arena " + file.getName().replaceAll(".yml", ""));
            ArenaConfig arenaConfig = new ArenaConfig(plugin, file.getName().replaceAll(".yml", ""));
            arenaConfigs.add(arenaConfig);
            FileConfiguration fileConfiguration = arenaConfig.getFileConfiguration();
            Arena arena = arenaManager.createArena(fileConfiguration.getString("name"), (Location) fileConfiguration.get("borders.1"), (Location) fileConfiguration.get("borders.2"), fileConfiguration.getInt("waves"), fileConfiguration.getString("bossName"), ArenaDifficulty.valueOf(fileConfiguration.getString("difficulty")));
            arena.setMobsSpawn((List<Location>) fileConfiguration.getList("mobs"));
            arena.setPlayersSpawn((List<Location>) fileConfiguration.getList("players"));
            arena.setBossSpawn((List<Location>) fileConfiguration.getList("boss"));
        }
    }

    public void createConfig(String name, Location border1, Location border2, int wavesMax, String bossName, ArenaDifficulty difficulty) {
        ArenaConfig arenaConfig = new ArenaConfig(plugin, name.toLowerCase().replaceAll(" ", "_"));
        arenaConfigs.add(arenaConfig);
        FileConfiguration fileConfiguration = arenaConfig.getFileConfiguration();

        fileConfiguration.set("name", name);
        fileConfiguration.set("borders.1", border1);
        fileConfiguration.set("borders.2", border2);
        fileConfiguration.set("waves", wavesMax);
        fileConfiguration.set("bossName", bossName);
        fileConfiguration.set("difficulty", difficulty.name());

        arenaConfig.save();
    }

    public void setConfig(Arena arena, String path, Object value) {
        ArenaConfig arenaConfig = getConfig(arena.getArenaName());
        if (arenaConfig != null) {
            arenaConfig.getFileConfiguration().set(path, value);
            arenaConfig.save();
        }
    }

    public ArenaConfig getConfig(String name) {
        String formattedFile = name.toLowerCase().replaceAll(" ", "_");

        for (ArenaConfig arenaConfig : arenaConfigs) {
            if (arenaConfig.name().equals(formattedFile)) {
                return arenaConfig;
            }
        }
        return null;
    }
}
