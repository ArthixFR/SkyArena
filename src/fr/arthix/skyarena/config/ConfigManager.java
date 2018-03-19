package fr.arthix.skyarena.config;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.rewards.Rarity;
import fr.arthix.skyarena.rewards.RewardsManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class ConfigManager {

    private List<ArenaConfig> arenaConfigs = new ArrayList<>();
    private List<RewardConfig> rewardConfigs = new ArrayList<>();
    private SkyArena plugin;
    private ArenaManager arenaManager;
    private RewardsManager rewardsManager;

    public ConfigManager(SkyArena plugin) {
        this.plugin = plugin;
        arenaManager = plugin.getArenaManager();
        rewardsManager = plugin.getRewardsManager();
    }

    public List<ArenaConfig> getArenaConfigs() {
        return arenaConfigs;
    }

    public void setArenaConfigs(List<ArenaConfig> arenaConfigs) {
        this.arenaConfigs = arenaConfigs;
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

    public void createArenaConfig(String name, Location border1, Location border2, int wavesMax, String bossName, ArenaDifficulty difficulty) {
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

    public void setArenaConfig(Arena arena, String path, Object value) {
        ArenaConfig arenaConfig = getArenaConfig(arena.getArenaName());
        if (arenaConfig != null) {
            arenaConfig.getFileConfiguration().set(path, value);
            arenaConfig.save();
        }
    }

    public ArenaConfig getArenaConfig(String name) {
        String formattedFile = name.toLowerCase().replaceAll(" ", "_");

        for (ArenaConfig arenaConfig : arenaConfigs) {
            if (arenaConfig.name().equals(formattedFile)) {
                return arenaConfig;
            }
        }
        return null;
    }


    public List<RewardConfig> getRewardConfigs() {
        return rewardConfigs;
    }

    public void setRewardConfigs(List<RewardConfig> rewardConfigs) {
        this.rewardConfigs = rewardConfigs;
    }

    public void loadRewards() {
        File folder = new File(plugin.getDataFolder() + "/rewards");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (String arenaDifficulty : ArenaDifficulty.getDifficulties()) {
            File file = new File(plugin.getDataFolder() + "/rewards", arenaDifficulty.toUpperCase() + ".yml");
            if (!file.exists()) {
                plugin.getLogger().log(Level.WARNING, "Rewards file for difficulty " + arenaDifficulty.toUpperCase() + " doesn't exist ! Try creating one...");
                try {
                    if (file.createNewFile()) {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                        writer.write("####################################################\n");
                        writer.write("#                                                  #\n");
                        writer.write("#                     EXEMPLE                      #\n");
                        writer.write("#                                                  #\n");
                        writer.write("####################################################\n");

                        writer.write("#bedrock: # Un nom random, ne pas le mettre plusieurs fois.\n");
                        writer.write("#  name: \"&c&lBedrock\" # Le nom de l'item, ne sera pas affiché quand le joueur gagnera l'item.\n");
                        writer.write("#  material: BEDROCK # Type de l'item.\n");
                        writer.write("#  metadata: 0 # La metadata de l'item.\n");
                        writer.write("#  amount: 1 # La quantité.\n");
                        writer.write("#  chance: 10 # La chance de gagner l'item.\n");
                        writer.write("#  rarity: RARE # La rareté. Purement visuel.\n");
                        writer.write("rewards: []\n");

                        writer.close();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            plugin.getLogger().log(Level.INFO, "Loading reward list for difficulty " + file.getName().replaceAll(".yml", ""));
            RewardConfig rewardConfig = new RewardConfig(plugin, file.getName().replaceAll(".yml", ""));
            rewardConfigs.add(rewardConfig);
            FileConfiguration fileConfiguration = rewardConfig.getFileConfiguration();
            ConfigurationSection cs = fileConfiguration.getConfigurationSection("rewards");
            if (cs != null) {
                for (String str : cs.getKeys(false)) {
                    Material material = Material.getMaterial(fileConfiguration.getString("rewards." + str + ".material"));
                    String name = fileConfiguration.getString("rewards." + str + ".name").replaceAll("&", "§");
                    int amount = fileConfiguration.getInt("rewards." + str + ".amount");
                    byte metadata = (byte) fileConfiguration.getInt("rewards." + str + ".metadata");
                    int percentage = fileConfiguration.getInt("rewards." + str + ".chance");
                    Rarity rarity = Rarity.getRarityByName(fileConfiguration.getString("rewards." + str + ".rarity"));

                    rewardsManager.addReward(material, name, amount, metadata, percentage, rarity, ArenaDifficulty.getDifficultyByName(arenaDifficulty.toUpperCase()));
                }
            }
        }
    }

    public RewardConfig getRewardConfig(ArenaDifficulty arenaDifficulty) {
        for (RewardConfig rewardConfig : rewardConfigs) {
            if (rewardConfig.name().equals(arenaDifficulty.toString())) {
                return rewardConfig;
            }
        }
        return null;
    }
}
