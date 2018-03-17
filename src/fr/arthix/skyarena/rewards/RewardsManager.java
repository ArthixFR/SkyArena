package fr.arthix.skyarena.rewards;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.gui.GuiBase;
import fr.arthix.skyarena.gui.GuiManager;
import fr.arthix.skyarena.gui.GuiRewards;
import fr.arthix.skyarena.utils.ItemFormat;
import javafx.util.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class RewardsManager {

    private List<Rewards> rewardsList = new ArrayList<>();
    private Map<Arena, List<Rewards>> rewardsArena = new HashMap<>();
    private Map<UUID, Pair<ItemStack, Integer>> selectedReward = new HashMap<>();
    private Map<Arena, Integer> remaining = new HashMap<>();
    private SkyArena plugin;
    private GuiManager guiManager;

    public RewardsManager(SkyArena plugin) {
        this.plugin = plugin;
    }

    public void addReward(Material material, String name, int amount, byte metadata, int percentage, Rarity rarity, ArenaDifficulty arenaDifficulty) {
        rewardsList.add(new Rewards(material, name, amount, metadata, percentage, rarity, arenaDifficulty));
    }

    public void loadRewards() {
        addReward(Material.BEDROCK, "§c§lBedrock", 1, (byte)0, 10, Rarity.RARE, ArenaDifficulty.EASY);
        addReward(Material.COAL_BLOCK, "§fCoal block", 16, (byte)0, 50, Rarity.COMMON, ArenaDifficulty.EASY);
        addReward(Material.BEACON, "§fBeacon", 1, (byte)0,30, Rarity.UNCOMMON, ArenaDifficulty.EASY);
        addReward(Material.DIAMOND, "§fDiamant", 64, (byte)0, 5, Rarity.EPIC, ArenaDifficulty.EASY);
        addReward(Material.EMERALD_BLOCK, "§fBloc d'émeraude", 16, (byte)0, 1, Rarity.LEGENDARY, ArenaDifficulty.EASY);
    }

    public Rewards getRandomReward(ArenaDifficulty arenaDifficulty) {
        RandomCollection<Rewards> randomCollection = new RandomCollection<>();

        for (Rewards rewards : rewardsList) {
            if (rewards.getArenaDifficulty() == arenaDifficulty) {
                randomCollection.add(rewards.getPercentage(), rewards);
            }
        }

        return randomCollection.next();
    }

    public Rewards isReward(ItemStack is, ArenaDifficulty arenaDifficulty) {
        if (is == null) return null;
        if (!is.hasItemMeta()) return null;
        for (Rewards rewards : rewardsList) {
            if (rewards.getName().equals(is.getItemMeta().getDisplayName()) && rewards.getMaterial() == is.getType() && rewards.getAmount() == is.getAmount() && rewards.getMetadata() == is.getDurability() && rewards.getArenaDifficulty() == arenaDifficulty) {
                return rewards;
            }
        }
        return null;
    }

    public Rewards getSelectedReward(Player p, Arena a) {
        Pair<ItemStack, Integer> reward = selectedReward.get(p.getUniqueId());
        if (reward == null) {
            return rewardsArena.get(a).get(0);
        }
        Rewards rewards = isReward(reward.getKey(), a.getDifficulty());
        return rewards == null ? rewardsArena.get(a).get(0) : rewards;
    }

    public void setRewardsArena(Arena a, int amount) {
        List<Rewards> rewards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            rewards.add(getRandomReward(a.getDifficulty()));
        }
        rewardsArena.put(a, rewards);
    }

    public List<Rewards> getRewardsArena(Arena a) {
        return rewardsArena.get(a);
    }

    public ItemStack getRewardItemStack(Rewards rewards) {
        return ItemFormat.setItemName(rewards.getName(), rewards.getMaterial(), rewards.getAmount(), rewards.getMetadata(), Arrays.asList(rewards.getRarity().getLore()), false);
    }


    public int getRemaining(Arena a) {
        return remaining.get(a);
    }

    public void setRemaining(Arena a, int remaining) {
        if (this.remaining.containsKey(a)) {
            this.remaining.replace(a, remaining);
        } else {
            this.remaining.put(a, remaining);
        }
    }

    public Pair<ItemStack, Integer> getSelectedReward(UUID uuid) {
        return selectedReward.get(uuid);
    }

    public void addSelectedReward(UUID uuid, ItemStack is, int slot) {
        if (this.selectedReward.containsKey(uuid)) {
            this.selectedReward.replace(uuid, new Pair<>(is, slot));
        } else {
            this.selectedReward.put(uuid, new Pair<>(is, slot));
        }
    }

    public void removeSelectedReward(UUID uuid) {
        this.selectedReward.remove(uuid);
    }
}
