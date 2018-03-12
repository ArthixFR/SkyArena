package fr.arthix.skyarena.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuiSelectBoss extends GuiBase {

    public GuiSelectBoss(Player p) {
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
        return "Selection de l'arène";
    }

    @Override
    public void setContent(Inventory inv) {

    }
}
