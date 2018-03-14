package fr.arthix.skyarena.arena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.titleapi.TitleAPI;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class ArenaManager {

    private List<Arena> arenas = new ArrayList<>();
    public Map<UUID, List<Location>> arenaCreationLocation = new HashMap<>();
    private SkyArena plugin;
    private MobManager mobManager;

    public ArenaManager(SkyArena plugin) {
        this.plugin = plugin;
        mobManager = plugin.getMythicMobs().getMobManager();
    }

    public Arena createArena(String name, Location border1, Location border2, int wavesMax, String bossName, ArenaDifficulty difficulty) {
        Arena arena = new Arena(name, border1, border2, wavesMax, bossName, difficulty);
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
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "10 secondes"), 20L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "9 secondes"), 20L * 2);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "8 secondes"), 20L * 3);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "7 secondes"), 20L * 4);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "6 secondes"), 20L * 5);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "5 secondes"), 20L * 6);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "4 secondes"), 20L * 7);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "3 secondes"), 20L * 8);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "2 secondes"), 20L * 9);
        Bukkit.getScheduler().runTaskLater(plugin, () -> a.sendTitle("Début dans", "1 seconde"), 20L * 10);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            a.setArenaState(ArenaState.IN_PROGRESS);
            startWave(a);
        }, 20L * 11);
    }

    public void startWave(Arena a) {
        a.setActualWave(a.getActualWave() + 1);
        int actualWave = a.getActualWave();
        System.out.println("Starting wave " + actualWave);
        if (actualWave == a.getMaxWaves()) {
            a.setArenaState(ArenaState.BOSS);
            // TODO: FAIRE SPAWN LE BOSS
        } else {
            a.sendTitle("Vague", Integer.toString(actualWave));
            int players = a.getPlayers().size();
            int mobCount = (int) (10 * actualWave * players / Math.PI);
            a.setMobWave(mobCount);
            System.out.println("Spawning " + mobCount + " mobs...");
            for (int i = 0; i < a.getMobWave(); i++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, a.getMobsSpawn().size());
                System.out.println("Spawning zombie on position " + randomNum);
                Location loc = a.getMobsSpawn().get(randomNum);

                mobManager.spawnMob("SkeletalKnight", loc);
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR
                // TODO: CORRIGER L'IA DE MERDE BY MOJANG PTDR

                /*World world = ((CraftWorld) loc.getWorld()).getHandle();

                CraftZombie zombie = new CraftZombie((CraftServer) Bukkit.getServer(), new EntityZombie(world));

                setOrAdAttribute(zombie.getHandle(),"generic.followRange", 64.0D);

                world.addEntity(zombie.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);

                loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);*/
            }
        }
    }

    public static void setOrAdAttribute(Entity entity, String name, double base) {
        NBTTagCompound compound = new NBTTagCompound();
        entity.c(compound);
        NBTTagList attributeList;
        NBTTagCompound attribute = new NBTTagCompound();
        attribute.set("Name", new NBTTagString(name));
        attribute.set("Base", new NBTTagDouble(base));
        if (compound.hasKey("Attributes")) {
            attributeList = compound.getList("Attributes", 10);
            for (int compt = 0; compt < attributeList.size(); compt++) {
                if (attributeList.get(compt).get("Name").equals(attribute.get("Name"))) {
                    attributeList.remove(compt);
                }
            }
            attributeList.add(attribute);
        } else {
            attributeList = new NBTTagList();
            attributeList.add(attribute);
        }
        compound.set("Attributes", attributeList);
        ((EntityLiving) entity).a(compound);
    }
}
