package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageEvent implements Listener {

    private ArenaManager arenaManager;

    public EntityDamageEvent(SkyArena plugin) {
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (p.getGameMode() == GameMode.ADVENTURE) {
                Arena a = arenaManager.getArena(p);
                if (a != null) {
                    if (a.getArenaState() != ArenaState.FREE) {
                        e.setCancelled(true);
                    }
                }
            }
        }

    }
}
