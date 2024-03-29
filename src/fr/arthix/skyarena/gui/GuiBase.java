package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class GuiBase {

    private SkyArena plugin;

    protected GuiBase(SkyArena plugin) {
        this.plugin = plugin;
    }

    public abstract int size();

    public abstract boolean showReturnButton();

    public abstract boolean showLeaveButton();

    public abstract boolean refreshGui();

    public abstract String title();

    public abstract String name();

    public abstract String returnGui();

    public void openGui(Player p, Object... arg) {
        final Inventory inv = Bukkit.createInventory(p, size(), title());

        for (int i = 0; i < inv.getSize(); i++) {
            if (i < 9) {
                inv.setItem(i, ItemFormat.setItemName(null, Material.STAINED_GLASS_PANE, 1, (byte)15, null, false));
            } else if (i % 9 == 0 || (i + 1) % 9 == 0) {
                inv.setItem(i, ItemFormat.setItemName(null, Material.STAINED_GLASS_PANE, 1, (byte)15, null, false));
            } else if (i >= inv.getSize() - 9) {
                if (showReturnButton()) {
                    if (i == (inv.getSize() - 6)) {
                        inv.setItem(i, ItemFormat.setItemName("§c§lPrécédent", Material.SPECTRAL_ARROW, 1, (byte)0, null, false));
                    } else if (i == (inv.getSize() - 4)) {
                        inv.setItem(i, ItemFormat.setItemName("§c§lRetour en jeu", Material.DARK_OAK_DOOR_ITEM, 1, (byte)0, null, false));
                    } else {
                        inv.setItem(i, ItemFormat.setItemName(null, Material.STAINED_GLASS_PANE, 1, (byte)15, null, false));
                    }
                } else {
                    if (i == (inv.getSize() - 5) && showLeaveButton()) {
                        inv.setItem(i, ItemFormat.setItemName("§c§lRetour en jeu", Material.DARK_OAK_DOOR_ITEM, 1, (byte)0, null, false));
                    } else {
                        inv.setItem(i, ItemFormat.setItemName(null, Material.STAINED_GLASS_PANE, 1, (byte)15, null, false));
                    }
                }
            }
        }

        setContent(inv, arg);

        if (refreshGui()) {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (p.getOpenInventory().getTopInventory() != null) {
                    if (p.getOpenInventory().getTopInventory().getTitle().equals(title())) {
                        setContent(inv, arg);
                        //p.updateInventory();
                    }
                }
            }, 20L, 20L);
        }

        p.openInventory(inv);
    }

    public abstract void setContent(Inventory inv, Object... arg);

    public abstract void interact(InventoryClickEvent e);
}
