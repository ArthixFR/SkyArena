package fr.arthix.skyarena.rewards;

import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import org.bukkit.Material;

public class Rewards {

    private Material material;
    private String name;
    private int amount;
    private byte metadata;
    private Rarity rarity;
    private int percentage;
    private ArenaDifficulty arenaDifficulty;


    public Rewards(Material material, String name, int amount, byte metadata, int percentage, Rarity rarity, ArenaDifficulty arenaDifficulty) {
        this.material = material;
        this.name = name;
        this.amount = amount;
        this.metadata = metadata;
        this.rarity = rarity;
        this.percentage = percentage;
        this.arenaDifficulty = arenaDifficulty;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public byte getMetadata() {
        return metadata;
    }

    public void setMetadata(byte metadata) {
        this.metadata = metadata;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ArenaDifficulty getArenaDifficulty() {
        return arenaDifficulty;
    }

    public void setArenaDifficulty(ArenaDifficulty arenaDifficulty) {
        this.arenaDifficulty = arenaDifficulty;
    }
}
