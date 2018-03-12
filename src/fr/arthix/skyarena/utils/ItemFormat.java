package fr.arthix.skyarena.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class ItemFormat {
    public static ItemStack setItemName(String customName, Material material, int amount, byte damage, List lore) {
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
}
