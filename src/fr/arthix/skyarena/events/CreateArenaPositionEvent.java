package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.ArenaManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateArenaPositionEvent implements Listener {

    ArenaManager arenaManager;

    public CreateArenaPositionEvent(SkyArena plugin) {
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        ItemStack is = e.getItem();
        if (is != null) {
            if (is.getItemMeta().hasDisplayName()) {
                if (is.getItemMeta().getDisplayName().equals("§b§lSkyArena §7| §e§lHache de sélection")) {
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        Location bLoc = e.getClickedBlock().getLocation();
                        UUID uuid = e.getPlayer().getUniqueId();
                        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                            if (!arenaManager.arenaCreationLocation.containsKey(uuid)) {
                                arenaManager.arenaCreationLocation.put(uuid, new ArrayList<>());
                            }
                            if (arenaManager.arenaCreationLocation.get(uuid).isEmpty()) {
                                arenaManager.arenaCreationLocation.get(uuid).add(bLoc);
                            } else {
                                arenaManager.arenaCreationLocation.get(uuid).set(0, bLoc);
                            }
                            System.out.println(arenaManager.arenaCreationLocation.get(uuid).get(0));
                            e.getPlayer().sendMessage("Position 1 définie (X:" + bLoc.getBlockX() + " Y:" + bLoc.getBlockY() + " Z:" + bLoc.getBlockZ() + ")");
                            e.setCancelled(true);
                        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (!arenaManager.arenaCreationLocation.containsKey(uuid)) {
                                arenaManager.arenaCreationLocation.put(uuid, new ArrayList<>());
                            }
                            if (arenaManager.arenaCreationLocation.get(uuid).size() == 1) {
                                arenaManager.arenaCreationLocation.get(uuid).add(bLoc);
                            } else {
                                arenaManager.arenaCreationLocation.get(uuid).set(1, bLoc);
                            }
                            System.out.println(arenaManager.arenaCreationLocation.get(uuid).get(1));
                            e.getPlayer().sendMessage("Position 2 définie (X:" + bLoc.getBlockX() + " Y:" + bLoc.getBlockY() + " Z:" + bLoc.getBlockZ() + ")");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
