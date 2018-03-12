package fr.arthix.skyarena.arena;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public final class ArenaManager {

    private List<Arena> arenas = new ArrayList<>();

    public Map<UUID, List<Location>> arenaCreationLocation = new HashMap<>();

    public void createArena(String name, Location border1, Location border2, int wavesMax, String bossName, ArenaDifficulty difficulty) {
        arenas.add(new Arena(name, border1, border2, wavesMax, bossName, difficulty));
    }

    public void addPlayerToArena(Player p, Arena a) {
        a.addPlayers(Arrays.asList(p.getUniqueId()));
    }

    public void addPlayerToArena(List<UUID> p, Arena a) {
        a.addPlayers(p);
    }

    public Arena getArena(Player p) {
        return arenas.stream().findFirst().filter((a) -> a.getPlayers().contains(p.getUniqueId())).orElse(null);
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
            if(loc.getX() > dim[1] || loc.getX() < dim[0]) {
                return null;
            }

            dim[0] = loc1.getZ();
            dim[1] = loc2.getZ();
            Arrays.sort(dim);
            if(loc.getZ() > dim[1] || loc.getZ() < dim[0]) {
                return null;
            }

            dim[0] = loc1.getY();
            dim[1] = loc2.getY();
            Arrays.sort(dim);
            if(loc.getY() > dim[1] || loc.getY() < dim[0]) {
                return null;
            }

            return arena;
        }
        return null;
    }

    public void setArenaState(Arena a, ArenaState arenaState) {
        a.setArenaState(arenaState);
    }
}
