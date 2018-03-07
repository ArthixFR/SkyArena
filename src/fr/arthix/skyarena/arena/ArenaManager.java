package fr.arthix.skyarena.arena;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class ArenaManager {

    private List<Arena> arenas = new ArrayList<>();

    public void createArena(String name, List<Location> playersSpawn, List<Location> mobsSpawn, int wavesMax, String bossName, ArenaDifficulty difficulty) {
        arenas.add(new Arena(name, playersSpawn, mobsSpawn, wavesMax, bossName, difficulty));
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

    public void setArenaState(Arena a, ArenaState arenaState) {
        a.setArenaState(arenaState);
    }
}
