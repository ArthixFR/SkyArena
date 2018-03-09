package fr.arthix.skyarena.arena;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
    private Collection<Location> playersSpawn = new ArrayList<>();
    private List<Location> mobsSpawn = new ArrayList<>();
    private Location border1;
    private Location border2;
    private int maxWaves;
    private int actualWave;
    private String bossName;
    private ArenaDifficulty difficulty;
    private ArenaState state;

    public Arena(String arenaName, Location border1, Location border2, int maxWaves, String bossName, ArenaDifficulty difficulty) {
        this.name = arenaName;
        this.border1 = border1;
        this.border2 = border2;
        this.maxWaves = maxWaves;
        this.actualWave = 0;
        this.bossName = bossName;
        this.difficulty = difficulty;
        this.state = ArenaState.FREE;
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

    public Collection<Location> getPlayersSpawn() {
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
        this.mobsSpawn = mobsSpawn;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public void setPlayersSpawn(List<Location> playersSpawn) {
        this.playersSpawn = playersSpawn;
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

    public void addPlayerSpawn(Location loc) {
        this.playersSpawn.add(loc);
    }

    public void addMobsSpawn(Location loc) {
        this.mobsSpawn.add(loc);
    }
}
