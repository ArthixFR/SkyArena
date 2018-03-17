package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetSpectatorEvent implements Listener {

    private ArenaManager arenaManager;

    public EntityTargetSpectatorEvent(SkyArena plugin) {
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player) {
            Player p = (Player) e.getTarget();
            Arena a = arenaManager.getArena(p);
            if (a != null) {
                if (a.getArenaState() != ArenaState.FREE && p.getGameMode() == GameMode.ADVENTURE && p.getAllowFlight()) {
                    e.setTarget(null);
                    e.setCancelled(true);
                }
            }
        }
    }
}
