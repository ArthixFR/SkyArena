package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiSelectArena extends GuiBase {

    private GuiManager guiManager;

    public GuiSelectArena(SkyArena plugin, GuiManager guiManager) {
        super(plugin);
        this.guiManager = guiManager;
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
    public boolean showLeaveButton() {
        return true;
    }

    @Override
    public boolean refreshGui() {
        return false;
    }

    @Override
    public String title() {
        return "Selection de l'arène";
    }

    @Override
    public String name() {
        return "arena_select";
    }

    @Override
    public String returnGui() {
        return "main";
    }

    @Override
    public void setContent(Inventory inv, Object... arg) {
        inv.setItem(20, ItemFormat.setItemName("Arène facile", Material.SKULL_ITEM, 1, (byte)0, null, false));
        inv.setItem(24, ItemFormat.setItemName("Arène difficile", Material.SKULL_ITEM, 1, (byte)1, null, false));
    }

    @Override
    public void interact(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack is = e.getCurrentItem();
        if (is.getType() == Material.SKULL_ITEM) {
            if (is.getDurability() == 0) {
                guiManager.openGui(p, "arenas", ArenaDifficulty.EASY);
            } else if (is.getDurability() == 1) {
                guiManager.openGui(p, "arenas", ArenaDifficulty.HARD);
            }
        }
    }
}
