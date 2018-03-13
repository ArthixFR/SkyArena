package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.gui.GuiBase;
import fr.arthix.skyarena.gui.GuiManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryEvent implements Listener {

    private GuiManager guiManager;

    public InventoryEvent(SkyArena plugin) {
        guiManager = plugin.getGuiManager();

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        GuiBase gui = guiManager.getGui(e.getInventory().getName());
        if (gui != null) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getClickedInventory().getType() == InventoryType.CHEST && e.getCurrentItem().getType() != Material.AIR) {
                ItemStack is = e.getCurrentItem();
                Player p = (Player) e.getWhoClicked();
                switch (is.getType()) {
                    case DARK_OAK_DOOR_ITEM:
                        p.closeInventory();
                        break;
                    case SPECTRAL_ARROW:
                        guiManager.getGuiByName(gui.returnGui()).openGui(p, null);
                }
                gui.interact(e);
            }
        }
    }
}
