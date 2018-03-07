package fr.arthix.skyarena.arena;

import org.bukkit.Location;

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

    private String arenaName;
    private List<UUID> players;
    private List<Location> playersSpawn;
    private List<Location> mobsSpawn;
    private int wavesMax;
    private String bossName;
    private ArenaDifficulty difficulty;
    private ArenaState arenaState;

    public Arena(String arenaName, List<Location> playersSpawn, List<Location> mobsSpawn, int wavesMax, String bossName, ArenaDifficulty difficulty) {
        this.arenaName = arenaName;
        this.playersSpawn = playersSpawn;
        this.mobsSpawn = mobsSpawn;
        this.wavesMax = wavesMax;
        this.bossName = bossName;
        this.difficulty = difficulty;
        this.arenaState = ArenaState.FREE;
    }

    public String getArenaName() {
        return arenaName;
    }

    public ArenaDifficulty getDifficulty() {
        return difficulty;
    }

    public int getWavesMax() {
        return wavesMax;
    }

    public ArenaState getArenaState() {
        return arenaState;
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

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public void setArenaState(ArenaState arenaState) {
        this.arenaState = arenaState;
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

    public void setWavesMax(int wavesMax) {
        this.wavesMax = wavesMax;
    }

    public void addPlayers(List<UUID> players) {
        this.players.addAll(players);
    }
}
