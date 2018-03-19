package fr.arthix.skyarena.arena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.rewards.Rewards;
import fr.arthix.skyarena.rewards.RewardsManager;
import fr.arthix.skyarena.titleapi.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Arena {
    /*

    A mettre dans l'arène :

     - participants
     - zone
     - point de spawn joueurs
     - point de spawn mobs
     - vague
     - boss
     - récompenses
     - status
     */

    private String name;
    private List<UUID> players = new ArrayList<>();
    private List<Location> playersSpawn = new ArrayList<>();
    private List<Location> mobsSpawn = new ArrayList<>();
    private List<Location> bossSpawn = new ArrayList<>();
    private Map<UUID, List<Rewards>> rewards = new HashMap<>();
    private Location border1;
    private Location border2;
    private int maxWaves;
    private int actualWave;
    private String bossName;
    private ArenaDifficulty difficulty;
    private ArenaState state;
    private int mobWave;

    private SkyArena plugin;

    public Arena(SkyArena plugin, String arenaName, Location border1, Location border2, int maxWaves, String bossName, ArenaDifficulty difficulty) {
        this.name = arenaName;
        this.border1 = border1;
        this.border2 = border2;
        this.maxWaves = maxWaves;
        this.actualWave = 0;
        this.bossName = bossName;
        this.difficulty = difficulty;
        this.state = ArenaState.FREE;
        this.mobWave = -1;
        this.plugin = plugin;
        rewardTimer();
    }

    public String getArenaName() {
        return name;
    }

    public ArenaDifficulty getDifficulty() {
        return difficulty;
    }

    public int getMaxWaves() {
        return maxWaves;
    }

    public ArenaState getArenaState() {
        return state;
    }

    public List<Location> getMobsSpawn() {
        return mobsSpawn;
    }

    public List<Location> getPlayersSpawn() {
        return playersSpawn;
    }

    public String getBossName() {
        return bossName;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public int getActualWave() {
        return actualWave;
    }

    public List<Location> getBorder() {
        List<Location> locations = new ArrayList<>();
        locations.add(border1);
        locations.add(border2);
        return locations;
    }

    public void setArenaName(String arenaName) {
        this.name = arenaName;
    }

    public void setArenaState(ArenaState arenaState) {
        this.state = arenaState;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public void setDifficulty(ArenaDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setMobsSpawn(List<Location> mobsSpawn) {
        if (mobsSpawn != null) {
            this.mobsSpawn = mobsSpawn;
        }
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public void setPlayersSpawn(List<Location> playersSpawn) {
        if (playersSpawn != null) {
            this.playersSpawn = playersSpawn;
        }
    }

    public void setMaxWaves(int maxWaves) {
        this.maxWaves = maxWaves;
    }

    public void setActualWave(int actualWave) {
        this.actualWave = actualWave;
    }

    public void addPlayers(List<UUID> players) {
        this.players.addAll(players);
    }

    public void removePlayer(UUID uuid) {
        if (this.players.contains(uuid)) {
            this.players.remove(uuid);
        }
    }

    public void addPlayerSpawn(Location loc) {
        this.playersSpawn.add(loc);
    }

    public void addMobsSpawn(Location loc) {
        this.mobsSpawn.add(loc);
    }

    public int getMobWave() {
        return mobWave;
    }

    public void setMobWave(int mobWave) {
        this.mobWave = mobWave;
    }

    public void sendTitle(String title, String subtitle) {
        for (UUID uuid : this.getPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                TitleAPI.sendTitle(p, 10, 20, 10, title, subtitle);
            }
        }
    }

    public List<Location> getBossSpawn() {
        return bossSpawn;
    }

    public void setBossSpawn(List<Location> bossSpawn) {
        this.bossSpawn = bossSpawn;
    }

    public void addBossSpawn(Location loc) {
        this.bossSpawn.add(loc);
    }

    public List<Player> getAlivePlayers() {
        List<Player> alive = new ArrayList<>();
        for (UUID uuid : this.players) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                if (p.getGameMode() != GameMode.ADVENTURE) {
                    alive.add(p);
                }
            }
        }
        return alive;
    }

    public Map<UUID, List<Rewards>> getRewards() {
        return rewards;
    }

    public List<Rewards> getRewards(UUID uuid) {
        return rewards.get(uuid);
    }

    public void setRewards(Map<UUID, List<Rewards>> rewards) {
        this.rewards = rewards;
    }

    public void addReward(UUID uuid, Rewards is) {
        if (!this.rewards.containsKey(uuid)) {
            this.rewards.put(uuid, new ArrayList<>());
        }
        this.rewards.get(uuid).add(is);
    }

    public void addReward(UUID uuid, List<Rewards> is) {
        if (!this.rewards.containsKey(uuid)) {
            this.rewards.put(uuid, new ArrayList<>());
        }
        this.rewards.get(uuid).addAll(is);
    }

    public void rewardTimer() {
        RewardsManager rewardsManager = plugin.getRewardsManager();
        rewardsManager.setRemaining(this, 30);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (this.getArenaState() == ArenaState.SELECTING_REWARD) {
                int remaining = rewardsManager.getRemaining(this);
                rewardsManager.setRemaining(this, remaining - 1);
                if (remaining == 0) {
                    this.setArenaState(ArenaState.IN_PROGRESS);
                    for (Player p : this.getAlivePlayers()) {
                        this.addReward(p.getUniqueId(), rewardsManager.getSelectedReward(p, this));
                        rewardsManager.removeSelectedReward(p.getUniqueId());
                        p.closeInventory();
                    }
                    plugin.getArenaManager().spawnMobs(this, actualWave);
                    rewardsManager.setRemaining(this, 30);
                }
            }
        }, 0L, 20L);
    }
}
