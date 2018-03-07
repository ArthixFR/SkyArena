package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateArenaPositionEvent implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        ItemStack is = e.getItem();
        if (is != null) {
            if (is.getItemMeta().hasDisplayName()) {
                if (is.getItemMeta().getDisplayName().equals("§b§lSkyArena §7| §e§lHache de sélection")) {
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        List<Location> arenaLocation = SkyArena.arenaCreationLocation;
                        Location bLoc = e.getClickedBlock().getLocation();
                        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                            if (arenaLocation.isEmpty()) {
                                arenaLocation.add(bLoc);
                            } else {
                                arenaLocation.set(0, bLoc);
                            }
                            e.getPlayer().sendMessage("Position 1 définie (X:" + bLoc.getBlockX() + " Y:" + bLoc.getBlockY() + " Z:" + bLoc.getBlockZ() + ")");
                            e.setCancelled(true);
                        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (arenaLocation.size() == 1) {
                                arenaLocation.add(bLoc);
                            } else {
                                arenaLocation.set(1, bLoc);
                            }
                            e.getPlayer().sendMessage("Position 2 définie (X:" + bLoc.getBlockX() + " Y:" + bLoc.getBlockY() + " Z:" + bLoc.getBlockZ() + ")");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
