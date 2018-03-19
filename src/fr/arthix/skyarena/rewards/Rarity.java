package fr.arthix.skyarena.rewards;

import fr.arthix.skyarena.arena.ArenaDifficulty;

public enum Rarity {
    COMMON("Commun", "§8(§7✬§8✬✬✬✬)"),
    UNCOMMON("Peu-commun", "§8(§a✬✬§8✬✬✬)"),
    RARE("Rare", "§8(§b✬✬✬§8✬✬)"),
    EPIC("Épique", "§8(§5✬✬✬✬§8✬)"),
    LEGENDARY("Légendaire", "§8(§6✬✬✬✬✬§8)");

    private String name;
    private String lore;

    Rarity(String name, String lore) {
        this.name = name;
        this.lore = lore;
    }

    public String getName() {
        return this.name;
    }
    public String getLore() {
        return this.lore;
    }

    public static Rarity getRarityByName(String str) {
        for (Rarity rarity : Rarity.values()) {
            if (rarity.toString().toLowerCase().equalsIgnoreCase(str)) {
                return rarity;
            }
        }
        return null;
    }
}
