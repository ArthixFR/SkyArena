package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import fr.arthix.skyarena.gui.GuiBase;
import fr.arthix.skyarena.gui.GuiManager;
import fr.arthix.skyarena.gui.GuiRewards;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryEvent implements Listener {

    private GuiManager guiManager;
    private ArenaManager arenaManager;
    private SkyArena plugin;

    public InventoryEvent(SkyArena plugin) {
        this.plugin = plugin;
        guiManager = plugin.getGuiManager();
        arenaManager = plugin.getArenaManager();
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
                        guiManager.getGuiByName(gui.returnGui()).openGui(p);
                }
                gui.interact(e);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        GuiBase gui = guiManager.getGui(e.getInventory().getName());
        if (gui != null) {
            if (gui instanceof GuiRewards) {
                Player p = (Player) e.getPlayer();
                Arena a = arenaManager.getArena(p);
                if (a != null) {
                    if (a.getArenaState() == ArenaState.SELECTING_REWARD) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> gui.openGui(p, a, p), 1L);
                    }
                }
            }
        }
    }
}
