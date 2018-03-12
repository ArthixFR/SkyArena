package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class GuiBase {

    private Player p;

    protected GuiBase(Player p) {
        this.p = p;
    }

    public abstract int size();

    public abstract boolean showReturnButton();

    public abstract String name();

    public void createGui() {
        Inventory inv = Bukkit.createInventory(p, size(), name());

        for (int i = 0; i < inv.getSize(); i++) {
            if (i < 9) {
                inv.setItem(i, ItemFormat.setItemName("", Material.STAINED_GLASS_PANE, 1, (byte)15, null));
            } else if (i % 9 == 0 || (i + 1) % 9 == 0) {
                inv.setItem(i, ItemFormat.setItemName("", Material.STAINED_GLASS_PANE, 1, (byte)15, null));
            } else if (i >= inv.getSize() - 9) {
                if (showReturnButton()) {
                    if (i == (inv.getSize() - 6)) {
                        inv.setItem(i, ItemFormat.setItemName("§c§lPrécédent", Material.SPECTRAL_ARROW, 1, (byte)0, null));
                    } else if (i == (inv.getSize() - 4)) {
                        inv.setItem(i, ItemFormat.setItemName("§c§lRetour en jeu", Material.DARK_OAK_DOOR_ITEM, 1, (byte)0, null));
                    } else {
                        inv.setItem(i, ItemFormat.setItemName("", Material.STAINED_GLASS_PANE, 1, (byte)15, null));
                    }
                } else {
                    if (i == (inv.getSize() - 5)) {
                        inv.setItem(i, ItemFormat.setItemName("§c§lRetour en jeu", Material.DARK_OAK_DOOR_ITEM, 1, (byte)0, null));
                    } else {
                        inv.setItem(i, ItemFormat.setItemName("", Material.STAINED_GLASS_PANE, 1, (byte)15, null));
                    }
                }
            }
        }

        setContent(inv);

        p.openInventory(inv);
    }

    public abstract void setContent(Inventory inv);
}
