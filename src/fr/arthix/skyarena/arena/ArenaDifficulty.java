package fr.arthix.skyarena.arena;

import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum ArenaDifficulty {
    EASY("Facile", ItemFormat.setItemName("§e§lArène facile", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true)),
    MEDIUM("Moyen", ItemFormat.setItemName("§6§lArène moyenne", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true)),
    HARD("Difficile", ItemFormat.setItemName("§c§lArène difficile", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true)),
    EXTREME("Extrème", ItemFormat.setItemName("§0§lArène extrème", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true)),
    EVENT("Event", ItemFormat.setItemName("§b§lArène event", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true));

    private String name;
    private ItemStack itemKey;

    ArenaDifficulty(String name, ItemStack itemKey) {
        this.name = name;
        this.itemKey = itemKey;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getItemKey() {
        return this.itemKey;
    }

    public static List<String> getDifficulties() {
        List<String> strings = new ArrayList<>();
        for (ArenaDifficulty difficulty : ArenaDifficulty.values()) {
            strings.add(difficulty.toString().toLowerCase());
        }
        return strings;
    }

    public static ArenaDifficulty getDifficultyByName(String str) {
        for (ArenaDifficulty difficulty : ArenaDifficulty.values()) {
            if (difficulty.toString().toLowerCase().equalsIgnoreCase(str)) {
                return difficulty;
            }
        }
        return null;
    }
}
