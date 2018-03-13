package fr.arthix.skyarena.arena;

import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArenaDifficulty {
    EASY("Facile", ItemFormat.setItemName("§e§lArène facile", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true)),
    MEDIUM("Moyen", ItemFormat.setItemName("§6§lArène moyenne", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true)),
    HARD("Difficile", ItemFormat.setItemName("§c§lArène difficile", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true)),
    EXTREME("Extrème", ItemFormat.setItemName("§0§lArène extrème", Material.TRIPWIRE_HOOK, 1, (byte)0, null, true));

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
}
