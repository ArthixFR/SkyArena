package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuiArenas extends GuiBase {

    public GuiArenas(Player p) {
        super(p);
    }

    @Override
    public int size() {
        return 5 * 9;
    }

    @Override
    public boolean showReturnButton() {
        return true;
    }

    @Override
    public String name() {
        return "Arènes disponibles";
    }

    @Override
    public void setContent(Inventory inv) {
        inv.setItem(10, ItemFormat.setItemName("BITE", Material.IRON_BOOTS, 1, (byte)0, null));
    }
}
