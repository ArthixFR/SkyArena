package fr.arthix.skyarena.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemFormat {

    private SkyArena plugin;

    public ItemFormat(SkyArena plugin) {
        this.plugin = plugin;
    }

    public static ItemStack setItemName(String customName, Material material, int amount, byte damage, List lore, boolean glow) {
        ItemStack is = new ItemStack(material, amount, damage);
        ItemMeta im = is.getItemMeta();
        if (customName == null) {
            im.setDisplayName("§0");
        } else {
            im.setDisplayName(customName);
        }
        im.setLore(null);
        if (lore != null) {
            im.setLore(lore);
        }
        if (glow) {
            im.addEnchant(Enchantment.DURABILITY, 1, false);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack setPotion(String customName, PotionType potionType, int amount, boolean isLingering, List lore) {
        Potion potion = new Potion(potionType, 1);
        ItemStack is = potion.toItemStack(amount);
        if (isLingering) {
            is.setType(Material.LINGERING_POTION);
        }
        ItemMeta im = is.getItemMeta();
        if (customName == null) {
            im.setDisplayName("§0");
        } else {
            im.setDisplayName(customName);
        }
        im.setLore(null);
        if (lore != null) {
            im.setLore(lore);
        }
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack setSkullHead(String customName, int amount, String url, List lore) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, amount, (byte) 3);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(customName);
        im.setLore(null);
        if (lore != null) {
            im.setLore(lore);
        }
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = im.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        assert profileField != null;
        profileField.setAccessible(true);
        try {
            profileField.set(im, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack setGlowing(ItemStack is, boolean glow) {
        ItemMeta im = is.getItemMeta();
        if (glow) {
            im.addEnchant(Enchantment.DURABILITY, 1, false);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            if (im.hasEnchants()) {
                for (Enchantment enchantment : im.getEnchants().keySet()) {
                    im.removeEnchant(enchantment);
                }
                if (im.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                    im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            }
        }
        is.setItemMeta(im);
        return is;
    }

    public ItemStack arenaItem(Arena arena, Inventory inv, int slot) {
        ArenaState status = arena.getArenaState();
        Material material;
        List<String> lore = new ArrayList<>();
        byte metadata;
        switch (status) {
            case FREE:
                material = Material.CONCRETE;
                metadata = 5;
                break;
            case CLOSE:
                material = Material.CONCRETE;
                metadata = 14;
                break;
            case TELEPORTING:
                material = Material.CONCRETE;
                metadata = 1;
                break;
            default:
                material = Material.SKULL_ITEM;
                metadata = 3;
                break;
        }

        ItemStack is = new ItemStack(material, 1, metadata);

        if (material == Material.SKULL_ITEM) {
            SkullMeta sm = (SkullMeta) is.getItemMeta();
            lore.add("Status : " + arena.getArenaState().getName());
            if (status == ArenaState.IN_PROGRESS) {
                lore.add("Vague actuelle : " + arena.getActualWave() + " sur " + arena.getMaxWaves());
            } else if (status == ArenaState.BOSS) {
                lore.add("Boss : " + arena.getBossName());
            }
            lore.add("Joueurs : ");
            for (UUID uuid : arena.getPlayers()) {
                lore.add(" - " + Bukkit.getPlayer(uuid).getName());
            }

            sm.setDisplayName(arena.getArenaName());
            sm.setLore(lore);

            is.setItemMeta(sm);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                ItemStack item = inv.getItem(slot);
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(arena.getPlayers().get(0)));
                item.setItemMeta(skullMeta);
                inv.setItem(slot, item);

            });
        } else {
            ItemMeta im = is.getItemMeta();

            lore.add(arena.getArenaState().getName());
            im.setDisplayName(arena.getArenaName());
            im.setLore(lore);

            is.setItemMeta(im);
        }
        return is;
    }
}
