package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {

    private SkyArena plugin;
    private ArenaManager arenaManager;

    public MoveEvent(SkyArena plugin) {
        this.plugin = plugin;
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Arena arena = arenaManager.getArena(e.getPlayer());
        if (arena != null) {
            if (arena.getPlayers().contains(e.getPlayer().getUniqueId()) && arena.getArenaState() == ArenaState.STARTING) {
                if (!e.getFrom().toVector().equals(e.getTo().toVector())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
