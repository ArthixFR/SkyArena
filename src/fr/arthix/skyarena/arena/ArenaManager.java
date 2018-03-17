package fr.arthix.skyarena.arena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.gui.GuiManager;
import fr.arthix.skyarena.rewards.RewardsManager;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class ArenaManager {

    private List<Arena> arenas = new ArrayList<>();
    public Map<UUID, List<Location>> arenaCreationLocation = new HashMap<>();
    private SkyArena plugin;
    private MobManager mobManager;
    private GuiManager guiManager;
    private RewardsManager rewardsManager;

    public ArenaManager(SkyArena plugin) {
        this.plugin = plugin;
        mobManager = plugin.getMythicMobs().getMobManager();
        guiManager = plugin.getGuiManager();
        rewardsManager = plugin.getRewardsManager();
    }

    public Arena createArena(String name, Location border1, Location border2, int wavesMax, String bossName, ArenaDifficulty difficulty) {
        Arena arena = new Arena(plugin, name, border1, border2, wavesMax, bossName, difficulty);
        arenas.add(arena);
        return arena;
    }

    public void addPlayerToArena(UUID p, Arena a) {
        if (a.getPlayers().size() > a.getPlayersSpawn().size()) {
            return;
        }
        Location spawn = a.getPlayersSpawn().get(a.getPlayers().size());
        Bukkit.getPlayer(p).teleport(spawn);
        a.addPlayers(Arrays.asList(p));
    }

    public void addPlayerToArena(List<UUID> p, Arena a) {
        //System.out.println("Players in list : " + p.size());
        //System.out.println("Players in arena : " + a.getPlayers().size());
        //System.out.println("Spawn players in arena : " + a.getPlayersSpawn().size());
        if (a.getPlayers().size() + p.size() > a.getPlayersSpawn().size()) {
            return;
        }
        for (UUID uuid : p) {
            //System.out.println("Teleporting " + Bukkit.getOfflinePlayer(uuid).getName());
            Location loc = a.getPlayersSpawn().get(a.getPlayers().size());
            Bukkit.getPlayer(uuid).teleport(loc);
            a.addPlayers(Arrays.asList(uuid));
        }
    }

    public Arena getArena(Player p) {
        return arenas.stream().findFirst().filter((a) -> a.getPlayers().contains(p.getUniqueId())).orElse(null);
    }

    public Arena getArena(String name) {
        return arenas.stream().findFirst().filter((a) -> a.getArenaName().equals(name)).orElse(null);
    }

    public Arena getArena(Location loc) {
        for (Arena arena : arenas) {
            double[] dim = new double[2];
            List<Location> border = arena.getBorder();
            if (border.isEmpty()) {
                return null;
            }
            Location loc1 = border.get(0);
            Location loc2 = border.get(1);

            dim[0] = loc1.getX();
            dim[1] = loc2.getX();
            Arrays.sort(dim);
            if (loc.getX() > dim[1] || loc.getX() < dim[0]) {
                continue;
            }

            dim[0] = loc1.getZ();
            dim[1] = loc2.getZ();
            Arrays.sort(dim);
            if (loc.getZ() > dim[1] || loc.getZ() < dim[0]) {
                continue;
            }

            dim[0] = loc1.getY();
            dim[1] = loc2.getY();
            Arrays.sort(dim);
            if (loc.getY() > dim[1] || loc.getY() < dim[0]) {
                continue;
            }

            return arena;
        }
        return null;
    }

    public List<Arena> getArenasByDifficulty(ArenaDifficulty arenaDifficulty) {
        List<Arena> arenaList = new ArrayList<>();
        for (Arena arena : arenas) {
            if (arena.getDifficulty() == arenaDifficulty) {
                arenaList.add(arena);
            }
        }
        return arenaList;
    }

    public void startArena(Arena a) {
        a.setArenaState(ArenaState.STARTING);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "5 secondes"), 20L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "4 secondes"), 20L * 2);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "3 secondes"), 20L * 3);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "2 secondes"), 20L * 4);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "1 seconde"), 20L * 5);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            a.setArenaState(ArenaState.IN_PROGRESS);
            startWave(a);
        }, 20L * 6);
    }

    public void startWave(Arena a) {
        guiManager = plugin.getGuiManager();
        rewardsManager = plugin.getRewardsManager();
        a.setActualWave(a.getActualWave() + 1);
        int actualWave = a.getActualWave();
        System.out.println("Starting wave " + actualWave);
        if (actualWave == a.getMaxWaves()) {
            a.setArenaState(ArenaState.BOSS);
            int bossSpawns = a.getBossSpawn().size();
            int randomNum = 0;
            if (bossSpawns != 1) {
                randomNum = ThreadLocalRandom.current().nextInt(0, a.getBossSpawn().size());
            }
            ActiveMob boss = mobManager.spawnMob(a.getBossName(), a.getBossSpawn().get(randomNum));
            a.sendTitle("Vague finale", "Boss : " + boss.getType().getDisplayName());
        } else {
            if ((actualWave - 1) % 5 == 0 && actualWave != 1) {
                rewardsManager.setRewardsArena(a, a.getAlivePlayers().size());
                a.setArenaState(ArenaState.SELECTING_REWARD);
                for (Player p : a.getAlivePlayers()) {
                    guiManager.openGui(p, "rewards", a, p);
                }
            } else {
                spawnMobs(a, actualWave);
            }
        }
    }

    public void spawnMobs(Arena a, int actualWave) {
        a.sendTitle("Vague", Integer.toString(actualWave));
        int players = a.getPlayers().size();
        int mobCount = (int) (3 * actualWave * players / Math.PI); // 7
        a.setMobWave(mobCount);
        System.out.println("Spawning " + mobCount + " mobs...");
        for (int i = 0; i < a.getMobWave(); i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, a.getMobsSpawn().size());
            //System.out.println("Spawning zombie on position " + randomNum);
            Location loc = a.getMobsSpawn().get(randomNum);

            mobManager.spawnMob("ZombieTest", loc);
        }
    }
}
