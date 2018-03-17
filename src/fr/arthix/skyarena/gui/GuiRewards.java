package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.rewards.Rewards;
import fr.arthix.skyarena.rewards.RewardsManager;
import fr.arthix.skyarena.utils.ItemFormat;
import javafx.util.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiRewards extends GuiBase {

    RewardsManager rewardsManager;
    ArenaManager arenaManager;

    public GuiRewards(SkyArena plugin) {
        super(plugin);
        rewardsManager = plugin.getRewardsManager();
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public int size() {
        return 3 * 9;
    }

    @Override
    public boolean showReturnButton() {
        return false;
    }

    @Override
    public boolean showLeaveButton() {
        return false;
    }

    @Override
    public boolean refreshGui() {
        return true;
    }

    @Override
    public String title() {
        return "Sélectionnez une récompense";
    }

    @Override
    public String name() {
        return "rewards";
    }

    @Override
    public String returnGui() {
        return null;
    }

    @Override
    public void setContent(Inventory inv, Object... arg) {
        if (arg[0] instanceof Arena) {
            Arena a = (Arena) arg[0];
            List<Rewards> rewards = rewardsManager.getRewardsArena(a);
            int remaining = rewardsManager.getRemaining(a);
            inv.setItem(13, ItemFormat.setItemName("§e§lTemps restant", Material.WATCH, remaining == 0 ? 1 : rewardsManager.getRemaining(a), (byte) 0, null, false));
            int i = 11;

            resetRewardGlass(inv);
            Pair<ItemStack, Integer> selectedReward = rewardsManager.getSelectedReward(((Player) arg[1]).getUniqueId());

            if (selectedReward != null) {
                setActiveReward(inv, selectedReward.getValue());
            }

            for (Rewards reward : rewards) {
                if (i == 13) i = 14;
                ItemStack is = rewardsManager.getRewardItemStack(reward);
                inv.setItem(i, is);
                i++;
            }
        }
    }

    @Override
    public void interact(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Arena a = arenaManager.getArena(p);
        ItemStack is = e.getCurrentItem();
        if (a != null) {
            if (rewardsManager.isReward(is, a.getDifficulty()) != null) {
                Inventory inv = e.getClickedInventory();
                resetRewardGlass(inv);
                setActiveReward(inv, e.getSlot());
                rewardsManager.addSelectedReward(p.getUniqueId(), inv.getItem(e.getSlot()), e.getSlot());
            }
        }
    }

    public void resetRewardGlass(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (i < 9 || i > inv.getSize() - 9) {
                inv.setItem(i, ItemFormat.setItemName(null, Material.STAINED_GLASS_PANE, 1, (byte)15, null, false));
            }
        }
    }

    public void setActiveReward(Inventory inv, int slot) {
        inv.setItem(slot - 9, ItemFormat.setItemName(null, Material.STAINED_GLASS_PANE, 1, (byte)5, null, false));
        inv.setItem(slot + 9, ItemFormat.setItemName(null, Material.STAINED_GLASS_PANE, 1, (byte)5, null, false));
    }
}
