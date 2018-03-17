package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropEvent implements Listener {

    private ArenaManager arenaManager;

    public ItemDropEvent(SkyArena plugin) {
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        Arena a = arenaManager.getArena(p);
        if (a != null) {
            if (p.getGameMode() == GameMode.ADVENTURE && a.getArenaState() != ArenaState.FREE) {
                e.setCancelled(true);
            }
        }
    }
}
