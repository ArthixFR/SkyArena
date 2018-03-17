package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiMain extends GuiBase {

    private GuiManager guiManager;

    public GuiMain(SkyArena plugin, GuiManager guiManager) {
        super(plugin);
        this.guiManager = guiManager;
    }

    @Override
    public int size() {
        return 5 * 9;
    }

    @Override
    public boolean showReturnButton() {
        return false;
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
        return "Menu principal";
    }

    @Override
    public String name() {
        return "main";
    }

    @Override
    public String returnGui() {
        return null;
    }

    @Override
    public void setContent(Inventory inv, Object... arg) {
        inv.setItem(20, ItemFormat.setItemName("§b§lJouer", Material.CONCRETE, 1, (byte)3, null, false));
        inv.setItem(22, ItemFormat.setItemName("§6§lClassement", Material.TOTEM, 1, (byte)0, null, false));
        inv.setItem(24, ItemFormat.setItemName("§e§lMes stats", Material.CONCRETE, 1, (byte)4, null, false));

    }

    @Override
    public void interact(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack is = e.getCurrentItem();
        if (is.getType() == Material.CONCRETE && is.getDurability() == 3) {
            guiManager.openGui(p, "arena_select");
        } else if (is.getType() == Material.TOTEM) {
            p.sendMessage("§cSoon...");
            //guiManager.openGui(p, "leaderboard");
        }
    }
}
